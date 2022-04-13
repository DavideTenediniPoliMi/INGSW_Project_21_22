package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to perform actions as non-active <code>Player</code>.
 */
public class NotCurrentPlayerException extends EriantysException {
    public NotCurrentPlayerException(int playerID) {
        super("It's not player " + playerID + "'s turn");
    }
}
