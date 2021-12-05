package pl.agh.poker.ranker;

import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.CardComparator;

import pl.agh.poker.constants.Constants;

import java.util.*;

public class HandRanking {
    List<Integer> significantValues;
    int strengthOfHand;

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

    public int getStrengthOfHand() {
        return strengthOfHand;
    }

    public List<Integer> getSignificantValues() {
        return significantValues;
    }

    private void calculateHand(List<Card> cards) {
        if (checkStraightFlush(cards) != null) {
            setSignificantValues(checkStraightFlush(cards));
            setStrengthOfHand(9);
            return;
        }
        if (check4OfKind(cards) != null) {
            setSignificantValues(check4OfKind(cards));
            setStrengthOfHand(8);
            return;
        }
        if (checkFullHouse(cards) != null) {
            setSignificantValues(checkFullHouse(cards));
            setStrengthOfHand(7);
            return;
        }
        if (checkFlush(cards) != null) {
            setSignificantValues(checkFlush(cards));
            setStrengthOfHand(6);
            return;
        }
        if (checkStraight(cards) != null) {
            setSignificantValues(checkStraight(cards));
            setStrengthOfHand(5);
            return;
        }
        if (checkStraight(cards) != null) {
            setSignificantValues(checkStraight(cards));
            setStrengthOfHand(4);
            return;
        }
        if (check3OfKind(cards) != null) {
            setSignificantValues(check3OfKind(cards));
            setStrengthOfHand(3);
            return;
        }
        if (checkTwoPairs(cards) != null) {
            setSignificantValues(checkTwoPairs(cards));
            setStrengthOfHand(2);
            return;
        }
        if (checkPair(cards) != null) {
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
        int maxPairValue = -1;
        for (int idx = 0; idx < Constants.CARDS_IN_HAND; ++idx) {
            if (Collections.frequency(ranks, ranks.get(idx)) == 2) {
                if (maxPairValue < ranks.get(idx)) {
                    maxPairValue = ranks.get(idx);
                }
            }
        }
        if (maxPairValue > -1) {
            ranks.remove(Integer.valueOf(maxPairValue));
            ranks.remove(Integer.valueOf(maxPairValue));
            return Arrays.asList(maxPairValue,
                    ranks.get(0),
                    ranks.get(1),
                    ranks.get(2)
            );
        } else {
            return null;
        }
    }

    private List<Integer> checkTwoPairs(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        int pairValFirst = -1, pairValSecond = -1;
        for (int idx = 0; idx < ranks.size(); ++idx) {
            if (Collections.frequency(ranks, ranks.get(idx)) == 2) {
                if (pairValFirst == -1) {
                    pairValFirst = ranks.get(idx);
                    ranks.remove(Integer.valueOf(pairValFirst));
                    ranks.remove(Integer.valueOf(pairValFirst));
                } else {
                    pairValSecond = ranks.get(idx);
                    ranks.remove(Integer.valueOf(pairValSecond));
                    ranks.remove(Integer.valueOf(pairValSecond));
                    break;
                }
                idx = -1;
            }
        }
        if (pairValSecond != -1 && pairValFirst != -1) {
            return Arrays.asList(
                    Math.max(pairValSecond, pairValFirst),
                    Math.min(pairValSecond, pairValFirst),
                    ranks.get(0)
            );
        } else {
            return null;
        }
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
            return null;
        }
    }

    private List<Integer> checkStraight(List<Card> cards) {
        List<Integer> ranks = getRanksOfCards(cards);
        boolean isStraight = true;
        //standard straight
        for (int idx = 1; idx < ranks.size(); ++idx) {
            if (ranks.get(idx) != ranks.get(idx - 1) - 1) {
                isStraight = false;
                break;
            }
        }
        if (isStraight) {
            return Arrays.asList(ranks.get(4));
        } else {
            //check for straight starting from an ace
            if (ranks.get(0) == 12 && ranks.get(1) == 3) {
                isStraight = true;
                for (int idx = 2; idx < ranks.size(); ++idx) {
                    if (ranks.get(idx) != ranks.get(idx - 1) - 1) {
                        isStraight = false;
                        break;
                    }
                }
                if (isStraight) {
                    return Arrays.asList(-1);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private List<Integer> checkFlush(List<Card> cards) {
        List<Integer> suits = getSuitesOfCards(cards);
        for (int idx = 1; idx < suits.size(); ++idx) {
            if (!Objects.equals(suits.get(idx), suits.get(0))) {
                return null;
            }
        }
        return getRanksOfCards(cards);
    }

    private List<Integer> checkFullHouse(List<Card> cards) {
        List<Integer> tripleValues = check3OfKind(cards);
        if (tripleValues == null) {
            return null;
        }
        List<Integer> ranks = getRanksOfCards(cards);
        ranks.remove(tripleValues.get(0));
        ranks.remove(tripleValues.get(0));
        ranks.remove(tripleValues.get(0));
        if (Objects.equals(ranks.get(0), ranks.get(1))) {
            return Arrays.asList(tripleValues.get(0), ranks.get(0));
        } else {
            return null;
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
        return null;
    }

    private List<Integer> checkStraightFlush(List<Card> cards) {
        List<Integer> straight = checkStraight(cards);
        List<Integer> flush = checkFlush(cards);
        if (straight != null && flush != null) {
            return straight;
        }
        return null;
    }

}
