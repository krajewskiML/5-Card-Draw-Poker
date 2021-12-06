package pl.agh.poker.elements;


import pl.agh.poker.exceptions.NotEnoughCardsInDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class representing deck of cards
 */
public class Deck {
    List<Card> cards;
    Random random = new Random();

    /**
     * Deck constructor. Fills the deck with 52 unique cards
     */
    public Deck() {
        cards = new ArrayList<>();
        for (int suiteIdx = 0; suiteIdx < 4; ++suiteIdx) {
            for (int rankIdx = 0; rankIdx < 13; ++rankIdx) {
                cards.add(new Card(rankIdx, suiteIdx));
            }
        }
    }

    /** Returns given amount of numbers from deck and deletes them from deck
     * @param numOfCards indicates how many cards you want to receive
     * @return List containing numOfCards Cards
     */
    public List<Card> dealCards(int numOfCards) throws NotEnoughCardsInDeckException {
        if (cards.size() >= numOfCards) {
            List<Card> result = new ArrayList<>();
            List<Integer> alreadyPicked = new ArrayList<>();
            int idxOfPickedCard;
            for (int idx = 0; idx < numOfCards; ++idx) {

                idxOfPickedCard = random.ints(0, cards.size()).findFirst().getAsInt();
                while (alreadyPicked.contains(idxOfPickedCard)){
                    idxOfPickedCard = random.ints(0, cards.size()).findFirst().getAsInt();
                }
                alreadyPicked.add(idxOfPickedCard);
                result.add(cards.get(idxOfPickedCard));
            }
            Collections.sort(alreadyPicked);
            Collections.reverse(alreadyPicked);
            for(int idx: alreadyPicked){
                cards.remove(idx);
            }
            return result;
        }
        throw new NotEnoughCardsInDeckException();
    }
}
