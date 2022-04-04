package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.TurnState;

public class MNStateController extends CharacterCardPlayableStateController {
    public MNStateController(RoundStateController oldState) {
        super(oldState, TurnState.MOTHER_NATURE);
    }

    @Override
    public void moveMN(int steps) {
        // TODO
    }
}
