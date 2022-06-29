package it.polimi.ingsw.network.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.utils.JsonUtils;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.viewStates.*;

import java.io.IOException;
import java.net.Socket;

/**
 * Class representing the serverConnection on the Client while using the CLI.
 */
public class ServerConnectionCLI extends Connection {
    private final CLI view;
    private boolean inGame;
    private boolean ready;
    private boolean joined;
    private MessageConsumer jsonConsumer;
    private final Object connectionLock = new Object();

    public ServerConnectionCLI(Socket socket) throws IOException {
        super(socket);

        ViewState viewState = new ViewState();
        viewState.addObserver(this);
        view = new CLI(new HandshakeViewState(viewState));
    }

    /**
     * Starts the CLI interaction for the handshake. Then waits a response from the server. If the response is an error
     * it repeats the process otherwise it starts the lobby sequence.
     */
    @Override
    public void run() {
        while(true) {
            view.handleHandshake();

            String received = receiveMessage();
            if(received.equals("")) continue;

            JsonObject jo = JsonParser.parseString(received).getAsJsonObject();

            if (!jo.has("error")) {
                new ResponseParameters().deserialize(jo);
                break;
            }

            view.resetInteraction(jo.get("error").getAsString());
        }

        lobbySequence();
    }

    /**
     * If the first message contains an IN_GAME status than handles the reconnection of the player and start the game
     * sequence.
     * If the first message does not contain a lobby than it prompts the user to create one, while listening for
     * other people to create one in the meanwhile. If the first messages contains a valid lobby, or the user created one,
     * then it attempts to join it. If it fails than it stops the application.
     * Once the user joined the lobby it starts reading messages, and it prompts the user to selecting a CB and team.
     * When the user is ready it listens and shows messages in the lobby until the game starts.
     */
    private void lobbySequence() {
        String lastResp = "";

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.IN_GAME)) { //Handle reconnection
            // WAIT FOR A GAME MESSAGE TO ARRIVE
            while(connected) {
                String received = receiveMessage();
                if(received.equals("")) continue;

                lastResp = received;

                JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

                if(!jsonObject.has("error") && jsonObject.has("game")) { //Received game package
                    ready = true;
                    inGame = true;
                    gameSequence(lastResp, true);
                    return;
                }
            }
        } else if(MatchInfo.getInstance().getSelectedNumPlayer() == 0) { // NO LOBBY FOUND, CREATE A LOBBY
            executor.submit(this::waitForLobbyCreation);

            //Begin lobby creation sequence
            view.setViewState(new NoLobbyViewState(view.getViewState()));
            view.handleInteraction();
        }

        //Force connection to lobby
        synchronized (connectionLock) {
            RequestParameters requestParameters = new RequestParameters()
                    .setCommandType(CommandType.JOIN);
            update(requestParameters.serialize().toString());
        }

        // WAIT FOR A VALID RESPONSE TO THE JOIN COMMAND, OTHERWISE EXIT
        while(!joined && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
            if(jsonObject.has("error")) {
                System.out.println("Error while trying to join the lobby (" + jsonObject.get("error").getAsString() +")");
                disconnect();
            }
            bindPlayerID(jsonObject);
        }

        // Begin lobby loop
        view.setViewState(new SelectLobbyViewState(view.getViewState()));
        executor.submit(view::handleInteraction);

        // READS MESSAGES, DESERIALIZES AND ASKS INTERACTION TO THE CLI UNTIL THE PLAYER IS READY.
        while(!ready && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;
            lastResp = received;

            if(isThisPlayerReady(received))
                break;

            executor.submit( () -> {
                JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
                synchronized (view) {
                    if (jsonObject.has("error")) {
                        view.resetInteraction(jsonObject.get("error").getAsString());
                        view.handleInteraction();
                        return;
                    }
                    new ResponseParameters().deserialize(jsonObject);
                    if (Lobby.getLobby().isReady(view.getPlayerID())) {
                        ready = true;
                        return;
                    }
                }
                if(view.getViewState().isInteractionComplete())
                    view.handleInteraction();
            });
        }

        if(connected) {
            view.setViewState(new LobbyViewState(view.getViewState()));
            new ResponseParameters().deserialize(JsonParser.parseString(lastResp).getAsJsonObject());
            view.displayState();
        }

        // SHOWS NEW MESSAGES INCOMING UNTIL THE GAME STARTS
        while(!inGame && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;
            lastResp = received;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
            if(jsonObject.has("matchInfo")) {
                JsonObject matchInfoJson = jsonObject.get("matchInfo").getAsJsonObject();
                if(matchInfoJson.get("gameStatus").getAsString().equalsIgnoreCase("IN_GAME")) {
                    synchronized (view) {
                        inGame = true;
                    }
                    break;
                }
            }
            executor.submit( () -> {
                synchronized (view) {
                    new ResponseParameters().deserialize(jsonObject);
                    view.displayState();
                }
            });
        }

        gameSequence(lastResp, false);
    }

    /**
     * Keeps reading from the socket and deserializes the messages that it reads. If nextState returns true then asks
     * the CLI to perform an interaction otherwise shows the changes to the user.
     */
    private void gameSequence(String lastResponse, boolean isReconnection) {
        if(!connected)
            return;

        JsonObject initJsonObject = JsonParser.parseString(lastResponse).getAsJsonObject();

        if(!joined)
            bindPlayerID(initJsonObject);

        new ResponseParameters().deserialize(initJsonObject);

        // Waits for a matchInfo and a players message after the reconnection to align the message streams.
        if(isReconnection) {
            new ResponseParameters().deserialize(JsonParser.parseString(receiveMessage()).getAsJsonObject());
            new ResponseParameters().deserialize(JsonParser.parseString(receiveMessage()).getAsJsonObject());
        }

        view.resetTurnState(MatchInfo.getInstance().serialize());

        if(MatchInfo.getInstance().getCurrentPlayerID() == view.getPlayerID()) { // this user's turn
            view.handleFirstInteraction();
        } else { // somebody's else turn
            view.setViewState(new GameViewState(view.getViewState()));
            view.displayState();
        }

        MessageQueue<String> packetQueue = new MessageQueue<>();
        jsonConsumer = new MessageConsumer(packetQueue, view);
        executor.submit( () -> jsonConsumer.run());

        while(inGame && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;

            packetQueue.add(received);
            packetQueue.notifyAllForEmpty();
        }
        jsonConsumer.stop();
        System.out.println("Exiting...");
        System.exit(0);
    }

    /**
     * Checks if the player using this client has been detected ready by the server.
     *
     * @param received the last message received.
     * @return true is this player is ready, false otherwise.
     */
    private boolean isThisPlayerReady(String received) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(received);
        if(jsonObject.has("players")) {
            JsonArray jsonPlayers = jsonObject.get("players").getAsJsonArray();

            for(JsonElement jsonPlayer : jsonPlayers) {
                JsonObject playerObj = jsonPlayer.getAsJsonObject();
                if(playerObj.get("ID").getAsInt() == view.getPlayerID())
                    return playerObj.get("ready").getAsBoolean();
            }
        }
        return false;
    }

    /**
     * Binds the view with the ID assigned to the player using this client on the server.
     *
     * @param jsonObject the last message received.
     */
    private void bindPlayerID(JsonObject jsonObject) {
        JsonObject gameObject;

        if(jsonObject.has("game"))
            gameObject = jsonObject.get("game").getAsJsonObject();
        else
            gameObject = jsonObject;

        if(gameObject.has("players")) {
            JsonArray players = gameObject.get("players").getAsJsonArray();
            for(JsonElement player : players) {
                if(player.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(view.getName())) {
                    joined = true;
                    view.setPlayerID(player.getAsJsonObject().get("ID").getAsInt());
                    new ResponseParameters().deserialize(jsonObject);
                    break;
                }
            }
        }
    }

    /**
     * Async task that listens for messages that indicates the creation of another lobby while this user is creating one.
     */
    private void waitForLobbyCreation() {
        while(connected) {
            String received;
            synchronized (connectionLock) {
                received = receiveMessage();
            }
            if(received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

            if(!jsonObject.has("error")) { //Lobby was created
                new ResponseParameters().deserialize(jsonObject);
                synchronized (view) {
                    if(!view.getViewState().isInteractionComplete()) {
                        view.setViewState(new LobbyCreatedViewState(view.getViewState()));
                        view.handleInteraction();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        view.getViewState().setInteractionComplete(true);
        view.loadClosingScreen();
        System.exit(1);
    }
}
