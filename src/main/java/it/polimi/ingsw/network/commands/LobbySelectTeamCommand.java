package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.NoSuchPlayerException;
import it.polimi.ingsw.exceptions.lobby.TeamFullException;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Lobby Command class to request the selection of a team.
 */
public class LobbySelectTeamCommand implements Command {

    private final int playerID;
    private final TowerColor towerColor;
    private final LobbyController lobbyController;

    public LobbySelectTeamCommand(int playerID, TowerColor towerColor, LobbyController lobbyController) {
        this.playerID = playerID;
        this.towerColor = towerColor;
        this.lobbyController = lobbyController;
    }

    @Override
    public void execute() throws NoSuchPlayerException, TeamFullException {
        lobbyController.setTowerColor(playerID, towerColor);
    }
}
