package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.students.StudentTransferException;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * Command class to request a student transfer to an <code>Island</code>.
 */
public class TransferToIslandCommand implements Command {
    private final int islandIndex;
    private final Color color;
    private final GameController gameController;

    public TransferToIslandCommand(int islandIndex, Color color, GameController gameController) {
        this.islandIndex = islandIndex;
        this.gameController = gameController;
        this.color = color;
    }

    @Override
    public void execute() throws StudentTransferException, IllegalActionException {
        gameController.getRoundStateController().transferStudentToIsland(islandIndex, color);
    }
}
