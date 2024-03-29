package it.polimi.ingsw.exceptions.player;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.Card;

/**
 * Exception thrown when the Assistant card requested was already used during a turn.
 */
public class CardUsedException extends EriantysException {
    public CardUsedException(Card card) {
        super("Card " + card + " already used this turn");
    }
}
