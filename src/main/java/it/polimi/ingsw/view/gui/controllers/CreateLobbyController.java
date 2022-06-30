package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class representing the controller of the scene handling the creation of a new Lobby.
 */
public class CreateLobbyController extends FXController {
    @FXML
    private Slider numPlayers;
    @FXML
    private CheckBox expertMode;

    /**
     * Handles the click of the button in this scene
     */
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
        alert.setTitle("OOPS!");
        alert.setHeaderText("Somebody just created a lobby!");
        alert.setContentText("You automatically joined the new lobby!");

        alert.show();
    }

    /**
     * Handles the press of the ENTER key. It acts as if the Create button had been pressed instead.
     *
     * @param keyEvent the event fired by the press of any key.
     */
    public void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            handleCreateLobbyButton();
        }
    }
}
