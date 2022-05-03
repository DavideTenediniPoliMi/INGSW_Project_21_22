package it.polimi.ingsw.view;

import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.CommandFactory;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;

public class VirtualView extends Observable<String> implements Observer<ResponseParameters> {
    private final int playerID;
    private final CommandFactory commandFactory;
    private final LobbyController lobbyController;
    private final GameController gameController;
    public VirtualView(int playerID, LobbyController lobbyController, GameController gameController) {
        this.lobbyController = lobbyController;
        this.gameController = gameController;
        this.playerID = playerID;

        Game.getInstance().addObserver(this);
        MatchInfo.getInstance().addObserver(this);
        Lobby.getLobby().addObserver(this);
        commandFactory = new CommandFactory(playerID, lobbyController, gameController);
    }

    public void handleRequest(String message) {
        RequestParameters params = new RequestParameters();
        params.deserialize(JsonParser.parseString(message).getAsJsonObject());
        try {
            Command command = commandFactory.createCommand(params);
            if (params.getCommandType().isInGame) {
                gameController.requestCommand(playerID, command);
            } else {
                lobbyController.requestCommand(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ResponseParameters params) {
        notify(params.serialize().toString());
    }
}
