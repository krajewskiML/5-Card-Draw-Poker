package pl.edu.agh.kis.pz1;

import pl.agh.poker.elements.Deck;
import pl.agh.poker.game.Table;
import pl.agh.poker.ranker.HandRanking;
import pl.agh.poker.ranker.HandRankingComparator;
import pl.edu.agh.kis.pz1.util.TextUtils;

import pl.agh.poker.elements.Card;

import pl.agh.poker.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Przykładowy kod do zajęć laboratoryjnych 2, 3, 4 z przedmiotu: Programowanie zaawansowane 1
 *
 * @author Paweł Skrzyński
 */
public class Main2 {
    public static void main(String[] args) throws IOException {
        Scanner myInput = new Scanner( System.in );
        System.out.println("Podaj liczbe graczy: ");
        int howManyPlayers = myInput.nextInt();
        Table stol = new Table(howManyPlayers);
        stol.performRound();
    }
}
