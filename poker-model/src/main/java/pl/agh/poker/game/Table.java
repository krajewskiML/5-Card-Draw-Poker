package pl.agh.poker.game;

import pl.agh.poker.elements.Deck;
import pl.agh.poker.exceptions.NotEnoughCardsInDeckException;
import pl.agh.poker.exceptions.TooMuchCardsTooDiscard;
import pl.agh.poker.exceptions.WrongCardsIndicesException;
import pl.agh.poker.player.Player;
import pl.agh.poker.player.PlayerComparator;
import pl.agh.poker.player.SocketPlayer;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import pl.agh.poker.exceptions.WrongNumberOfPlayers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Class responsible for handling round and game logic. It also keeps track of players and
 * sends them info and responses.
 */

public class Table {
    List<SocketPlayer> players;
    float currentPool;
    Deck deck;
    ServerSocket server;

    /**
     * Constructor
     * @param numberOfPlayers this number indicates how many players need to connect before
     * the game the game starts
     * @throws IOException
     * @throws WrongNumberOfPlayers is thrown when number of players is not between 2 and 5
     */
    public Table(int numberOfPlayers) throws IOException, WrongNumberOfPlayers {

        if(numberOfPlayers > Constants.MAX_NUMBER_OF_PLAYERS || numberOfPlayers < Constants.MIN_NUMBER_OF_PLAYERS){
            throw new WrongNumberOfPlayers(numberOfPlayers);
        }

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

    private void deal() throws NotEnoughCardsInDeckException {
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
        float maxBet = 0;
        float allBets = 0;
        float currentBet;
        List<SocketPlayer> consideredPlayers;

        boolean allPlayersDone = false;
        while(!allPlayersDone){
            //gets players that are in game and has not folded
            consideredPlayers = getActivePlayers();
            for(SocketPlayer player: consideredPlayers){
                //if player is not all in and has less in pool then others then he needs to do smth
                if((!player.isAllIn() && player.getCurrentBalanceInPool() <  maxBet) || !player.isHasParticipatedInRound() ){
                    currentBet = player.placeBet(maxBet);
                    allBets += currentBet;
                    maxBet = Math.max(maxBet, currentBet);
                    informOtherPlayersAboutBet(currentBet, player);
                }
            }
            consideredPlayers = getActivePlayers();
            allPlayersDone = true;
            for(SocketPlayer player: consideredPlayers){
                //if player is not all in and has less in pool then others then he needs to do smth
                if(!player.isAllIn() && player.getCurrentBalanceInPool() <  maxBet) {
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

    private void swapCards() throws IOException, WrongCardsIndicesException, NotEnoughCardsInDeckException, TooMuchCardsTooDiscard {
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
        StringBuilder messageAboutWinner;
        if(bestPlayers.size() > 1){
            messageAboutWinner = new StringBuilder("Winners are: ");
        }else{
            messageAboutWinner = new StringBuilder("Winner is: ");
        }
        for(SocketPlayer player: bestPlayers){
            messageAboutWinner.append(player.getName()).append(" ");
        }
        messageAboutWinner.append("with a hand ranking of: ").append(bestPlayers.get(0).getHandRanking().toString());
        for (SocketPlayer player : players) {
            player.sendMessageWithoutResponse(messageAboutWinner.toString());
            player.sendMessageWithoutResponse("Your current balance is: " + player.getBalance());
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
        return playersHavingMoney <= 1;
    }

    private void refreshPlayersState() throws TooMuchCardsTooDiscard {
        for(SocketPlayer player: players){
            player.setFolded(false);
            player.setAllIn(false);
            player.setCurrentBalanceInPool(0);
            player.returnAllCards();
        }
    }

    private void performRound() throws IOException, WrongCardsIndicesException, NotEnoughCardsInDeckException, TooMuchCardsTooDiscard {
        ante();
        System.out.println("Curent pool after ante is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "ANTE!");
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after ante is equal to: " + getCurrentPool());
        deal();
        swapCards();
        sendMessageToPlayers(getIndicesOfPlayers(), "FIRST ROUND OF BETTING BEGINS!");
        performBettingPhase();
        System.out.println("Curent pool after first betting phase is  is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after first betting phase is equal to: " + getCurrentPool());
        swapCards();
        sendMessageToPlayers(getIndicesOfPlayers(), "SECOND ROUND OF BETTING BEGINS!");
        performBettingPhase();
        System.out.println("Curent pool after second betting is equal to: " + getCurrentPool());
        sendMessageToPlayers(getIndicesOfPlayers(), "Curent pool after second betting is equal to: " + getCurrentPool());

        distributePool();
        refreshPlayersState();
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


    /**
     * Performs one whole game until one player is winning or all players don't want to play
     * @throws IOException
     * @throws WrongCardsIndicesException
     * @throws NotEnoughCardsInDeckException
     * @throws TooMuchCardsTooDiscard
     */
    public void performGame() throws IOException, WrongCardsIndicesException, NotEnoughCardsInDeckException, TooMuchCardsTooDiscard {
        while (!checkIfGameIsDone()) {
            performRound();
            if (!checkIfPlayersWantToContinue()) {
                break;
            }
        }
        endGame();
    }
}
