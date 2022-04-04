package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;

public class CharacterCardPlayableStateController extends RoundStateController {
    public CharacterCardPlayableStateController(RoundStateController oldState, TurnState stateType) {
        super(oldState, stateType);
    }

    @Override
    public void buyCharacterCard(int cardIndex) {
        // TODO
    }

    @Override
    public void setCardParameters(Parameters params) {
        // TODO
    }

    @Override
    public void activateCard() {
        // TODO
    }
}
