package pl.agh.poker.player;

import jdk.nashorn.internal.runtime.Scope;
import pl.agh.poker.elements.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketPlayer extends Player{

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    public SocketPlayer(Socket client) throws IOException {
        super();
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        super.setName(getNick());
    }

    private String getNick() throws IOException {
        out.println("podaj nazwe uzytkownika");
        return in.readLine();
    }

    public void showCards(){
        out.println("Twoje karty: ");
        for (Card card: getCards()){
            out.println(card);
        }
    }
}
