package it.polimi.ingsw.exceptions.game;

import it.polimi.ingsw.exceptions.EriantysException;

public class CharacterCardActivationException extends EriantysException {
    public CharacterCardActivationException(String requestedCard, String activeCard) {
        super("Unable to activate " + requestedCard + " card, " + activeCard + " is already active!");
    }
}
