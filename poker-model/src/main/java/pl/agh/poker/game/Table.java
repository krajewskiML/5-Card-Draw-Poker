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
    List<SocketPlayer> players;
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
        for (SocketPlayer player : players) {
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

    private void rateHands(){
        for (SocketPlayer player : players) {
            player.setHandRanking(new HandRanking(player.getCards()));
        }
    }

    private List<SocketPlayer> getActivePlayers(){
        List<SocketPlayer> validPlayers = new ArrayList<>();
        for(SocketPlayer player: players){
            if(player.isInGame() && !player.isFolded()){
                validPlayers.add(player);
            }
        }
        return validPlayers;
    }

    private List<SocketPlayer> getBestPlayers(){
        rateHands();
        PlayerComparator playerComparator = new PlayerComparator();
        List<SocketPlayer> validPlayers = getActivePlayers();
        List<SocketPlayer> bestPlayers = new ArrayList<>();

        for(SocketPlayer player: validPlayers) {
            if (bestPlayers.isEmpty()) {
                bestPlayers.add(player);
            } else {
                if (Objects.equals(bestPlayers.get(0).getHandRanking(), player.getHandRanking())) {
                    bestPlayers.add(player);
                } else {
                    if (playerComparator.compare(bestPlayers.get(0), player) > 0) {
                        bestPlayers.clear();
                        bestPlayers.add(player);
                    }
                }
            }
        }
        return bestPlayers;
    }

    private void informOtherPlayersAboutBet(float bet, SocketPlayer player){
        String nameOfPlayer = player.getName();
        for(SocketPlayer player1: players){
            if(!Objects.equals(player1.getName(), nameOfPlayer)){
                player1.sendMessageWithoutResponse(nameOfPlayer + " has bet" + bet);
            }
        }
    }

    private void performBettingPhase() throws IOException {
        float max_bet = 0;
        float allBets = 0;
        float currentBet;
        List<SocketPlayer> consideredPlayers;

        boolean allPlayersDone = false;
        while(!allPlayersDone){
            //gets players that are in game and has not folded
            consideredPlayers = getActivePlayers();
            for(SocketPlayer player: consideredPlayers){
                //if player is not all in and has less in pool then others then he needs to do smth
                if((!player.isAllIn() && player.getCurrentBalanceInPool() <  max_bet) || !player.isHasParticipatedInRound() ){
                    currentBet = player.placeBet(max_bet);
                    allBets += currentBet;
                    max_bet = Math.max(max_bet, currentBet);
                    informOtherPlayersAboutBet(currentBet, player);
                }
            }
            consideredPlayers = getActivePlayers();
            allPlayersDone = true;
            for(SocketPlayer player: consideredPlayers){
                //if player is not all in and has less in pool then others then he needs to do smth
                if(!player.isAllIn() && player.getCurrentBalanceInPool() <  max_bet) {
                    allPlayersDone = false;
                    break;
                }
            }
        }


        for (SocketPlayer player : players) {
            player.setCurrentBalanceInPool(0);
            player.setHasParticipatedInRound(false);
        }
        setCurrentPool(getCurrentPool() + allBets);
    }

    private void swapCards() throws IOException {
        for (SocketPlayer player : players) {
            player.showCards();
            player.swapCards(deck);
        }
    }

    private void distributePool() {
        List<SocketPlayer> bestPlayers = getBestPlayers();
        int splitCurrentPoolBy = bestPlayers.size();
        float amountWon = currentPool / splitCurrentPoolBy;
        for (SocketPlayer player : bestPlayers) {
            player.addToBalance(amountWon);
        }
        setCurrentPool(0);
        for (SocketPlayer player : players) {
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
        for (SocketPlayer player : players) {
            if (player.getBalance() > 0) {
                ++playersHavingMoney;
            }
        }
        return !(playersHavingMoney > 1);
    }

    private void refreshPlayersState(){
        for(SocketPlayer player: players){
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
        for (SocketPlayer player : players) {
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
