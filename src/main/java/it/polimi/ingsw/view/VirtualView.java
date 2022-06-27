package it.polimi.ingsw.view;

import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.exceptions.lobby.DuplicateIDException;
import it.polimi.ingsw.exceptions.lobby.GameFullException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.CommandFactory;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;

/**
 * Class representing the VirtualView. It acts as a layer of virtualization between the server and the network where the
 * actual clients are.
 */
public class VirtualView extends Observable<String> implements Observer<ResponseParameters> {
    private final String name;
    private boolean connected, skipping, joined, outdated;
    private int playerID;
    private CommandFactory commandFactory;
    private final LobbyController lobbyController;
    private final GameController gameController;
    private TurnState skippingTurn;

    public VirtualView(String name, LobbyController lobbyController, GameController gameController) {
        this.lobbyController = lobbyController;
        this.gameController = gameController;
        this.name = name;
        playerID = 0;
        joined = false;
        outdated = true;

        Game.getInstance().addObserver(this);
        MatchInfo.getInstance().addObserver(this);
        Lobby.getLobby().addObserver(this);
    }

    /**
     * Returns the name of the player bound to this VirtualView.
     *
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the player is connected to this VirtualView.
     *
     * @return true if the player is connected, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    /**
     * Handles any request coming from the clients, and it interfaces with the controller.
     *
     * @param message the message received from the client.
     */
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
                            if(!params.getCommandType().equals(CommandType.JOIN))
                                update(new ResponseParameters().setError("Can't send commands before joining!"));

                            params.setName(name);

                            CommandFactory tempFactory = new CommandFactory(playerID, lobbyController, gameController);
                            command = tempFactory.createCommand(params);

                            lobbyController.requestCommand(command);

                            commandFactory = tempFactory;
                            joined = true;
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
        if(connected && !params.shouldSkip()) { // Avoid notifying useless packets if the client is connected
            notify(params.serialize().toString());
            //If the last turn skipped is NOT a PLANNING turn (Meaning this player has already skipped its last round)
            if(skippingTurn != null && !skippingTurn.equals(TurnState.PLANNING)) {
                //If we are into a new round, set this VirtualView to outdated
                if(MatchInfo.getInstance().getStateType().equals(TurnState.PLANNING)) {
                    outdated = true;
                    skipping = false;
                }
            }
        } else {
            if(params.shouldSkip() && shouldSkip())
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

    /**
     * Tells the controller that the player bound to this VirtualView has reconnected.
     */
    public void reconnect() {
        String reconnectCommand = new RequestParameters().setCommandType(CommandType.RECONNECT).serialize().toString();
        handleRequest(reconnectCommand);
        notify(new ResponseParameters().setSendGame(true).serialize().toString());
        Game.getInstance().notifyPlayerStatusChange();
        /*
         * If the current round is in a different state compared to the last one skipped, then the info on this
         * VirtualView is outdated.
         */
        if(!MatchInfo.getInstance().getStateType().equals(skippingTurn)) {
            outdated = true; // Force the update of the skipping boolean
            skipping = false; // Reset skipping value
        }
    }

    /**
     * Tells the controller that the player bound to this VirtualView has disconnected. If necessary sets up
     * the skipping mechanism for the player.
     */
    public void disconnect() {
        connected = false;
        //Skip everything else if player has NOT joined the game (Only applies to lobby)
        if(!joined)
            return;

        //Begin turn-skip sequence (Player is in-game)
        clearObservers();
        String disconnectCommand = new RequestParameters().setCommandType(CommandType.DISCONNECT).serialize().toString();
        handleRequest(disconnectCommand);
        Game.getInstance().notifyPlayerStatusChange();

        //This VirtualView will skip if the player reconnects DURING A ROUND
        skipping = true;

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.LOBBY)){
            Lobby.getLobby().removeObserver(this);
            Game.getInstance().removeObserver(this);
            MatchInfo.getInstance().removeObserver(this);
        }
    }

    /**
     * Returns whether the <code>Player</code> bound to this <code>VirtualView</code> should try to skip a turn.
     *
     * @return <code>true</code> if the <code>Player</code> has to skip a turn.
     */
    private boolean shouldSkip() {
        if(!connected)
            return true;

        //If the round is in any state other than PLANNING, then this Player must skip until the end of this round.
        if(outdated && !MatchInfo.getInstance().getStateType().equals(TurnState.PLANNING)) {
            skipping = Game.getInstance().getPlayerByID(playerID).getSelectedCard().equals(Card.CARD_AFK);
            outdated = false;
            skippingTurn = null;
        }
        return skipping;
    }

    /**
     * If the player is not connected and if necessary it tells the controller to skip the turns of the player.
     */
    public void skipIfCurrent() {
        MatchInfo match = MatchInfo.getInstance();
        if(match.getGameStatus().equals(GameStatus.IN_GAME) &&
                match.getCurrentPlayerID() == playerID &&
                !MatchInfo.getInstance().isGamePaused()) {
            skippingTurn = MatchInfo.getInstance().getStateType();
            String skipCommand = new RequestParameters().setCommandType(CommandType.SKIP_TURN).serialize().toString();
            handleRequest(skipCommand);
        }
    }

    /**
     * Rebuilds a working VirtualView for the ID after the server shut down.
     *
     * @param playerID the ID of a player who was connected to the server before the shut down.
     */
    public void deserialize(int playerID) {
        this.playerID = playerID;
        commandFactory = new CommandFactory(playerID, lobbyController, gameController);
    }

    /**
     * Checks if the player bound to this VirtualView has joined the game.
     *
     * @return true if the player joined the game, false otherwise.
     */
    public boolean hasJoined() {
        return joined;
    }
}
