package pl.edu.agh.kis.pz1;

import pl.agh.poker.constants.Constants;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.*;


/**
 * Przykładowy kod do zajęć laboratoryjnych 2, 3, 4 z przedmiotu: Programowanie zaawansowane 1
 * @author Paweł Skrzyński
 */
public class Main1 {
    public static void main( String[] args ) {
        Scanner myInput = new Scanner(System.in);
        Logger clientLogger = Logger.getLogger("clientLogger");
        clientLogger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        clientLogger.addHandler(handler);

        // implement s4jlf logger

        try (Socket client = new Socket("localhost", Constants.PORT)) {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String fromServerType;
            String fromServerMessage;
            String fromUser;

            while ((fromServerType = in.readLine()) != null) {
                fromServerMessage = in.readLine();
                System.out.println("Server: " + fromServerMessage);
                if (fromServerMessage.equals("Thanks for game!")) {
                    break;
                }
                if (fromServerType.equals("1")) {
                    fromUser = myInput.nextLine();
                    if (fromUser != null) {
                        out.println(fromUser.replaceAll("\\s+", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
