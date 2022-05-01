package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.CardBackTakenException;
import it.polimi.ingsw.exceptions.lobby.NoSuchPlayerException;
import it.polimi.ingsw.model.enumerations.CardBack;

/**
 * Lobby Command class to request setting a <code>CardBack</code>.
 */
public class LobbySelectCBCommand implements Command {

    private final int playerID;
    private final CardBack cardBack;
    private final LobbyController lobbyController;

    public LobbySelectCBCommand(int playerID, CardBack cardBack, LobbyController lobbyController) {
        this.playerID = playerID;
        this.cardBack = cardBack;
        this.lobbyController = lobbyController;
    }
    @Override
    public void execute() throws NoSuchPlayerException, CardBackTakenException {
        lobbyController.setCardBack(playerID, cardBack);
    }
}
