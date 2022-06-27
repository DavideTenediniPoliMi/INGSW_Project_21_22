package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

/**
 * Exception thrown when an Expert Mode action is called but the game is not in Expert Mode.
 */
public class ExpertModeDisabledException extends EriantysRuntimeException {
    public ExpertModeDisabledException() {
        super("Expert mode is NOT active!");
    }
}
