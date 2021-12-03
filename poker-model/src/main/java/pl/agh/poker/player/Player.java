package pl.agh.poker.player;

import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.Deck;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Player {
    private List<Card> cards;
    private HandRanking handRanking;
    private String name;
    float balance;
    float currentBalanceInPool;
    boolean allIn;
    boolean folded;
    boolean inGame;

    public Player() {
        cards = new ArrayList<>();
        setBalance(Constants.STARTING_BALANCE);
        setInGame(true);
    }

    public Player(String name) {
        cards = new ArrayList<>();
        setName(name);
        setBalance(Constants.STARTING_BALANCE);
        setInGame(true);
    }

    public HandRanking getHandRanking() {
        return handRanking;
    }

    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInGame() {
        return inGame;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public boolean isFolded() {
        return folded;
    }

    public boolean isInRound() {
        return isInGame() && !isFolded();
    }

    public float getCurrentBalanceInPool() {
        return currentBalanceInPool;
    }

    public void setCurrentBalanceInPool(float currentBalanceInPool) {
        this.currentBalanceInPool = currentBalanceInPool;
    }

    public void addToBalance(float amount){
        setBalance(balance + amount);
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float bet(float amount) {
        if (getBalance() >= amount) {
            setBalance(this.balance - amount);
            setCurrentBalanceInPool(getCurrentBalanceInPool() + amount);
            if (getBalance() == 0) {
                setAllIn(true);
            }
            return amount;
        } else {
            //throw exception
            return 0;
        }
    }

    public List<Card> returnCards(List<Integer> indicesOfCardsToReturn) {
        if (indicesOfCardsToReturn.size() > Constants.CARDS_IN_HAND - 1) {
            //throws exception
            return null;
        } else {
            Collections.sort(indicesOfCardsToReturn);
            Collections.reverse(indicesOfCardsToReturn);
            List<Card> result = new ArrayList<>();
            for (int toRemove : indicesOfCardsToReturn) {
                result.add(cards.remove(toRemove));
            }
            return result;
        }
    }

    public void fetchCards(List<Card> newCards) {
        if (cards.size() + newCards.size() == Constants.CARDS_IN_HAND) {
            cards.addAll(newCards);
        }
    }

    public List<Card> getCards() {
        return cards;
    }


    public List<Card> returnAllCards() {
        List<Integer> indices = Arrays.asList(0, 1, 2, 3, 4);
        return returnCards(indices);
    }

    public void fold() {
        setFolded(true);
    }

    public void showCards() {
        System.out.println("twoje karty wersja podstawowa");
    }

    public void swapCards(Deck deck) throws IOException {
    }

    public float placeBet(float max_bet) throws IOException {
        return 0;
    }

    public void sendMessageWithoutResponse(String a) {
    }
}
