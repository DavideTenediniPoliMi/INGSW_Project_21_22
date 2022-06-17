package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class CreateLobbyController extends FXController {
    @FXML
    private Slider numPlayers;
    @FXML
    private CheckBox expertMode;


    public void handleCreateLobbyButton() {
        GUI.setCreatedLobby(true);

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.CREATE_LOBBY)
                        .setExpertMode(expertMode.isSelected())
                        .setSelectedNumPlayer((int) numPlayers.getValue())
                        .serialize().toString()
        );
    }

    @Override
    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OOPPS!");
        alert.setHeaderText("Somebody just created a lobby!");
        alert.setContentText("You automatically joined the new lobby!");

        alert.show();
    }
}
