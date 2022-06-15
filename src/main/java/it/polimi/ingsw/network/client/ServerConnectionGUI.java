package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    @Override
    public void run() {
        while(true) {
            String received = receiveMessage();
            if(received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

            if(jsonObject.has("error")) {
                String errorText = jsonObject.get("error").getAsString();
                Platform.runLater(() -> GUI.showError(errorText));
                continue;
            }

            Platform.runLater(() -> {
                try {
                    GUI.loadScene("/scenes/bindingScene.fxml");
                } catch (IOException e) {
                    Platform.runLater(() -> GUI.showError(e.getMessage()));
                }
            });

            new ResponseParameters().deserialize(jsonObject);
        }
    }
}
