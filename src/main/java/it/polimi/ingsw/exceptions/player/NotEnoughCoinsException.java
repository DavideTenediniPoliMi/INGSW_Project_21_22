package it.polimi.ingsw.exceptions.player;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when a <code>Player</code> doesn't have enough coins to buy a Character card.
 */
public class NotEnoughCoinsException extends EriantysException {
    public NotEnoughCoinsException(int amt, int req) {
        super("Not enough coins (Player has " + amt + ", required " + req + ")");
    }
}
