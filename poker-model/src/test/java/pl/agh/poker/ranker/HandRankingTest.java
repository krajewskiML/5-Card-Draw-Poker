package pl.agh.poker.ranker;

import junit.framework.TestCase;
import pl.agh.poker.elements.Card;

import java.util.ArrayList;
import java.util.List;

public class HandRankingTest extends TestCase {
    public void testCheckPair(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(4, 1));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 1);

    }

    public void testCheckTwoPairs(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(2, 0));
        cards.add(new Card(4, 1));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 2);

    }

    public void testCheck3OfKind(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(1, 2));
        cards.add(new Card(2, 0));
        cards.add(new Card(4, 1));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 3);

    }
    public void testCheckStraight(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 2));
        cards.add(new Card(4, 0));
        cards.add(new Card(5, 1));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 4);
    }
    public void testCheckFlush(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(2, 0));
        cards.add(new Card(8, 0));
        cards.add(new Card(4, 0));
        cards.add(new Card(5, 0));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 5);
    }
    public void testCheckFullHouse(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 1));
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 2));
        cards.add(new Card(2, 0));
        cards.add(new Card(2, 0));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 6);
    }
    public void testCheck4OfKind(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 1));
        cards.add(new Card(1, 2));
        cards.add(new Card(1, 3));
        cards.add(new Card(1, 0));
        cards.add(new Card(2, 0));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 7);
    }
    public void testCheckStraightFlush(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(2, 0));
        cards.add(new Card(3, 0));
        cards.add(new Card(4, 0));
        cards.add(new Card(5, 0));

        HandRanking ranking = new HandRanking(cards);

        assertEquals(ranking.getStrengthOfHand(), 8);
    }
}
