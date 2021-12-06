package pl.agh.poker.ranker;

import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.CardComparator;

import pl.agh.poker.constants.Constants;

import java.util.*;

/**
 * Class that rates player hand
 */
public class HandRanking {
    List<Integer> significantValues;
    int strengthOfHand;

    /**
     * Rates player hand by checking all possible combinations top to bottom
     * @param cards cards owned by player
     */
    public HandRanking(List<Card> cards) {
        CardComparator cardComparator = new CardComparator();
        cards.sort(cardComparator);
        Collections.reverse(cards);
        calculateHand(cards);
    }

    private void setSignificantValues(List<Integer> significantValues) {
        this.significantValues = significantValues;
    }

    private void setStrengthOfHand(int strengthOfHand) {
        this.strengthOfHand = strengthOfHand;
    }

    /**
     *Returns strength of hand ranking descriibed by integer
     * @return integer indicating strength of hand (the bigger the number the better hand ranking)
     */
    public int getStrengthOfHand() {
        return strengthOfHand;
    }

    /**
     * Returns numbers characterizing giiven hand rankng
     * @return List of significant values
     */
    public List<Integer> getSignificantValues() {
        return significantValues;
    }

    private void calculateHand(List<Card> cards) {
        if (!checkStraightFlush(cards).isEmpty()) {
            setSignificantValues(checkStraightFlush(cards));
            setStrengthOfHand(8);
            return;
        }
        if (!check4OfKind(cards).isEmpty()) {
            setSignificantValues(check4OfKind(cards));
            setStrengthOfHand(7);
            return;
        }
        if (!checkFullHouse(cards).isEmpty()) {
            setSignificantValues(checkFullHouse(cards));
            setStrengthOfHand(6);
            return;
        }
        if (!checkFlush(cards).isEmpty()) {
            setSignificantValues(checkFlush(cards));
            setStrengthOfHand(5);
            return;
        }
        if (!checkStraight(cards).isEmpty()) {
            setSignificantValues(checkStraight(cards));
            setStrengthOfHand(4);
            return;
        }
        if (!check3OfKind(cards).isEmpty()) {
            setSignificantValues(check3OfKind(cards));
            setStrengthOfHand(3);
            return;
        }
        if (!checkTwoPairs(cards).isEmpty()) {
            setSignificantValues(checkTwoPairs(cards));
            setStrengthOfHand(2);
            return;
        }
        if (!checkPair(cards).isEmpty()) {
            setSignificantValues(checkPair(cards));
            setStrengthOfHand(1);
            return;
        }
        setSignificantValues(checkHighCard(cards));
        setStrengthOfHand(0);
    }

