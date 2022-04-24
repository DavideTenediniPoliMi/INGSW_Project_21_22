package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to join a <code>Lobby</code> with the same ID as another <code>Player</code>.
 */
public class DuplicateIDException extends EriantysException {
    public DuplicateIDException(int ID) {
        super("There is already another player with ID: " + ID);
    }
}
