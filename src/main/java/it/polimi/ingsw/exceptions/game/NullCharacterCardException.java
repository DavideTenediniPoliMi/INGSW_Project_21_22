package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class NullCharacterCardException extends EriantysRuntimeException {
    public NullCharacterCardException() {
        super("Active Character card is null");
    }
}
