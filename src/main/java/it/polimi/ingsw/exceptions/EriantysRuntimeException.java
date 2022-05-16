package it.polimi.ingsw.exceptions;

/**
 * Abstract class for unchecked exceptions
 */
public abstract class EriantysRuntimeException extends RuntimeException{
    private final String message;

    public EriantysRuntimeException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
