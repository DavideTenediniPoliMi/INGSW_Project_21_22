package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.TurnState;

public class CloudStateController extends RoundStateController {
    public CloudStateController(RoundStateController oldState) {
        super(oldState, TurnState.CLOUD);
    }

    @Override
    public void definePlayOrder() {
        // TODO define play order before the planning phase (clock-wise starting from the lowest card played weight)
    }

    @Override
    public void collectFromClouds(int cloudIndex) {
        // TODO
    }
}
