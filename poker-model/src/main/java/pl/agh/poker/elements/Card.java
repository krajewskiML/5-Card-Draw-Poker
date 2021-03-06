package pl.agh.poker.elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing single card
 */
public class Card {
    private int rank;
    private int suite;

    private final Map<Integer, String> rankToString = new HashMap<>();


    private final Map<Integer, String> suiteToString = new HashMap<>();

    /**
     * Card constructor
     * @param rank number from 0-12 indicating strength of card
     * @param suite number from 0-3 indicating color of card
     */
    public Card(int rank, int suite) {
        //initializes rank and suite values
        this.setRank(rank);
        this.setSuite(suite);
        rankToString.put(0, "Two");
        rankToString.put(1, "Three");
        rankToString.put(2, "Four");
        rankToString.put(3, "Five");
        rankToString.put(4, "Six");
        rankToString.put(5, "Seven");
        rankToString.put(6, "Eight");
        rankToString.put(7, "Nine");
        rankToString.put(8, "Ten");
        rankToString.put(9, "Jack");
        rankToString.put(10, "Queen");
        rankToString.put(11, "King");
        rankToString.put(12, "Ace");

        suiteToString.put(0, "Hearts");
        suiteToString.put(1, "Diamonds");
        suiteToString.put(2, "Clovers");
        suiteToString.put(3, "Pikes");
    }

    /**
     * Sets rank of card
     * @param rnk takes number from 0-12 and assigns is it as rank
     */
    public void setRank(int rnk) {
        if (rnk >= 0 && rnk <= 12)
            this.rank = rnk;
    }

    /**
     * Gets rank of card
     * @return rank from 0-12
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets suite
     * @param suite takes number from 0-3 and assigns it as suite
     */
    public void setSuite(int suite) {
        if (suite >= 0 && suite <= 3)
            this.suite = suite;
    }

    /**
     * Returns suite
     * @return suite from 0-3
     */
    public int getSuite() {
        return suite;
    }

    /**
     * String representation of card
     * @return String representation of card
     */
    public String toString() {
        return rankToString.get(this.rank) + " " + suiteToString.get(this.suite);
    }
}
