package it.polimi.ingsw.exceptions.player;

import it.polimi.ingsw.exceptions.EriantysException;

public class NotEnoughCoinsException extends EriantysException {
    public NotEnoughCoinsException(int amt, int req) {
        super("Not enough coins (Player has " + amt + ", required " + req);
    }
}
