package pl.agh.poker.elements;

import junit.framework.TestCase;

public class CardComparatorTest extends TestCase {
    public void testCompare1(){
        Card card1 = new Card(0, 3);
        Card card2 = new Card(1, 0);
        CardComparator comparator = new CardComparator();
        int result = comparator.compare(card1, card2);
        assertTrue(result < 0);
    }

    public void testCompare2(){
        Card card1 = new Card(1, 3);
        Card card2 = new Card(1, 0);
        CardComparator comparator = new CardComparator();
        int result = comparator.compare(card1, card2);
        assertEquals(result,0);
    }

    public void testCompare3(){
        Card card1 = new Card(1, 3);
        Card card2 = new Card(0, 0);
        CardComparator comparator = new CardComparator();
        int result = comparator.compare(card1, card2);
        assertTrue(result > 0);
    }
}
