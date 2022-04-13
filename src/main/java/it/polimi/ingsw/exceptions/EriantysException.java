package it.polimi.ingsw.exceptions;

/**
 * Abstract class for checked exceptions
 */
public abstract class EriantysException extends Exception{

    public EriantysException(String message){
        super(message);
    }
}
