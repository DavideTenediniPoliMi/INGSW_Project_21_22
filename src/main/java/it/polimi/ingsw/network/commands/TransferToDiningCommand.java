package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.students.StudentTransferException;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * Command class to request a student transfer to the dining room.
 */
public class TransferToDiningCommand implements Command {

    private final Color color;
    private final GameController gameController;

    public TransferToDiningCommand(Color color, GameController gameController) {
        this.color = color;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws StudentTransferException, IllegalActionException {
        gameController.getRoundStateController().transferStudentToDiningRoom(color);
    }
}
