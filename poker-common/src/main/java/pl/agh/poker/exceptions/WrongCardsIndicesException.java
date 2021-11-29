package pl.agh.poker.exceptions;

public class WrongCardsIndicesException extends Exception{
    public WrongCardsIndicesException(){};

    public WrongCardsIndicesException(String message){
        super(message);
    }
}
