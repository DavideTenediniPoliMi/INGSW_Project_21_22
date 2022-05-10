package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

public class GamePausedException extends EriantysException {
    public GamePausedException() {
        super("Game is currently paused as only 1 player is connected");
    }
}
