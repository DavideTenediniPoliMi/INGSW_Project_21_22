package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class NullPlayerException extends EriantysRuntimeException {
    public NullPlayerException() {
        super("Bad player ID (found -1)");
    }
}
