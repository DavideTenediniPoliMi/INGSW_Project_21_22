package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Exception thrown when there is no player with the specified ID.
 */
public class NullPlayerException extends EriantysRuntimeException {
    public NullPlayerException() {
        super("Bad player ID (found -1)");
    }
}
