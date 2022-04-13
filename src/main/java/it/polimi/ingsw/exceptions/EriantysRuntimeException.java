package it.polimi.ingsw.exceptions;

/**
 * Abstract class for unchecked exceptions
 */
public abstract class EriantysRuntimeException extends RuntimeException{

    public EriantysRuntimeException(String message){
        super(message);
    }
}
