package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.model.MatchInfo;

public class DisconnectCommand implements Command {

    private final int playerID;
    private final LobbyController lobbyController;
    private final GameController gameController;

    public DisconnectCommand(int playerID, LobbyController lobbyController, GameController gameController) {
        this.playerID = playerID;
        this.lobbyController = lobbyController;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        switch (MatchInfo.getInstance().getGameStatus()) {
            case LOBBY:
                lobbyController.removePlayer(playerID);
                break;
            case IN_GAME:
                gameController.handleDisconnection(playerID);
                break;
            default:
                break;
        }
    }
}
