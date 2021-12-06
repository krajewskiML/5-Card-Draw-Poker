package pl.agh.poker.player;

import pl.agh.poker.elements.Card;
import pl.agh.poker.exceptions.TooMuchCardsTooDiscard;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import java.util.*;

/**
 * Class responsible for handling all player logic
 */
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
    /**
     * Player Constructor
     */
    public Player() {
        cards = new ArrayList<>();
        setBalance(Constants.STARTING_BALANCE);
        setInGame(true);
        setAllIn(false);
        setFolded(false);
    }

    /**
     * Returns HandRanking of player
     * @return Handranking of player
     */
    public HandRanking getHandRanking() {
        return handRanking;
    }
    /**
     * Sets HandRanking of player
     * @param handRanking sets given handRanking for player
     */
    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }
    /**
     * Sets inGame of player
     * @param inGame sets given inGame for player
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * Returns if player can still play a round
     * @return returns true if player can play this round, else false
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Checks if player is all in
     * @return true if player is all in, else false
     */
    public boolean isAllIn() {
        return isAllIn;
    }
    /**
     * Checks if player has folded
     * @return true if player has folded, else false
     */
    public boolean isFolded() {
        return folded;
    }

    /**
     * checks if player performed any action this round
     * @return true if has made some move, else false
     */
    public boolean isHasParticipatedInRound() {
        return hasParticipatedInRound;
    }
    /**
     * Sets true if player has participated in this round
     * @param hasParticipatedInRound given state to set
     */
    public void setHasParticipatedInRound(boolean hasParticipatedInRound) {
        this.hasParticipatedInRound = hasParticipatedInRound;
    }

    /**
     * Checks if player can perform move this round by checking if he is in game and is not folded
     * @return true if can perform move, else false
     */
    public boolean isInRound() {
        return isInGame() && !isFolded();
    }
    /**
     * Returns player's balance in pool this round
     * @return balance in pool
     */
    public float getCurrentBalanceInPool() {
        return currentBalanceInPool;
    }

    /**
     * Sets current balance to be in pool
     * @param currentBalanceInPool amount that should be placed in a pool
     */
    public void setCurrentBalanceInPool(float currentBalanceInPool) {
        this.currentBalanceInPool = currentBalanceInPool;
    }
    /**
     * Adds given amount of money to player
     * @param amount amount that is to be added
     */
    public void addToBalance(float amount){
        setBalance(balance + amount);
    }

    /**
     * Sets if player is all in
     * @param allIn if the player is all in
     */
    public void setAllIn(boolean allIn) {
        this.isAllIn = allIn;
    }

    /**
     * Sets if player is folded
     * @param folded boolean if player has folded
     */
    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    /**
     * Sets balance of player
     * @param balance float that describes new balance
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    /**
     * Returns balance
     * @return float balance
     */
    public float getBalance() {
        return balance;
    }

    /**
     * Sets player name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Bets given amount by returning amount that is bet and increments current balance in pool by this amount
     * @param amount amount that is to be bet
     * @return amount that is actually bet
     */
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

    /**
     * Returns cards
     * @param indicesOfCardsToReturn indices of card ot return
     * @throws TooMuchCardsTooDiscard is being thrown when too many cards are being thrown
     */
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

    /**
     * Adds given cards to hand
     * @param newCards Cards that are to be added
     */
    public void fetchCards(List<Card> newCards) {
        if (cards.size() + newCards.size() == Constants.CARDS_IN_HAND) {
            cards.addAll(newCards);
        }
    }

    /**
     * Returns owned cards
     * @return all cards that are owned by the player
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * returns all cards
     * @throws TooMuchCardsTooDiscard if player will discard too much card
     */
    public void returnAllCards() throws TooMuchCardsTooDiscard {
        List<Integer> indices = Arrays.asList(0, 1, 2, 3, 4);
        returnCards(indices);
    }

    /**
     * Folds the player
     */
    public void fold() {
        setFolded(true);
    }
}
