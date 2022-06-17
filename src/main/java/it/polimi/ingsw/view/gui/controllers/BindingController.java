package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.ServerConnectionGUI;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;

public class BindingController extends FXController {
    @FXML
    private TextField ipAddressText;
    @FXML
    private TextField portText;

    public void handleBindingButton() {
        String ip = ipAddressText.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portText.getText().trim());
            GUI.setServerConnection(new ServerConnectionGUI(new Socket(ip, port)));

            // IF NO EXCEPTIONS WERE THROWN PROCEED TO THE HANDSHAKE SCENE
            GUI.loadScene("/scenes/handshakeScene.fxml");
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            showError("Something went wrong! Try again!");
        }
    }
}
