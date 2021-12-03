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

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public float getCurrentPool() {
        return currentPool;
    }

    public void setCurrentPool(float currentPool) {
        this.currentPool = currentPool;
    }

    public void addToPool(float amount) {
        setCurrentPool(getCurrentPool() + amount);
    }

    private ArrayList<Integer> getIndicesOfPlayers(){
        ArrayList<Integer> indices = new ArrayList<>();
        for(int i = 0;  i < players.size(); i++){
            indices.add(i);
        }
        return indices;
    }

    public void ante() {
        for (Player player : players) {
            if (player.getBalance() < Constants.ANTE) {
                player.setInGame(false);
            } else {
                addToPool(player.bet(Constants.ANTE));
            }
            player.setCurrentBalanceInPool(0);
        }
    }

    public void deal() {
        for (Player player : players) {
            if (player.isInGame()) {
                player.fetchCards(deck.dealCards(Constants.CARDS_IN_HAND));
            }
        }
    }

    public void rateHands() {
        for (Player player : players) {
            if (player.isInGame()) {
                player.setHandRanking(new HandRanking(player.getCards()));
            }
        }
    }

    public List<Player> getBestPlayers() {
        rateHands();
        PlayerComparator playerComparator = new PlayerComparator();
        players.sort(playerComparator);
        Collections.reverse(players);
        List<Player> bestPlayers = new ArrayList<>();
        bestPlayers.add(players.get(0));
        for (int idx = 1; idx < players.size(); idx++) {
            if (Objects.equals(bestPlayers.get(0).getHandRanking(), players.get(idx).getHandRanking())) {
                bestPlayers.add(players.get(idx));
            }
        }
        return bestPlayers;
    }

    public void performBettingPhase() throws IOException {
        float max_bet = 0, allBets = 0, currentBet;
        Player currentPlayer;

        for(Player player: players){
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
            for(int otherPlayers = 0; otherPlayers < players.size(); ++otherPlayers){
                if(otherPlayers != playerIdx){
                    players.get(otherPlayers).sendMessageWithoutResponse(String.valueOf(currentPlayer.getName()) + " current bet is: " + String.valueOf(currentPlayer.getCurrentBalanceInPool()));
                }
            }
            allBets += currentBet;
        }
        boolean allEven = false;
        for(int playerIdx = 0; !allEven; playerIdx = (playerIdx + 1) % players.size()){
            // checks if this player should perform any action
            currentPlayer = players.get(playerIdx);
            if (currentPlayer.isAllIn() || currentPlayer.isFolded() || !currentPlayer.isInGame() || currentPlayer.getCurrentBalanceInPool() == max_bet) {
            
            }else{
                // if this player has to do smth then ask him to place a bet
                currentBet = currentPlayer.placeBet(max_bet);
                if (max_bet < currentPlayer.getCurrentBalanceInPool()) {
                    max_bet = currentPlayer.getCurrentBalanceInPool();
                }
                //inform other players about his movement
                for(int otherPlayers = 0; otherPlayers < players.size(); ++otherPlayers){
                    if(otherPlayers != playerIdx){
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
                }else{
                    allEven = false;
                }
            }
        }


        for(Player player: players){
            player.setCurrentBalanceInPool(0);
        }
        setCurrentPool(getCurrentPool() + allBets);
    }

    public void swapCards() throws IOException {
        for (Player player : players) {
            player.showCards();
            player.swapCards(deck);
        }
    }

    private void distributePool(){
        List<Player> bestPlayers = getBestPlayers();
        int splitCurrentPoolBy = bestPlayers.size();
        float amountWon = currentPool / splitCurrentPoolBy;
        for(Player player : bestPlayers){
            player.addToBalance(amountWon);
        }
        setCurrentPool(0);
        for(Player player: players){
            player.sendMessageWithoutResponse("Your current balance is: " + String.valueOf(player.getBalance()));
        }
    }

    private void sendMessageToPlayers(ArrayList<Integer> indices, String message){
        for(Integer idx: indices){
            players.get(idx).sendMessageWithoutResponse(message);
        }
    }

    public void performRound() throws IOException {
        ante();
        System.out.println(getCurrentPool());
        deal();
        swapCards();
        performBettingPhase();
        System.out.println(getCurrentPool());
        swapCards();
        performBettingPhase();
        System.out.println(getCurrentPool());
        distributePool();
    }
}
