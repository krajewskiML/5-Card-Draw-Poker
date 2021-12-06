package pl.agh.poker.exceptions;

public class TooMuchCardsTooDiscard extends Exception{
    public TooMuchCardsTooDiscard(int amount){
        super();
        String message = "You want to discard improper amount of cards: " + amount;
        System.out.println(message);
    }

}
