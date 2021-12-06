package pl.agh.poker.exceptions;

public class NotEnoughCardsInDeckException extends Exception{
    public NotEnoughCardsInDeckException(){
        super();
        System.out.println("Not enough cards in the deck");
    }
}
