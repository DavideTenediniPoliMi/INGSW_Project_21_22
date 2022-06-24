package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.ServerConnectionGUI;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    public void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            if(((Node) keyEvent.getSource()).getId().equals("ipAddressText")) {
                portText.requestFocus();
            } else {
                handleBindingButton();
            }
        }
    }
}
