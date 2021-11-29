package pl.agh.poker.elements;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    List<Card> cards;
    Random random = new Random();

    public Deck() {

        cards = new ArrayList<>();
        for (int suiteIdx = 0; suiteIdx < 4; ++suiteIdx) {
            for (int rankIdx = 0; rankIdx < 13; ++rankIdx) {
                cards.add(new Card(rankIdx, suiteIdx));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public List<Card> dealCards(int numOfCards) {
        if (this.cards.size() >= numOfCards) {
            List<Card> result = new ArrayList<>();
            int idxOfPickedCard;
            for (int idx = 0; idx < numOfCards; ++idx) {
                idxOfPickedCard = random.ints(0, cards.size()).findFirst().getAsInt();
                result.add(cards.remove(idxOfPickedCard));
            }
            return result;
        }
        // raise exception
        return null;
    }
}
