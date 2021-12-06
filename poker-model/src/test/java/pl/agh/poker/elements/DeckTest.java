package pl.agh.poker.elements;

import junit.framework.TestCase;
import pl.agh.poker.exceptions.NotEnoughCardsInDeckException;

import java.util.ArrayList;
import java.util.List;

public class DeckTest extends TestCase {

    public void testDeck() {
        Deck deck = new Deck();
        assertEquals(deck.cards.size(), 52);
    }

    public void testDeckCards() {
        Deck deck = new Deck();
        int color;
        ArrayList<Integer> colors = new ArrayList<>();
        for (Card card : deck.cards) {
            color = card.getSuite();
            if (!colors.contains(color)) {
                colors.add(color);
            }
        }
        assertEquals(colors.size(), 4);
    }

    public void testDeckCardsColor() {
        Deck deck = new Deck();
        int color;
        ArrayList<Integer> colors = new ArrayList<>();
        for (Card card : deck.cards) {
            color = card.getRank();
            if (!colors.contains(color)) {
                colors.add(color);
            }
        }
        assertEquals(colors.size(), 13);
    }

    public void testDealCards() throws NotEnoughCardsInDeckException {
        Deck deck = new Deck();
        List<Card> dealtCards = deck.dealCards(5);
        assertEquals(dealtCards.size(), 5);
    }
}
