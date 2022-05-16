package it.polimi.ingsw.exceptions;

/**
 * Abstract class for checked exceptions
 */
public abstract class EriantysException extends Exception{
    private final String message;
    public EriantysException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
