package pl.agh.poker.player;

import junit.framework.TestCase;
import pl.agh.poker.elements.Card;
import pl.agh.poker.ranker.HandRanking;

import java.util.ArrayList;
import java.util.List;

public class PlayerComparatorTest extends TestCase {
    public void testPairVSTriplePlayers(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(4, 1));

        HandRanking pairRanking = new HandRanking(cards);
        Player player1 = new Player();
        player1.setHandRanking(pairRanking);

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(1, 0));
        cards2.add(new Card(1, 1));
        cards2.add(new Card(1, 2));
        cards2.add(new Card(2, 0));
        cards2.add(new Card(4, 1));

        HandRanking tripleRanking = new HandRanking(cards2);

        PlayerComparator comparator = new PlayerComparator();
        Player player2 = new Player();
        player2.setHandRanking(tripleRanking);

        int result = comparator.compare(player1, player2);

        assertTrue(result < 0);
    }
    public void testPairVSPairPlayer(){
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, 0));
        cards.add(new Card(1, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(4, 1));

        HandRanking pairRanking = new HandRanking(cards);
        Player player1 = new Player();
        player1.setHandRanking(pairRanking);

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card(9, 0));
        cards2.add(new Card(9, 1));
        cards2.add(new Card(3, 2));
        cards2.add(new Card(2, 0));
        cards2.add(new Card(4, 1));

        HandRanking pairRanking2 = new HandRanking(cards2);

        PlayerComparator comparator = new PlayerComparator();
        Player player2 = new Player();
        player2.setHandRanking(pairRanking2);

        int result = comparator.compare(player1, player2);

        assertTrue(result < 0);
    }
}
