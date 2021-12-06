package pl.agh.poker.player;

import pl.agh.poker.constants.Constants;
import pl.agh.poker.elements.Card;
import pl.agh.poker.elements.Deck;
import pl.agh.poker.exceptions.NotEnoughCardsInDeckException;
import pl.agh.poker.exceptions.TooMuchCardsTooDiscard;
import pl.agh.poker.exceptions.WrongCardsIndicesException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketPlayer extends Player {

    static final String ALLIN = "allin";
    static final String FOLD = "fold";
    static final String CHECK = "check";
    static final String CALL = "call";
    static final String RAISE = "raise";

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
            outString.append(cardIdx).append(" ").append(card.toString()).append(", ");
            ++cardIdx;
        }
        sendMessageWithoutResponse(outString.toString());
    }

    public void swapCards(Deck deck) throws IOException, WrongCardsIndicesException, NotEnoughCardsInDeckException, TooMuchCardsTooDiscard {
        int numberOfCardsToSwap = Integer.parseInt(sendMessageGetResponse("How many cards you want to replace: "));
        if(numberOfCardsToSwap > Constants.CARDS_IN_HAND){
            throw new TooMuchCardsTooDiscard(numberOfCardsToSwap);
        }
        ArrayList<Integer> cardsToReturn = new ArrayList<>();
        for (int idx = 0; idx < numberOfCardsToSwap; ++idx) {
            int indexOfCard = Integer.parseInt(sendMessageGetResponse("Type in card index that you want to replace: "));
            if (indexOfCard < 0 || indexOfCard >= Constants.CARDS_IN_HAND){
                throw new WrongCardsIndicesException(indexOfCard);
            }
            cardsToReturn.add(indexOfCard);
        }
        returnCards(cardsToReturn);
        fetchCards(deck.dealCards(numberOfCardsToSwap));
        showCards();
    }

    public float placeBet(float currentBet) throws IOException {
        ArrayList<String> possibleChoices = new ArrayList<>();

        if(currentBet > currentBalanceInPool + balance){
            // if less money than max bet then fold or all in
            possibleChoices.add(FOLD);
            possibleChoices.add(ALLIN);
        }else{
            if (currentBet == 0) {
                possibleChoices.add(CHECK);
            }
            possibleChoices.add(FOLD);
            possibleChoices.add(ALLIN);
            if (currentBalanceInPool + balance >= currentBet) {
                possibleChoices.add(CALL);
                if (currentBalanceInPool + balance > currentBet) {
                    possibleChoices.add(RAISE);
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
            case FOLD:
                fold();
                break;
            case CHECK:
                break;
            case ALLIN:
                amount = balance;
                setAllIn(true);
                break;
            case CALL:
                amount = currentBet - currentBalanceInPool;
                break;
            case RAISE:
                do {
                    amount = Float.parseFloat(sendMessageGetResponse("Type in how much you want to add to bet, min(" + (currentBet - currentBalanceInPool) + "): "));
                } while (amount <= currentBet - currentBalanceInPool);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + answer);
        }
        setHasParticipatedInRound(true);
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
