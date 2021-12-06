package pl.agh.poker.elements;

import junit.framework.TestCase;

public class CardTest extends TestCase{
    public void testCard() {
        Card card = new Card(0, 1);
        assertEquals(card.getRank(), 0);
        assertEquals(card.getRank(), 0);
    }

    public void testToString1(){
        Card card = new Card(10, 1);
        assertEquals(card.toString(), "Queen Diamonds");
    }

    public void testSetRank(){
        Card card = new Card(10, 1);
        card.setRank(11);
        assertEquals(card.getRank(), 11);
    }

    public void testGetRank(){
        Card card = new Card(10, 1);
        assertEquals(card.getRank(), 10);
    }

}
