package it.polimi.ingsw.view;

import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.exceptions.lobby.DuplicateIDException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.CommandFactory;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;

public class VirtualView extends Observable<String> implements Observer<ResponseParameters> {
    private final String name;
    private boolean connected, skipping;
    private int playerID;
    private CommandFactory commandFactory;
    private final LobbyController lobbyController;
    private final GameController gameController;

    public VirtualView(String name, LobbyController lobbyController, GameController gameController) {
        this.lobbyController = lobbyController;
        this.gameController = gameController;
        this.name = name;
        playerID = 0;

        Game.getInstance().addObserver(this);
        MatchInfo.getInstance().addObserver(this);
        Lobby.getLobby().addObserver(this);
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return connected;
    }

    public synchronized void handleRequest(String message) {
        RequestParameters params = new RequestParameters();
        params.deserialize(JsonParser.parseString(message).getAsJsonObject());

        try {
            Command command;

            if(commandFactory == null) {
                if(params.getCommandType() == CommandType.CREATE_LOBBY) {
                    command = new CommandFactory(playerID, lobbyController, gameController).createCommand(params);
                } else {
                    do {
                        try {
                            params.setName(name);

                            CommandFactory tempFactory = new CommandFactory(playerID, lobbyController, gameController);
                            command = tempFactory.createCommand(params);

                            lobbyController.requestCommand(command);

                            commandFactory = tempFactory;
                            return;
                        } catch(DuplicateIDException e) {
                            System.out.println("Trying again!");
                            playerID += 1;
                        } catch(EriantysException | EriantysRuntimeException e) {
                            update(new ResponseParameters().setError(e.getMessage()));
                            return;
                        }
                    } while (true);
                }
            } else {
                command = commandFactory.createCommand(params);
            }

            if(params.getCommandType().isUrgent) {
                command.execute();
                return;
            }

            if (params.getCommandType().isInGame) {
                gameController.requestCommand(playerID, command);
            } else {
                lobbyController.requestCommand(command);
            }
        } catch (EriantysException | EriantysRuntimeException e) {
            update(new ResponseParameters().setError(e.getMessage()));
        }
    }

    @Override
    public void update(ResponseParameters params) {
        if(connected) {
            System.out.println(params.serialize().toString());
            notify(params.serialize().toString());
        } else {
            skipIfCurrent();
        }
    }

    @Override
    public void addObserver(Observer<String> observer) {
        super.addObserver(observer);
        connected = true;
        if(commandFactory != null) {
            // Player was connected
            reconnect();
        }
    }

    public void reconnect() {
        String reconnectCommand = new RequestParameters().setCommandType(CommandType.RECONNECT).serialize().toString();
        handleRequest(reconnectCommand);
        notify(new ResponseParameters().setSendGame(true).serialize().toString());
    }

    public void disconnect() {
        connected = false;
        skipping = true;
        clearObservers();
        String disconnectCommand = new RequestParameters().setCommandType(CommandType.DISCONNECT).serialize().toString();
        handleRequest(disconnectCommand);
        skipping = false;
        skipIfCurrent();
    }

    public void skipIfCurrent() {
        MatchInfo match = MatchInfo.getInstance();
        if(match.getGameStatus().equals(GameStatus.IN_GAME) && match.getCurrentPlayerID() == playerID && !gameController.isPaused() && !skipping) {
            skipping = true;
            String skipCommand = new RequestParameters().setCommandType(CommandType.SKIP_TURN).serialize().toString();
            handleRequest(skipCommand);
        }
        skipping = false;
    }

    public void deserialize(int playerID) {
        this.playerID = playerID;
        commandFactory = new CommandFactory(playerID, lobbyController, gameController);
    }
}
