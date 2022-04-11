package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

public class NotCurrentPlayerException extends EriantysException {
    public NotCurrentPlayerException(int playerID) {
        super("It's not player " + playerID + "'s turn");
    }
}
