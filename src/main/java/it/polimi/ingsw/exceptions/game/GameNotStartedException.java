package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

public class GameNotStartedException extends EriantysException {
    public GameNotStartedException() {
        super("Game has not started yet.");
    }
}
