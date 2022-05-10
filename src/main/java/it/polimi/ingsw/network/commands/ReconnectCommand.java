package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class ReconnectCommand implements Command {

    private final int playerID;
    private final GameController gameController;

    public ReconnectCommand(int playerID, GameController gameController) {
        this.playerID = playerID;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        gameController.handleReconnection(playerID);
    }
}
