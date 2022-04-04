package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.enumerations.TurnState;

public class PlanningStateController extends RoundStateController {
    public PlanningStateController(RoundStateController oldState) {
        super(oldState, TurnState.PLANNING);
    }

    @Override
    public void definePlayOrder() {
        // TODO define play order before the action phase (increasing card played weight)
    }

    @Override
    public void playCard(int cardIndex) {
        // TODO
    }
}
