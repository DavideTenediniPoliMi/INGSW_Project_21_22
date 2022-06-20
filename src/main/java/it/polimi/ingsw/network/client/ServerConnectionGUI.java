package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class ServerConnectionGUI extends Connection {

    public ServerConnectionGUI(Socket socket) throws IOException {
        super(socket);
    }

    private JsonObject waitForValidMessage() {
        while(true) {
            String received = receiveMessage();
            if (received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

            if (!jsonObject.has("error")) return jsonObject;

            String errorText = jsonObject.get("error").getAsString();
            Platform.runLater(() -> GUI.showError(errorText));
        }
    }

    @Override
    public void run() {
        JsonObject jsonObject = waitForValidMessage();
        new ResponseParameters().deserialize(jsonObject);

        if(!jsonObject.has("game")) {
            if (MatchInfo.getInstance().getSelectedNumPlayer() == 0) {
                Platform.runLater(() -> GUI.loadScene("/scenes/createLobbyScene.fxml"));

                new ResponseParameters().deserialize(waitForValidMessage());
                if (!GUI.didCreateLobby())
                    Platform.runLater(GUI::showAlert);
            }

            sendMessage(
                    new RequestParameters()
                            .setCommandType(CommandType.JOIN)
                            .setName(GUI.getName()).serialize().toString());

            new ResponseParameters().deserialize(waitForValidMessage());

            GUI.bindPlayerId();

            Platform.runLater(() -> GUI.loadScene("/scenes/lobbySelectionScene.fxml"));
        } else {
            GUI.bindPlayerId();
            Platform.runLater(() -> GUI.loadScene("/scenes/gameScene.fxml"));
        }

        while(true) {
            jsonObject = waitForValidMessage();

            new ResponseParameters().deserialize(jsonObject);

            if(jsonObject.has("game")) {
                Platform.runLater(() -> GUI.loadScene("/scenes/gameScene.fxml"));
            } else {
                Platform.runLater(GUI::applyChanges);
            }
        }
    }
}
