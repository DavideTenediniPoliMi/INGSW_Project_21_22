package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HandshakeController extends FXController {
    @FXML
    public TextField nameText;

    public void handleHandshakeButton() {
        String name = nameText.getText();

        RequestParameters handshake = new RequestParameters()
                .setCommandType(CommandType.HANDSHAKE)
                .setName(name);

        GUI.setName(name);

        notify(handshake.serialize().toString());
    }

    @Override
    public void applyChanges() {}
}
