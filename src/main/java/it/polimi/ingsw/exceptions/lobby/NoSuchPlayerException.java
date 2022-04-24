package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when tyring to perform an action with an unknown <code>Player</code>'s ID.
 */
public class NoSuchPlayerException extends EriantysException {
    public NoSuchPlayerException(int ID) {
        super("No Player with ID: " + ID + " was found.");
    }
}