    private List<Integer> getRanksOfCards(List<Card> cards) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int idx = 0; idx < Constants.CARDS_IN_HAND; ++idx) {
            values.add(cards.get(idx).getRank());
        }
        return values;
    }

    private List<Integer> getSuitesOfCards(List<Card> cards) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int idx = 0; idx < Constants.CARDS_IN_HAND; ++idx) {
            values.add(cards.get(idx).getSuite());
        }
        return values;
    }

    private List<Integer> checkHighCard(List<Card> cards) {
        return getRanksOfCards(cards);
    }

    private List<Integer> checkPair(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        int pair;
        for (int idx = 0; idx < ranks.size(); ++idx) {
            if (Collections.frequency(ranks, ranks.get(idx)) == 2) {
                pair = ranks.get(idx);
                ranks.remove(Integer.valueOf(pair));
                ranks.remove(Integer.valueOf(pair));
                return Arrays.asList(pair, ranks.get(0), ranks.get(1), ranks.get(2));
            }
        }
        return Collections.emptyList();
    }

    private List<Integer> checkTwoPairs(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        int pairValFirst;
        int pairValSecond;
        List<Integer> pair = checkPair(cards);
        if (pair.isEmpty()) {
            return Collections.emptyList();
        }
        pairValFirst = pair.get(0);
        ranks.remove(pair.get(0));
        ranks.remove(pair.get(0));
        if (Objects.equals(ranks.get(0), ranks.get(1))) {
            pairValSecond = ranks.get(0);
            if (pairValFirst < pairValSecond) {
                return Arrays.asList(pairValSecond, pairValFirst, ranks.get(2));
            } else {
                return Arrays.asList(pairValFirst, pairValSecond, ranks.get(2));
            }
        }
        if (Objects.equals(ranks.get(0), ranks.get(2))) {
            pairValSecond = ranks.get(0);
            if (pairValFirst < pairValSecond) {
                return Arrays.asList(pairValSecond, pairValFirst, ranks.get(1));
            } else {
                return Arrays.asList(pairValFirst, pairValSecond, ranks.get(1));
            }
        }
        if (Objects.equals(ranks.get(1), ranks.get(2))) {
            pairValSecond = ranks.get(1);
            if (pairValFirst < pairValSecond) {
                return Arrays.asList(pairValSecond, pairValFirst, ranks.get(0));
            } else {
                return Arrays.asList(pairValFirst, pairValSecond, ranks.get(0));
            }
        }
        return Collections.emptyList();
    }

    private List<Integer> check3OfKind(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        int triple = -1;
        for (int idx = 0; idx < ranks.size(); ++idx) {
            if (Collections.frequency(ranks, ranks.get(idx)) == 3) {
                triple = ranks.get(idx);
                ranks.remove(Integer.valueOf(triple));
                ranks.remove(Integer.valueOf(triple));
                ranks.remove(Integer.valueOf(triple));
                break;
            }
        }
        if (triple != -1) {
            return Arrays.asList(
                    triple,
                    ranks.get(0),
                    ranks.get(1)
            );
        } else {
            return Collections.emptyList();
        }
    }

    private List<Integer> checkStraight(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        //check for straight starting with ace
        if (ranks.get(0) == 12 && ranks.get(1) == 3 && ranks.get(2) == 2 && ranks.get(3) == 1 && ranks.get(4) == 0) {
            return Collections.singletonList(3);
        }
        //check for normal straight
        for (int idx = 0; idx < ranks.size() - 1; ++idx) {
            if (ranks.get(idx) != ranks.get(idx + 1) + 1) {
                return Collections.emptyList();
            }
        }
        return Collections.singletonList(ranks.get(0));
    }

    private List<Integer> checkFlush(List<Card> cards) {
        List<Integer> suits = getSuitesOfCards(cards);
        for (int idx = 1; idx < suits.size(); ++idx) {
            if (!Objects.equals(suits.get(idx), suits.get(0))) {
                return Collections.emptyList();
            }
        }
        return getRanksOfCards(cards);
    }

    private List<Integer> checkFullHouse(List<Card> cards) {
        List<Integer> tripleValues = check3OfKind(cards);
        if (tripleValues.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> ranks = getRanksOfCards(cards);
        ranks.remove(tripleValues.get(0));
        ranks.remove(tripleValues.get(0));
        ranks.remove(tripleValues.get(0));
        if (Objects.equals(ranks.get(0), ranks.get(1))) {
            return Arrays.asList(tripleValues.get(0), ranks.get(0));
        } else {
            return Collections.emptyList();
        }
    }

    private List<Integer> check4OfKind(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        int quadValue;

        if (Collections.frequency(ranks, ranks.get(0)) == 4) {
            quadValue = ranks.get(0);
            ranks.remove(Integer.valueOf(quadValue));
            ranks.remove(Integer.valueOf(quadValue));
            ranks.remove(Integer.valueOf(quadValue));
            ranks.remove(Integer.valueOf(quadValue));
            return Arrays.asList(quadValue, ranks.get(0));
        } else {
            if (Collections.frequency(ranks, ranks.get(1)) == 4) {
                quadValue = ranks.get(1);
                ranks.remove(Integer.valueOf(quadValue));
                ranks.remove(Integer.valueOf(quadValue));
                ranks.remove(Integer.valueOf(quadValue));
                ranks.remove(Integer.valueOf(quadValue));
                return Arrays.asList(quadValue, ranks.get(0));
            }
        }
        return Collections.emptyList();
    }

    private List<Integer> checkStraightFlush(List<Card> cards) {
        List<Integer> straight = checkStraight(cards);
        List<Integer> flush = checkFlush(cards);
        if (!straight.isEmpty() && !flush.isEmpty()) {
            return straight;
        }
        return Collections.emptyList();
    }

    /**
     * String representation of hadn ranking
     * @return string representation of hand ranking
     */
    public String toString() {
        switch (getStrengthOfHand()){
            case 0:
                return "Strong Card";
            case 1:
                return "Pair";
            case 2:
                return "Two Pairs";
            case 3:
                return "Triple";
            case 4:
                return "Straight";
            case 5:
                return "Flush";
            case 6:
                return "Full House";
            case 7:
                return "Four of a kind";
            case 8:
                return "Straight Flush";
            default:
                return "";
        }
    }
}
