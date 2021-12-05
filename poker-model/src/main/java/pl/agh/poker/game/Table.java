package pl.agh.poker.game;

import pl.agh.poker.elements.Deck;
import pl.agh.poker.player.Player;
import pl.agh.poker.player.PlayerComparator;
import pl.agh.poker.player.SocketPlayer;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Table {
    List<Player> players;
    float currentPool;
    Deck deck;
    ServerSocket server;

    public Table(int numberOfPlayers) throws IOException {

        System.out.println("Waiting for players...");

        server = new ServerSocket(Constants.PORT);
        setCurrentPool(0);
        setDeck(new Deck());

        players = new ArrayList<>();
        for (int idx = 0; idx < numberOfPlayers; ++idx) {
            Socket client = server.accept();
            SocketPlayer player = new SocketPlayer(client);
            players.add(player);
        }
    }

    private void setDeck(Deck deck) {
        this.deck = deck;
    }

    private void setPlayers(List<Player> players) {
        this.players = players;
    }

    private List<Player> getPlayers() {
        return players;
    }

    private float getCurrentPool() {
        return currentPool;
    }

    private void setCurrentPool(float currentPool) {
        this.currentPool = currentPool;
    }

    private void addToPool(float amount) {
        setCurrentPool(getCurrentPool() + amount);
    }

    private ArrayList<Integer> getIndicesOfPlayers() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            indices.add(i);
        }
        return indices;
    }

    private void ante() {
        for (Player player : players) {
            if (player.getBalance() < Constants.ANTE) {
                player.setInGame(false);
            } else {
                addToPool(player.bet(Constants.ANTE));
            }
            player.setCurrentBalanceInPool(0);
        }
    }

    private void deal() {
        for (Player player : players) {
            if (player.isInGame()) {
                player.fetchCards(deck.dealCards(Constants.CARDS_IN_HAND));
            }
        }
    }

    private void rateHands() {
        for (Player player : players) {
            player.setHandRanking(new HandRanking(player.getCards()));
        }
    }

    private List<Player> getBestPlayers() {
        rateHands();
        PlayerComparator playerComparator = new PlayerComparator();
        players.sort(playerComparator);
        Collections.reverse(players);
        List<Player> bestPlayers = new ArrayList<>();
        for (int idx = 0; idx < players.size(); ++idx) {
            if (players.get(idx).isInGame() && !players.get(idx).isFolded()) {
                if (bestPlayers.isEmpty()) {
                    bestPlayers.add(players.get(idx));
                } else {
                    if (Objects.equals(bestPlayers.get(0).getHandRanking(), players.get(idx).getHandRanking())) {
                        bestPlayers.add(players.get(idx));
                    } else {
                        break;
                    }
                }
            }
        }
        return bestPlayers;
    }

    private void performBettingPhase() throws IOException {
        float max_bet = 0, allBets = 0, currentBet;
        Player currentPlayer;

        for (Player player : players) {
            player.setCurrentBalanceInPool(0);
        }

        // perform first round betting
        for (int playerIdx = 0; playerIdx < players.size(); ++playerIdx) {
            currentPlayer = players.get(playerIdx);
            if (currentPlayer.isAllIn() || currentPlayer.isFolded() || !currentPlayer.isInGame()) {
                continue;
            }
            currentBet = currentPlayer.placeBet(max_bet);
            if (max_bet < currentBet) {
                max_bet = currentBet;
            }
            for (int otherPlayers = 0; otherPlayers < players.size(); ++otherPlayers) {
                if (otherPlayers != playerIdx) {
                    players.get(otherPlayers).sendMessageWithoutResponse(currentPlayer.getName() + " current bet is: " + String.valueOf(currentPlayer.getCurrentBalanceInPool()));
                }
            }
            allBets += currentBet;
        }
        boolean allEven = false;
        for (int playerIdx = 0; !allEven; playerIdx = (playerIdx + 1) % players.size()) {
            // checks if this player should perform any action
            currentPlayer = players.get(playerIdx);
            if (currentPlayer.isAllIn() || currentPlayer.isFolded() || !currentPlayer.isInGame() || currentPlayer.getCurrentBalanceInPool() == max_bet) {

            } else {
                // if this player has to do smth then ask him to place a bet
                currentBet = currentPlayer.placeBet(max_bet);
                if (max_bet < currentPlayer.getCurrentBalanceInPool()) {
                    max_bet = currentPlayer.getCurrentBalanceInPool();
                }
                //inform other players about his movement
                for (int otherPlayers = 0; otherPlayers < players.size(); ++otherPlayers) {
                    if (otherPlayers != playerIdx) {
                        players.get(otherPlayers).sendMessageWithoutResponse(String.valueOf(currentPlayer.getName()) + " current bet is: " + String.valueOf(String.valueOf(currentPlayer.getCurrentBalanceInPool())));
                    }
                }
                allBets += currentBet;
            }
            //check if any player should perform any more actions
            allEven = true;
            for (int playerIdxInside = 0; playerIdxInside < players.size(); ++playerIdxInside) {
                currentPlayer = players.get(playerIdxInside);
                if (currentPlayer.isAllIn() || currentPlayer.isFolded() || !currentPlayer.isInGame() || currentPlayer.getCurrentBalanceInPool() == max_bet) {
                    continue;
                } else {
                    allEven = false;
                }
            }
        }


        for (Player player : players) {
            player.setCurrentBalanceInPool(0);
        }
        setCurrentPool(getCurrentPool() + allBets);
    }

    private void swapCards() throws IOException {
        for (Player player : players) {
            player.showCards();
            player.swapCards(deck);
        }
    }

    private void distributePool() {
        List<Player> bestPlayers = getBestPlayers();
        int splitCurrentPoolBy = bestPlayers.size();
        float amountWon = currentPool / splitCurrentPoolBy;
        for (Player player : bestPlayers) {
            player.addToBalance(amountWon);
        }
        setCurrentPool(0);
        for (Player player : players) {
            player.sendMessageWithoutResponse("Your current balance is: " + String.valueOf(player.getBalance()));
        }
    }

    private void sendMessageToPlayers(ArrayList<Integer> indices, String message) {
        for (Integer idx : indices) {
            players.get(idx).sendMessageWithoutResponse(message);
        }
    }

    private boolean checkIfGameIsDone() {
        int playersHavingMoney = 0;
        for (Player player : players) {
            if (player.getBalance() > 0) {
                ++playersHavingMoney;
            }
        }
        return !(playersHavingMoney > 1);
    }

    private void refreshPlayersState(){
        for(Player player: players){
            player.setFolded(false);
            player.setAllIn(false);
            player.setCurrentBalanceInPool(0);
        }
    }


    public void performRound() throws IOException {
        refreshPlayersState();
        ante();
        System.out.println("Curent pool after ante is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after ante is equal to: " + getCurrentPool());
        deal();
        swapCards();
        performBettingPhase();
        System.out.println("Curent pool after first betting phase is  is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after first betting phase is equal to: " + getCurrentPool());
        swapCards();
        performBettingPhase();
        System.out.println("Curent pool after second betting is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after second betting is equal to: " + getCurrentPool());
        distributePool();
    }

    private boolean checkIfPlayersWantToContinue() throws IOException {
        int howManyWantToStop = 0;
        String answer;
        for (Player player : players) {
            answer = player.sendMessageGetResponse("Do you still want to to play? (yes, no): ");
            if (Objects.equals(answer, "no")) {
                ++howManyWantToStop;
            }
        }
        return howManyWantToStop != players.size();
    }

    private void endGame() throws IOException {
        sendMessageToPlayers(getIndicesOfPlayers(), "Thanks for game!");
        server.close();
    }


    public void performGame() throws IOException {
        while (!checkIfGameIsDone()) {
            performRound();
            if (!checkIfPlayersWantToContinue()) {
                break;
            }
        }
        endGame();
    }
}
