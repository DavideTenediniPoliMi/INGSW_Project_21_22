package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.RoundStateController;

public class GameController {
    private RoundStateController roundStateController;

    public GameController() {
    }

    private void nextState() {
        // TODO state machine implementation
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
