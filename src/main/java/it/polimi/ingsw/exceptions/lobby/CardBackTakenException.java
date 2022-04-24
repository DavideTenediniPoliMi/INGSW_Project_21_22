package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.CardBack;

/**
 * Exception thrown when selecting an already selected <code>CardBack</code>.
 */
public class CardBackTakenException extends EriantysException {
    public CardBackTakenException(CardBack cardBack) {
        super("The Card Back: " + cardBack + " is already selected by another Player");
    }
}
