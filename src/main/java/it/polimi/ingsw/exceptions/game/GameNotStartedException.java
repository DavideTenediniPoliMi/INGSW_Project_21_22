package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when a Game action is requested while in Lobby.
 */
public class GameNotStartedException extends EriantysException {
    public GameNotStartedException() {
        super("Game has not started yet.");
    }
}
