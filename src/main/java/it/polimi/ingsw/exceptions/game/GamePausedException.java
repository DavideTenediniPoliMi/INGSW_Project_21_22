package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when an action is requested but the Game in in pause.
 */
public class GamePausedException extends EriantysException {
    public GamePausedException() {
        super("Game is currently paused as only 1 player is connected");
    }
}
