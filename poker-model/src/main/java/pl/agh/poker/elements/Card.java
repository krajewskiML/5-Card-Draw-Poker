package pl.agh.poker.elements;

import java.util.HashMap;
import java.util.Map;

public class Card {
    private int rank;
    private int suite;

    private final Map<Integer, String> rankToString = new HashMap<>();


    private final Map<Integer, String> suiteToString = new HashMap<>();


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


    public void setRank(int rnk) {
        if (rnk >= 0 && rnk <= 12)
            this.rank = rnk;
    }

    public int getRank() {
        return rank;
    }

    public void setSuite(int suite) {
        if (suite >= 0 && suite <= 3)
            this.suite = suite;
    }

    public int getSuite() {
        return suite;
    }

    public String toString() {
        return rankToString.get(this.rank) + " " + suiteToString.get(this.suite);
    }
}
