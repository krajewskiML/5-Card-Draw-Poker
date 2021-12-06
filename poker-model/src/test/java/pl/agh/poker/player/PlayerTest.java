package pl.agh.poker.player;

import junit.framework.TestCase;
import pl.agh.poker.constants.Constants;
import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.Deck;
import pl.agh.poker.exceptions.NotEnoughCardsInDeckException;
import pl.agh.poker.exceptions.TooMuchCardsTooDiscard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTest extends TestCase {
    public void testPlayer(){
        Player player = new Player();
        assertEquals(player.getBalance(), Constants.STARTING_BALANCE);
        assertTrue(player.isInGame());
        assertFalse(player.isAllIn());
        assertFalse(player.isFolded());
    }

    public void testBet(){
        Player player = new Player();
        float amount = player.bet(20);
        assertEquals(20, amount);
        assertEquals(Constants.STARTING_BALANCE - 20, player.getBalance());
    }

    public void testFetchCards() throws NotEnoughCardsInDeckException {
        Player player = new Player();
        Deck deck = new Deck();
        List<Card> cardList = deck.dealCards(5);
        player.fetchCards(cardList);
        assertEquals(5, player.getCards().size());
    }

    public void testReturnCards() throws TooMuchCardsTooDiscard, NotEnoughCardsInDeckException {
        Player player = new Player();
        Deck deck = new Deck();
        List<Card> cardList = deck.dealCards(5);
        player.fetchCards(cardList);
        List<Integer> toDiscard = new ArrayList<>();
        toDiscard.add(0);
        toDiscard.add(1);
        toDiscard.add(2);
        player.returnCards(toDiscard);
        assertEquals(2, player.getCards().size());
    }





}
