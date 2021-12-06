package pl.agh.poker.exceptions;

import pl.agh.poker.constants.Constants;

public class WrongNumberOfPlayers extends Exception{
    public WrongNumberOfPlayers(int numOfPlayers){
        super();
        String message = numOfPlayers + " is out of bounds (" + Constants.MIN_NUMBER_OF_PLAYERS + ", " + Constants.MAX_NUMBER_OF_PLAYERS + ")";
        System.out.println(message);
    }
}
