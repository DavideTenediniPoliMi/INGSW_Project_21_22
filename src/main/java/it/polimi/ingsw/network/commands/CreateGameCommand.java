package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class CreateGameCommand implements Command {
    private final int selectedNumPlayer;
    private final boolean expertMode;
    private final LobbyController lobbyController;

    public CreateGameCommand(int selectedNumPlayer, boolean expertMode, LobbyController lobbyController) {
        this.selectedNumPlayer = selectedNumPlayer;
        this.expertMode = expertMode;
        this.lobbyController = lobbyController;
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        lobbyController.createGame(selectedNumPlayer, expertMode);
    }
}
