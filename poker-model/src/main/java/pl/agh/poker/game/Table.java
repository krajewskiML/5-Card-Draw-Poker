package pl.agh.poker.game;

import pl.agh.poker.elements.Deck;
import pl.agh.poker.player.Player;
import pl.agh.poker.player.PlayerComparator;
import pl.agh.poker.player.SocketPlayer;
import pl.agh.poker.ranker.HandRanking;

import pl.agh.poker.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Table {
    List<Player> players;
    int currentPool;
    Deck deck;
    ServerSocket server;

    public Table(int numberOfPlayers) throws IOException {

        server = new ServerSocket(Constants.PORT);
        setCurrentPool(0);
        setDeck(new Deck());

        players = new ArrayList<>();
        for (int idx = 0; idx < numberOfPlayers; ++idx) {
            Socket client = server.accept();
            System.out.println("Nowy klient na serwerze!");
            SocketPlayer player = new SocketPlayer(client);
            players.add(player);
            System.out.println(player.getName());
        }
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPool() {
        return currentPool;
    }

    public void setCurrentPool(int currentPool) {
        this.currentPool = currentPool;
    }

    public void addToPool(int amount) {
        setCurrentPool(getCurrentPool() + amount);
    }

    public void ante() {
        for (Player player : players) {
            if (player.getBalance() < Constants.ANTE) {
                player.fold();
            } else {
                addToPool(player.bet((int) Constants.ANTE));
            }
        }
    }

    public void deal() {
        for (Player player : players) {
            if (player.isInGame()) {
                player.fetchCards(deck.dealCards(Constants.CARDS_IN_HAND));
            }
        }
    }

    public void rateHands() {
        for (Player player : players) {
            if (player.isInGame()) {
                player.setHandRanking(new HandRanking(player.getCards()));
            }
        }
    }

    public List<Player> getBestPlayers() {
        PlayerComparator playerComparator = new PlayerComparator();
        players.sort(playerComparator);
        Collections.reverse(players);
        List<Player> bestPlayers = new ArrayList<>();
        bestPlayers.add(players.get(0));
        for (int idx = 1; idx < players.size(); idx++) {
            if (Objects.equals(bestPlayers.get(0).getHandRanking(), players.get(idx).getHandRanking())) {
                bestPlayers.add(players.get(idx));
            }
        }
        return bestPlayers;
    }

    public void performRound(){
        ante();
        deal();
        for (Player player: players)
            player.showCards();
    }
}
