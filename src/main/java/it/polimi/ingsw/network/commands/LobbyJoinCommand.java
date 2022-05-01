package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.DuplicateIDException;
import it.polimi.ingsw.exceptions.lobby.GameFullException;
import it.polimi.ingsw.exceptions.lobby.NameTakenException;

public class LobbyJoinCommand implements Command {

    private final int playerID;
    private final String name;
    private final LobbyController lobbyController;

    public LobbyJoinCommand(int playerID, String name, LobbyController lobbyController) {
        this.playerID = playerID;
        this.name = name;
        this.lobbyController = lobbyController;
    }

    @Override
    public void execute() throws NameTakenException, GameFullException, DuplicateIDException {
        lobbyController.joinLobby(playerID, name);
    }
}
