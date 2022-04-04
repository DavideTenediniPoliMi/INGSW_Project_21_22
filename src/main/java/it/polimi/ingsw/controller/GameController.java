package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.*;

public class GameController {
    private RoundStateController roundStateController;
    private int NUM_MOVABLE_STUDENTS; // TODO define behaviour

    public GameController() {
    }

    private void nextState() {
        switch(roundStateController.getStateType()) {
            case PLANNING:
                if(roundStateController.getNumPlayersStillToAct() == 0) {
                    setState(new StudentsStateController(roundStateController));
                    roundStateController.definePlayOrder();
                }
                break;
            case STUDENTS:
                if(roundStateController.getNumMovedStudents() == NUM_MOVABLE_STUDENTS) {
                    setState(new MNStateController(roundStateController));
                }
                break;
            case MOTHER_NATURE:
                checkWinnerAfterMN();
                setState(new CloudStateController(roundStateController));
                break;
            case CLOUD:
                checkWinnerAfterRound();
                setState(new PlanningStateController(roundStateController));
                break;
        }
    }

    private void setState(RoundStateController newState) {
        roundStateController = newState;
    }

    private void checkWinnerAfterMN() {
        // TODO
    }

    private void checkWinnerAfterRound() {
        // TODO
    }

    private void declareWinner(int playerID) {
        // TODO
    }
}
