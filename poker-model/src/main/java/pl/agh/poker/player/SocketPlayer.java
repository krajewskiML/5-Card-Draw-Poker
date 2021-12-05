package pl.agh.poker.player;

import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.Deck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketPlayer extends Player {

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    public SocketPlayer(Socket client) throws IOException {
        super();
        socket = client;
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        setName(getNick());
        System.out.println("New player joined: " + getName());
    }

    private String getNick() throws IOException {
        return sendMessageGetResponse("Type in your nick: ");
    }

    public void showCards() {
        StringBuilder outString = new StringBuilder();
        int cardIdx = 0;
        outString.append("Your cards are: ");
        for (Card card : getCards()) {
            outString.append(String.valueOf(cardIdx)).append(" ").append(card.toString()).append(", ");
        }
        sendMessageWithoutResponse(outString.toString());
    }

    public void swapCards(Deck deck) throws IOException {
        int numberOfCardsToSwap = Integer.parseInt(sendMessageGetResponse("How many cards you want to replace: "));
        ArrayList<Integer> cardsToReturn = new ArrayList<>();
        for (int idx = 0; idx < numberOfCardsToSwap; ++idx) {
            cardsToReturn.add(
                    Integer.parseInt(sendMessageGetResponse("Type in card index that you want to replace: "))
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
            possibleChoices.add("allin");
        }else{
            if (currentBet == 0) {
                possibleChoices.add("check");
            }
            possibleChoices.add("fold");
            possibleChoices.add("allin");
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
                break;
            case "check":
                break;
            case "allin":
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

    public String sendMessageGetResponse(String message) throws IOException {
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
