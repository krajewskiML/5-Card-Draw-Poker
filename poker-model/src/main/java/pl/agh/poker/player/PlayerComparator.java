package pl.agh.poker.player;

import pl.agh.poker.ranker.HandRanking;
import pl.agh.poker.ranker.HandRankingComparator;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player firstPlayer, Player secondPlayer) {
        HandRanking firstPlayerHand = firstPlayer.getHandRanking();
        HandRanking secondPlayerHand = secondPlayer.getHandRanking();
        HandRankingComparator handRankingComparator = new HandRankingComparator();
        return handRankingComparator.compare(firstPlayerHand, secondPlayerHand);
    }
}
