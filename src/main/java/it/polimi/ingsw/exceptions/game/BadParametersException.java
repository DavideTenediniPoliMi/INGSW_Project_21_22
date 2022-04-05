package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class BadParametersException extends EriantysRuntimeException {
    public BadParametersException(String param) {
        super("Bad param: " + param);
    }
}
