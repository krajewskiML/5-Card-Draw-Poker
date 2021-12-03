package pl.agh.poker.player;

import jdk.nashorn.internal.runtime.Scope;
import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.Deck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketPlayer extends Player {

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    public SocketPlayer(Socket client) throws IOException {
        super();
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        setName(getNick());
        System.out.println("nowy gracz na serwerze: " + getName());
    }

    private String getNick() throws IOException {
        return sendMessageGetResponse("Wpisz swoj nick: ");
    }

    public void showCards() {
        StringBuilder outString = new StringBuilder();
        outString.append("Twoje karty to: ");
        for (Card card : getCards()) {
            outString.append(card.toString()).append(", ");
        }
        sendMessageWithoutResponse(outString.toString());
    }

    public void swapCards(Deck deck) throws IOException {
        int numberOfCardsToSwap = Integer.parseInt(sendMessageGetResponse("Ile kart chcesz wymienic: "));
        ArrayList<Integer> cardsToReturn = new ArrayList<>();
        for (int idx = 0; idx < numberOfCardsToSwap; ++idx) {
            cardsToReturn.add(
                    Integer.parseInt(sendMessageGetResponse("Wpisz indeks karty którą chcesz wymienic: "))
            );
        }
        returnCards(cardsToReturn);
        fetchCards(deck.dealCards(numberOfCardsToSwap));
        showCards();
    }

    public float placeBet(float currentBet) throws IOException {
        ArrayList<String> possibleChoices = new ArrayList<>();

        if(currentBet > currentBalanceInPool + balance){
            // if less money than max bet then fold or all in
            possibleChoices.add("fold");
            possibleChoices.add("all in");
        }else{
            if (currentBet == 0) {
                possibleChoices.add("check");
            }
            possibleChoices.add("fold");
            possibleChoices.add("all in");
            if (currentBalanceInPool + balance >= currentBet) {
                possibleChoices.add("call");
                if (currentBalanceInPool + balance > currentBet) {
                    possibleChoices.add("raise");
                }
            }
        }
        StringBuilder bettingMessage = new StringBuilder();
        bettingMessage.append("Your possible choices this round are: ");
        for (String possibility : possibleChoices) {
            bettingMessage.append(possibility).append(", ");
        }
        bettingMessage.append("pick one by typing in its name: ");
        String answer = sendMessageGetResponse(bettingMessage.toString());
        float amount = 0;
        switch (answer) {
            case "fold":
                fold();
            case "check":
                break;
            case "all in":
                amount = balance;
                setAllIn(true);
                break;
            case "call":
                amount = currentBet - currentBalanceInPool;
                break;
            case "raise":
                do {
                    amount = Float.parseFloat(sendMessageGetResponse("Type in how much you want to add to bet, min(" + String.valueOf(currentBet - currentBalanceInPool) + "): "));
                } while (amount <= currentBet - currentBalanceInPool);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + answer);
        }
        return bet(amount);
    }

    private String sendMessageGetResponse(String message) throws IOException {
        out.println(1);
        out.println(message);
        return in.readLine();
    }

    public void sendMessageWithoutResponse(String message) {
        out.println(0);
        out.println(message);
    }

    public void showBalance() {
        sendMessageWithoutResponse(String.valueOf(getBalance()));
    }
}
