package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class ExpertModeDisabledException extends EriantysRuntimeException {
    public ExpertModeDisabledException() {
        super("Expert mode is NOT active!");
    }
}
