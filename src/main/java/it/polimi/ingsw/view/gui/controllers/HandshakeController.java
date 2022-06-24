package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HandshakeController extends FXController {
    @FXML
    public TextField nameText;

    public void handleHandshakeButton() {
        String name = nameText.getText();

        if(name.isEmpty()) {
            showError("Not a valid name!");
            return;
        }

        RequestParameters handshake = new RequestParameters()
                .setCommandType(CommandType.HANDSHAKE)
                .setName(name);

        GUI.setName(name);

        notify(handshake.serialize().toString());
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleHandshakeButton();
        }
    }
}
