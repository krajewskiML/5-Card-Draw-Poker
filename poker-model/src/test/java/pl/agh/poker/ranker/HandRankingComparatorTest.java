package pl.agh.poker.ranker;

import junit.framework.TestCase;
import pl.agh.poker.elements.Card;

import java.util.ArrayList;
import java.util.List;

public class HandRankingComparatorTest extends TestCase {
    public void testPairVSTriple(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(4, 1));

        HandRanking pairRanking = new HandRanking(cards);

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(1, 0));
        cards2.add(new Card(1, 1));
        cards2.add(new Card(1, 2));
        cards2.add(new Card(2, 0));
        cards2.add(new Card(4, 1));

        HandRanking tripleRanking = new HandRanking(cards2);

        HandRankingComparator comparator = new HandRankingComparator();

        int result = comparator.compare(pairRanking, tripleRanking);

        assertTrue(result < 0);
    }
    public void testPairVSPair(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(4, 1));

        HandRanking pairRanking = new HandRanking(cards);

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(9, 0));
        cards2.add(new Card(9, 1));
        cards2.add(new Card(3, 2));
        cards2.add(new Card(2, 0));
        cards2.add(new Card(4, 1));

        HandRanking pairRanking2 = new HandRanking(cards2);

        HandRankingComparator comparator = new HandRankingComparator();

        int result = comparator.compare(pairRanking, pairRanking2);

        assertTrue(result < 0);
    }

}
