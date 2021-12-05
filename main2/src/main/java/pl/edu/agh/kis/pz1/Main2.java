package pl.edu.agh.kis.pz1;


import pl.agh.poker.game.Table;


import java.io.IOException;
import java.util.*;

/**
 * Przykładowy kod do zajęć laboratoryjnych 2, 3, 4 z przedmiotu: Programowanie zaawansowane 1
 *
 * @author Paweł Skrzyński
 */
public class Main2 {
    public static void main(String[] args) throws IOException {
        Scanner myInput = new Scanner(System.in);
        while (true) {
            System.out.println("Type in number of players: ");
            int howManyPlayers = myInput.nextInt();
            Table stol = new Table(howManyPlayers);
            stol.performGame();
            System.out.println("Do you want to host another game (yes/no): ");
            myInput.nextLine();
            String decision = myInput.nextLine();
            if (Objects.equals(decision, "no")) {
                break;
            }
        }
    }
}
