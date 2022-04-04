package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;

public class CharacterCardPlayableStateController extends RoundStateController {
    public CharacterCardPlayableStateController(RoundStateController oldState, TurnState stateType) {
        super(oldState, stateType);
    }

    @Override
    public void buyCharacterCard(int cardIndex) {
        if(cardIndex < 0 || cardIndex > 2) {
            //BAD PARAMETERS EXCEPTION
        }
        characterCardController.buyCharacterCard(getCurrentPlayerID(), cardIndex);
    }

    @Override
    public void setCardParameters(Parameters params) {
        characterCardController.setCardParameters(params);
    }

    @Override
    public int activateCard() {
        return characterCardController.activateCard();
    }
}
