package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.CardBack;

public class CardBackTakenException extends EriantysException {
    public CardBackTakenException(CardBack cardBack) {
        super("The Card Back: " + cardBack + " is already selected by another Player");
    }
}
