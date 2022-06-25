package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class representing the controller of the scene handling the handshake of server and client.
 */
public class HandshakeController extends FXController {
    @FXML
    public TextField nameText;

    /**
     * Handles the click of the button in this scene.
     */
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

    /**
     * Handles the press of the ENTER key. Acts as if the Binding button had been pressed instead
     * @param keyEvent the event fired by the press of any key.
     */
    public void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleHandshakeButton();
        }
    }
}
