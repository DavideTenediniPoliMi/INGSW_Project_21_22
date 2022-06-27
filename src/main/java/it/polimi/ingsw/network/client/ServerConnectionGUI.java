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

public class ServerConnectionGUI extends Connection {

    public ServerConnectionGUI(Socket socket) throws IOException {
        super(socket);
    }

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

    @Override
    public void run() {
        JsonObject jsonObject = waitForValidMessage();
        new ResponseParameters().deserialize(jsonObject);
        boolean attemptedCreation = false;

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.LOBBY)) {
            if (MatchInfo.getInstance().getSelectedNumPlayer() == 0) {
                attemptedCreation = true;
                Platform.runLater(() -> GUI.loadScene("/scenes/createLobbyScene.fxml"));

                new ResponseParameters().deserialize(waitForValidMessage());
            }

            sendMessage(
                    new RequestParameters()
                            .setCommandType(CommandType.JOIN)
                            .setName(GUI.getName()).serialize().toString());

            do {
                JsonObject received = JsonParser.parseString(receiveMessage()).getAsJsonObject();

                if (received.has("error")) {
                    Platform.runLater(() -> GUI.loadScene("/scenes/bindingScene.fxml"));
                    Platform.runLater(() -> GUI.showError(received.get("error").getAsString()));
                    run();
                    return;
                }

                new ResponseParameters().deserialize(received);
            } while(connected && getPlayerByName(GUI.getName(), Lobby.getLobby().getPlayers()) == null);

            GUI.bindPlayerId();

            Platform.runLater(() -> GUI.loadScene("/scenes/lobbySelectionScene.fxml"));
            if (!GUI.didCreateLobby() && attemptedCreation)
                Platform.runLater(GUI::showAlert);
        } else {
            while(!jsonObject.has("game")) {
                jsonObject = waitForValidMessage();
                new ResponseParameters().deserialize(jsonObject);
            }

            GUI.bindPlayerId();
            Platform.runLater(() -> GUI.loadScene("/scenes/gameScene.fxml"));
            new ResponseParameters().deserialize(waitForValidMessage());
            new ResponseParameters().deserialize(waitForValidMessage());
            gameLoop();
            return;
        }

        while(connected) {
            jsonObject = waitForValidMessage();

            new ResponseParameters().deserialize(jsonObject);

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

    public Player getPlayerByName(String name, List<Player> players) {
        Optional<Player> result = players.stream()
                .filter((player) -> (player.getName().equals(name)))
                .findAny();

        return result.orElse(null);
    }

    private void gameLoop() {
        JsonObject jsonObject;

        while(connected) {
            jsonObject = waitForValidMessage();

            GUIState nextState = GUI.nextState(jsonObject);
            new ResponseParameters().deserialize(jsonObject);

            JsonObject finalJsonObject = jsonObject;
            Platform.runLater(() -> GUI.applyChanges(finalJsonObject));
            Platform.runLater(() -> GUI.handleInteraction(nextState));
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
