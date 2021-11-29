package pl.agh.poker.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card {
    private int rank;
    private int suite;

    private final Map<Integer, String> rankToString = new HashMap<Integer, String>() {{
        put(0, "Two");
        put(1, "Three");
        put(2, "Four");
        put(3, "Five");
        put(4, "Six");
        put(5, "Seven");
        put(6, "Eight");
        put(7, "Nine");
        put(8, "Ten");
        put(9, "Jack");
        put(10, "Queen");
        put(11, "King");
        put(12, "Ace");
    }};

    private final Map<Integer, String> suiteToString = new HashMap<Integer, String>() {{
        put(0, "Hearts");
        put(1, "Diamonds");
        put(2, "Clovers");
        put(3, "Pikes");
    }};

    public Card(int rank, int suite) {
        //initializes rank and suite values
        this.setRank(rank);
        this.setSuite(suite);
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
