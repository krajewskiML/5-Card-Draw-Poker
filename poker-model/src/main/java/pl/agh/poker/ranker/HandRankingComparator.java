package pl.agh.poker.ranker;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HandRankingComparator implements Comparator<HandRanking> {
    @Override
    public int compare(HandRanking firstHand, HandRanking secondHand) {
        if (firstHand.getStrengthOfHand() != secondHand.getStrengthOfHand()) {
            return Integer.compare(firstHand.getStrengthOfHand(), secondHand.getStrengthOfHand());
        } else {
            List<Integer> firstHandSignificantValues = firstHand.getSignificantValues();
            List<Integer> secondHandSignificantValues = secondHand.getSignificantValues();
            for (int idx = 0; idx < firstHandSignificantValues.size(); ++idx) {
                if (!Objects.equals(firstHandSignificantValues.get(idx), secondHandSignificantValues.get(idx))) {
                    return Integer.compare(
                            firstHandSignificantValues.get(idx),
                            secondHandSignificantValues.get(idx)
                    );
                }
            }
            return Integer.compare(
                    firstHandSignificantValues.get(firstHandSignificantValues.size() - 1),
                    secondHandSignificantValues.get(secondHandSignificantValues.size() - 1)
            );
        }
    }
}
