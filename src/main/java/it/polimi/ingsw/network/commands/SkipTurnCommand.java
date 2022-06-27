package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.model.MatchInfo;

/**
 * Command class that signals to the controller that the current player is not connected, and that their action should
 * be skipped.
 */
public class SkipTurnCommand implements Command {
    final int playerID;
    final GameController gameController;
    MatchInfo match;

    public SkipTurnCommand(int playerID, GameController gameController) {
        this.playerID = playerID;
        this.gameController = gameController;
        match = MatchInfo.getInstance();
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        gameController.getRoundStateController().skip();
    }
}
