package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class CreateLobbyController extends FXController {
    @FXML
    private Slider numPlayers;
    @FXML
    private CheckBox expertMode;


    public void handleCreateLobbyButton() {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.CREATE_LOBBY)
                        .setExpertMode(expertMode.isSelected())
                        .setSelectedNumPlayer((int) numPlayers.getValue())
                        .serialize().toString()
        );
    }
}
