package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.board.CloudUnavailableException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;

/**
 * Command class to request the collection of students from a <code>Cloud</code>.
 */
public class CollectCloudCommand implements Command {

    private final int cloudIndex;
    private final GameController gameController;

    public CollectCloudCommand(int cloudIndex, GameController gameController) {
        this.cloudIndex = cloudIndex;
        this.gameController = gameController;
    }
    @Override
    public void execute() throws IllegalActionException, CloudUnavailableException {
        gameController.getRoundStateController().collectFromCloud(cloudIndex);
    }
}
