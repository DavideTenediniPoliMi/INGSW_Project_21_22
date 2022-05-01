package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.board.MNOutOfRangeException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;

/**
 * Command class to request the movement of MN.
 */
public class MoveMNCommand implements Command {
    private final int destIndex;
    private final GameController gameController;

    public MoveMNCommand(int destIndex, GameController gameController) {
        this.destIndex = destIndex;
        this.gameController = gameController;
    }
    @Override
    public void execute() throws IllegalActionException, MNOutOfRangeException {
        gameController.getRoundStateController().moveMN(destIndex);
        gameController.setMovedMN();
    }
}
