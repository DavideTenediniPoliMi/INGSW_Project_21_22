package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class LobbyUnselectTeamCommand implements Command {
    private final int playerID;
    private final LobbyController lobbyController;

    public LobbyUnselectTeamCommand(int playerID, LobbyController lobbyController) {
        this.playerID = playerID;
        this.lobbyController = lobbyController;
    }
    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        lobbyController.resetTeamColor(playerID);
    }
}
