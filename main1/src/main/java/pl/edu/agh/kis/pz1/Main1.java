package pl.edu.agh.kis.pz1;

import pl.agh.poker.constants.Constants;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


/**
 * Przykładowy kod do zajęć laboratoryjnych 2, 3, 4 z przedmiotu: Programowanie zaawansowane 1
 * @author Paweł Skrzyński
 */
public class Main1 {
    public static void main( String[] args ) throws IOException {
        Socket client = null;
        try{
            client = new Socket("localhost", Constants.PORT);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String fromServerType;
            String fromServerMessage;
            String fromUser;
            Scanner myInput = new Scanner(System.in);

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
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            client.close();
        }

    }
}
