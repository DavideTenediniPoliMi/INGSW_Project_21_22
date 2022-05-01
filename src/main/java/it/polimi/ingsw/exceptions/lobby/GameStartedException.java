package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when sending Lobby-related commands while the game is in progress.
 */
public class GameStartedException extends EriantysException {
    public GameStartedException() {
        super("Game has already started!");
    }
}
