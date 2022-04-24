package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to join a full <code>Lobby</code>.
 */
public class GameFullException extends EriantysException {
    public GameFullException() {
        super("Game is full!");
    }
}
