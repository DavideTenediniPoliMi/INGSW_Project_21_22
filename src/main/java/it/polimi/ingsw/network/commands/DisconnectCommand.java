package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class DisconnectCommand implements Command {

    private final int playerID;
    private final GameController gameController;

    public DisconnectCommand(int playerID, GameController gameController) {
        this.playerID = playerID;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        gameController.handleDisconnection(playerID);
    }
}
