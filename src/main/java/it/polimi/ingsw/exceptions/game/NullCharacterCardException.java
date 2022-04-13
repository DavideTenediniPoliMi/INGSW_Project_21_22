package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Exception thrown when trying to use a Character card, but none is active.
 */
public class NullCharacterCardException extends EriantysRuntimeException {
    public NullCharacterCardException() {
        super("Active Character card is null");
    }
}
