package pl.agh.poker.exceptions;

public class WrongCardsIndicesException extends Exception{

    public WrongCardsIndicesException(int index){
        super();
        String message = index + " is out of bounds (0, 4)";
        System.out.println(message);
    }
}
