package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to buy a <code>CharacterCard</code> while another <code>CharacterCard</code> is already
 * active
 */
public class CharacterCardActivationException extends EriantysException {
    public CharacterCardActivationException(String requestedCard, String activeCard) {
        super("Unable to activate " + requestedCard + " card, " + activeCard + " is already active!");
    }
}
