package pl.agh.poker.elements;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card firstCard, Card secondCard) {
        return Integer.compare(firstCard.getRank(), secondCard.getRank());
    }
}
