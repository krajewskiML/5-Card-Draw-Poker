package pl.agh.poker.player;

import pl.agh.poker.elements.Card;
import pl.agh.poker.exceptions.TooMuchCardsTooDiscard;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import java.util.*;

public class Player {
    private List<Card> cards;
    private HandRanking handRanking;
    private String name;
    float balance;
    float currentBalanceInPool;
    boolean isAllIn;
    boolean folded;
    boolean inGame;
    boolean hasParticipatedInRound;

    public Player() {
        cards = new ArrayList<>();
        setBalance(Constants.STARTING_BALANCE);
        setInGame(true);
        setAllIn(false);
        setFolded(false);
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
        return isAllIn;
    }

    public boolean isFolded() {
        return folded;
    }

    public boolean isHasParticipatedInRound() {
        return hasParticipatedInRound;
    }

    public void setHasParticipatedInRound(boolean hasParticipatedInRound) {
        this.hasParticipatedInRound = hasParticipatedInRound;
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
        this.isAllIn = allIn;
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

    public void returnCards(List<Integer> indicesOfCardsToReturn) throws TooMuchCardsTooDiscard {
        if (indicesOfCardsToReturn.size() > Constants.CARDS_IN_HAND) {
            throw new TooMuchCardsTooDiscard(indicesOfCardsToReturn.size());
        } else {
            Collections.sort(indicesOfCardsToReturn);
            Collections.reverse(indicesOfCardsToReturn);
            for (int toRemove : indicesOfCardsToReturn) {
                cards.remove(toRemove);
            }
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


    public void returnAllCards() throws TooMuchCardsTooDiscard {
        List<Integer> indices = Arrays.asList(0, 1, 2, 3, 4);
        returnCards(indices);
    }

    public void fold() {
        setFolded(true);
    }
}
