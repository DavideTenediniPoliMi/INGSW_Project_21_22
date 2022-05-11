package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.MissingInfoAboutPlayerException;
import it.polimi.ingsw.exceptions.lobby.NoSuchPlayerException;

/**
 * Lobby Command class to request a change in the ready status.
 */
public class LobbyReadyStatusCommand implements Command {

    private final int playerID;
    private final boolean ready;
    private final LobbyController lobbyController;

    public LobbyReadyStatusCommand(int playerID, boolean ready, LobbyController lobbyController) {
        this.playerID = playerID;
        this.ready = ready;
        this.lobbyController = lobbyController;
    }

    @Override
    public void execute() throws NoSuchPlayerException, MissingInfoAboutPlayerException {
        lobbyController.setReadyStatus(playerID, ready);
    }
}
