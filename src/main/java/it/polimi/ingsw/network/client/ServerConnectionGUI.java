package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.Connection;
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
        new ResponseParameters().deserialize(waitForValidMessage());

        // TODO reconnection check

        String path = (MatchInfo.getInstance().getSelectedNumPlayer() == 0) ?
                "/scenes/createLobbyScene.fxml" :
                "/scenes/lobbySelectionScene.fxml";

        Platform.runLater(() -> GUI.loadScene(path));

        new ResponseParameters().deserialize(waitForValidMessage());

        Platform.runLater(() -> GUI.loadScene("/scenes/bindingScene.fxml"));
    }
}
