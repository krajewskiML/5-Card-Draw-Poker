package pl.agh.poker.elements;

import java.util.Comparator;

/**
 * Class responsible for comparing cards
 */
public class CardComparator implements Comparator<Card> {
    /**
     * Compares cards
     * @param firstCard first card to compare
     * @param secondCard second card to compare
     * @return integer greater than zero if first card is better than the other, 0 if equal and less then zero in other case
     */
    @Override
    public int compare(Card firstCard, Card secondCard) {
        return Integer.compare(firstCard.getRank(), secondCard.getRank());
    }
}
