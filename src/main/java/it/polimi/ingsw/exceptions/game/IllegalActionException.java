package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.TurnState;

/**
 * Exception thrown when requesting an invalid action for the current <code>TurnState</code>
 */
public class IllegalActionException extends EriantysException {
    public IllegalActionException(String action, TurnState currentState) {
        super("Illegal action: " + action + " during " + currentState + " state.");
    }
}
