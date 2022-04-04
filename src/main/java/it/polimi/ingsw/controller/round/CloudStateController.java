package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.TurnState;

public class CloudStateController extends RoundStateController {
    public CloudStateController(RoundStateController oldState) {
        super(oldState, TurnState.CLOUD);
    }

    @Override
    public void collectFromClouds(int cloudIndex) {
        // TODO
    }
}
