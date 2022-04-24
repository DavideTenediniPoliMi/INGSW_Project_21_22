package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to select the same nickname as another <code>Player</code>.
 */
public class NameTakenException extends EriantysException {
    public NameTakenException(String message) {
        super("Name: " + message + " is already taken!");
    }
}
