package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIState;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

/**
 * Class representing the serverConnection on the Client while using the GUI.
 */
public class ServerConnectionGUI extends Connection {

    public ServerConnectionGUI(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Reads from the socket until it receives a non-error message. Error messages are properly displayed.
     *
     * @return the last valid message received.
     */
    private JsonObject waitForValidMessage() {
        while(connected) {
            String received = receiveMessage();
            if (received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

            if (!jsonObject.has("error")) return jsonObject;

            String errorText = jsonObject.get("error").getAsString();
            Platform.runLater(() -> GUI.showError(errorText));
            Platform.runLater(() -> GUI.handleInteraction(GUIState.REPEAT_ACTION));
        }
        return new JsonObject();
    }

    /**
     * If the first message contains an IN_GAME status than handles the reconnection of the player and start the game
     * sequence.
     * If the first message does not contain a lobby than it prompts the user to create one, while listening for
     * other people to create one in the meanwhile. If the first messages contains a valid lobby, or the user created one,
     * then it attempts to join it. If it fails than it restarts the application.
     * Once the user joined the lobby it starts reading messages, and it prompts the user to selecting a CB and team.
     * When the user is ready it listens and shows messages in the lobby until the game starts.
     */
    @Override
    public void run() {
        JsonObject jsonObject = waitForValidMessage();
        deserialize(jsonObject);
        boolean attemptedCreation = false;

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.LOBBY)) { // THE GAME HAS NOT STARTED
            if (MatchInfo.getInstance().getSelectedNumPlayer() == 0) { // THERE'S NO LOBBY YET
                attemptedCreation = true;
                Platform.runLater(() -> GUI.loadScene("/scenes/createLobbyScene.fxml"));

                deserialize(waitForValidMessage());
            }

            sendMessage(
                    new RequestParameters()
                            .setCommandType(CommandType.JOIN)
                            .setName(GUI.getName()).serialize().toString());

            // WAITS FOR A VALID JOIN RESPONSE OR GOES TO THE STARTING SCREEN
            do {
                JsonObject received = JsonParser.parseString(receiveMessage()).getAsJsonObject();

                if (received.has("error")) {
                    Platform.runLater(() -> GUI.loadScene("/scenes/bindingScene.fxml"));
                    Platform.runLater(() -> GUI.showError(received.get("error").getAsString()));
                    run();
                    return;
                }

                deserialize(received);
            } while(connected && getPlayerByName(GUI.getName(), Lobby.getLobby().getPlayers()) == null);

            GUI.bindPlayerId();

            Platform.runLater(() -> GUI.loadScene("/scenes/lobbySelectionScene.fxml"));

            if (!GUI.didCreateLobby() && attemptedCreation) //THE LOBBY CREATION WAS INTERRUPTED
                Platform.runLater(GUI::showAlert);
        } else { // RECONNECTION MID-GAME
            while(!jsonObject.has("game")) {
                jsonObject = waitForValidMessage();
                deserialize(jsonObject);
            }

            GUI.bindPlayerId();
            Platform.runLater(() -> GUI.loadScene("/scenes/gameScene.fxml"));
            // DISCARDS A MATCH_INFO AND PLAYERS MESSAGE
            deserialize(waitForValidMessage());
            deserialize(waitForValidMessage());
            gameLoop();
            return;
        }

        // LOBBY SELECTION LOOP
        while(connected) {
            jsonObject = waitForValidMessage();

            deserialize(jsonObject);

            if(jsonObject.has("game")) {
                GUI.bindPlayerId();
                Platform.runLater(() -> GUI.loadScene("/scenes/gameScene.fxml"));
                gameLoop();
                return;
            } else {
                JsonObject finalJsonObject = jsonObject;
                Platform.runLater(() -> GUI.applyChanges(finalJsonObject));
            }
        }
    }

    /**
     * Returns a player with the specified name in the list of players. If none are found, returns null.
     *
     * @param name the name of the player
     * @param players the list of players
     * @return the target <code>Player</code>, or null.
     */
    public Player getPlayerByName(String name, List<Player> players) {
        Optional<Player> result = players.stream()
                .filter((player) -> (player.getName().equals(name)))
                .findAny();

        return result.orElse(null);
    }

    /**
     * Reads messages and deserializes them, then asks the GUI to show the changes and interaction if necessary.
     */
    private void gameLoop() {
        JsonObject jsonObject;

        while(connected) {
            jsonObject = waitForValidMessage();

            GUIState nextState = GUI.nextState(jsonObject);
            deserialize(jsonObject);

            JsonObject finalJsonObject = jsonObject;
            Platform.runLater(() -> GUI.applyChanges(finalJsonObject));
            Platform.runLater(() -> GUI.handleInteraction(nextState));
        }
    }

    /**
     * Deserializes the message received, applying the changes to the local model. The function is
     * synchronized on the current scene controller.
     * @param jsonObject the last message received.
     */
    public void deserialize(JsonObject jsonObject) {
        synchronized (GUI.getController()) {
            new ResponseParameters().deserialize(jsonObject);
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        Platform.runLater(() -> GUI.loadScene("/scenes/bindingScene.fxml"));
        Platform.runLater(() -> GUI.showError("Disconnected from the server!"));
        MatchInfo.resetInstance();
        Lobby.resetLobby();
        Game.resetInstance();
    }
}
