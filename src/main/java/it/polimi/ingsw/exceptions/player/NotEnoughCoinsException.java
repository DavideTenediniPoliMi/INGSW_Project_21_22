package it.polimi.ingsw.exceptions.player;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class NotEnoughCoinsException extends EriantysRuntimeException {
    public NotEnoughCoinsException(int amt, int req) {
        super("Not enough coins (Player has " + amt + ", required " + req);
    }
}
