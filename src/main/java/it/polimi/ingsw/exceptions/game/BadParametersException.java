package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Exception thrown when giving bad parameters to a method in a controller.
 */
public class BadParametersException extends EriantysRuntimeException {
    public BadParametersException(String param) {
        super("Bad param: " + param);
    }
}
