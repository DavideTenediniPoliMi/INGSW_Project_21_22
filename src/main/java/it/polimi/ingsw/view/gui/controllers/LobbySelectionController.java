package it.polimi.ingsw.view.gui.controllers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the controller of the scene handling the Lobby.
 */
public class LobbySelectionController extends FXController {
    @FXML
    private Label expertMode;
    @FXML
    private Label playersConnected;
    @FXML
    private Label maxPlayers;
    @FXML
    private Label usernameText1;
    @FXML
    private Label usernameText2;
    @FXML
    private Label usernameText3;
    @FXML
    private Label usernameText4;
    @FXML
    private ImageView lobbyImage1;
    @FXML
    private ImageView lobbyImage2;
    @FXML
    private ImageView lobbyImage3;
    @FXML
    private ImageView lobbyImage4;
    @FXML
    private ImageView tower1;
    @FXML
    private ImageView tower2;
    @FXML
    private ImageView tower3;
    @FXML
    private ImageView tower4;
    @FXML
    private Button wizardButton1;
    @FXML
    private Button wizardButton2;
    @FXML
    private Button wizardButton3;
    @FXML
    private Button wizardButton4;
    @FXML
    private CheckBox readyCheckbox1;
    @FXML
    private CheckBox readyCheckbox2;
    @FXML
    private CheckBox readyCheckbox3;
    @FXML
    private CheckBox readyCheckbox4;
    @FXML
    private RadioButton blackOption;
    @FXML
    private RadioButton whiteOption;
    @FXML
    private RadioButton greyOption;
    @FXML
    private ImageView blackTower;
    @FXML
    private ImageView whiteTower;
    @FXML
    private ImageView greyTower;
    @FXML
    private Button cardBackButton;
    @FXML
    private Button towerColorButton;
    @FXML
    private Button readyButton;
    @FXML
    private ToggleGroup team;

    private CardBack activeCardBack;
    private TowerColor activeTowerColor;

    private final List<Label> usernames = new ArrayList<>();
    private final List<ImageView> images = new ArrayList<>();
    private final List<ImageView> towers = new ArrayList<>();
    private final List<Button> wizardButtons = new ArrayList<>();
    private final List<RadioButton> teamOptions = new ArrayList<>();
    private final List<ImageView> towerOptions = new ArrayList<>();
    private final List<CheckBox> readyCheckBoxes = new ArrayList<>();

    @FXML
    public synchronized void initialize() {
        MatchInfo matchInfo = MatchInfo.getInstance();

        prepArrays();

        maxPlayers.setText(String.valueOf(matchInfo.getSelectedNumPlayer()));
        expertMode.setVisible(false);

        for(int i = 0; i < 4; i ++) {
            if(i >= matchInfo.getSelectedNumPlayer())
                images.get(i).setImage(new Image("images/cardback_black_and_white.png"));
        }

        if(matchInfo.getSelectedNumPlayer() != 3) {
            greyOption.setVisible(false);
            greyTower.setVisible(false);
        }

        team.selectedToggleProperty().addListener((event) -> {
            if(team.getSelectedToggle() != null) {
                activeTowerColor = TowerColor.valueOf(((RadioButton) team.getSelectedToggle()).getText().toUpperCase());
                towerColorButton.setDisable(false);
            } else {
                activeTowerColor = null;
                if(towerColorButton.getText().equals("Confirm!")) {
                    towerColorButton.setDisable(true);
                }
            }
        });

        applyChanges(null);
    }

    /**
     * Populates all the arrays used with the different elements of the graphic in the predetermined ordered.
     */
    private void prepArrays() {
        usernames.add(usernameText1);
        usernames.add(usernameText2);
        usernames.add(usernameText3);
        usernames.add(usernameText4);

        images.add(lobbyImage1);
        images.add(lobbyImage2);
        images.add(lobbyImage3);
        images.add(lobbyImage4);

        towers.add(tower1);
        towers.add(tower2);
        towers.add(tower3);
        towers.add(tower4);

        wizardButtons.add(wizardButton1);
        wizardButtons.add(wizardButton2);
        wizardButtons.add(wizardButton3);
        wizardButtons.add(wizardButton4);

        teamOptions.add(blackOption);
        teamOptions.add(whiteOption);
        teamOptions.add(greyOption);

        towerOptions.add(blackTower);
        towerOptions.add(whiteTower);
        towerOptions.add(greyTower);

        readyCheckBoxes.add(readyCheckbox1);
        readyCheckBoxes.add(readyCheckbox2);
        readyCheckBoxes.add(readyCheckbox3);
        readyCheckBoxes.add(readyCheckbox4);
    }

    @Override
    public synchronized void applyChanges(JsonObject jsonObject) {
        Image defaultImage = new Image("/images/Personaggi_retro.jpg");
        Lobby lobby = Lobby.getLobby();
        MatchInfo matchInfo = MatchInfo.getInstance();
        List<Player> players = lobby.getPlayers();

        List<TowerColor> pickedTeams = new ArrayList<>();

        if(matchInfo.isExpertMode())
            expertMode.setVisible(true);

        playersConnected.setText(String.valueOf(lobby.getPlayers().size()));

        if(cardBackButton.getText().equals("Confirm!")) {
            setDisableWizardButtons(false);
        }

        if(towerColorButton.getText().equals("Confirm!")) {
            setDisableTeamOptions(false);
        }

        resetReadyCheckBoxes();

        for(int i = 0 ; i < matchInfo.getSelectedNumPlayer() ; i++) {
            ImageView image = images.get(i);
            Label username = usernames.get(i);
            ImageView tower = towers.get(i);

            if (i >= players.size()){
                username.setVisible(false);
                tower.setVisible(false);
                image.setImage(defaultImage);
                continue;
            }

            Player player = players.get(i);

            username.setVisible(true);
            username.setText(player.getName());

            if(player.getTeamColor() != null) {
                if(matchInfo.getSelectedNumPlayer() != 4) {
                    teamOptions.get(player.getTeamColor().ordinal()).setDisable(true);
                    teamOptions.get(player.getTeamColor().ordinal()).setSelected(false);
                    towerOptions.get(player.getTeamColor().ordinal()).setDisable(true);
                } else {
                    if(pickedTeams.contains(player.getTeamColor())) {
                        teamOptions.get(player.getTeamColor().ordinal()).setDisable(true);
                        teamOptions.get(player.getTeamColor().ordinal()).setSelected(false);
                        towerOptions.get(player.getTeamColor().ordinal()).setDisable(true);
                    } else {
                        pickedTeams.add(player.getTeamColor());
                    }
                }

                tower.setVisible(true);
                tower.setImage(player.getTeamColor().getImage());
            } else {
                tower.setVisible(false);
            }

            if(player.getCardBack() != null) {
                if(player.getCardBack().equals(activeCardBack)) {
                    activeCardBack = null;
                    resetWizardButtons();
                    cardBackButton.setDisable(true);
                }
                wizardButtons.get(player.getCardBack().ordinal()).setDisable(true);
                image.setImage(player.getCardBack().getImage());
            } else {
                image.setImage(defaultImage);
            }

            if(lobby.isReady(player.getID())) {
                readyCheckBoxes.get(i).setVisible(true);
            }
        }
    }

    // EVENT LISTENERS

    /**
     * Handles the click of the wizard buttons. Sets the cardBack selected.
     *
     * @param mouseEvent the event fired by the click of the mouse.
     */
    public void handleWizardButtons(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        activeCardBack = getCardBackOfButton(button);

        resetWizardButtons();

        button.setStyle("-fx-border-color: yellow; -fx-border-width: 5px;");
        cardBackButton.setDisable(false);
    }

    /**
     * Calculates the corresponding cardBack of any given button by looking its ID.
     *
     * @param button the target button.
     * @return the CardBack corresponding to the given Button.
     */
    private CardBack getCardBackOfButton(Button button) {
        switch (button.getId()) {
            case "wizardButton1":
                return CardBack.WIZARD1;
            case "wizardButton2":
                return CardBack.WIZARD2;
            case "wizardButton3":
                return CardBack.WIZARD3;
            case "wizardButton4":
                return CardBack.WIZARD4;
            default:
                return null;
        }
    }

    /**
     * Handles the click of the Card Back Button. If the button is in the "Confirm!" state then it sends the SET message
     * to the server, otherwise it sends the UNSELECT message to the server.
     */
    public void handleCardBackButton() {
        if (cardBackButton.getText().equals("Confirm!")) {
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_CARDBACK)
                            .setCardBack(activeCardBack).serialize().toString()
            );

            resetWizardButtons();
            activeCardBack = null;
            setDisableWizardButtons(true);

            if(towerColorButton.getText().equals("Undo!")) {
                readyButton.setDisable(false);
            }

            cardBackButton.setText("Undo!");
        } else {
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.UNSEL_CARDBACK).serialize().toString()
            );

            setDisableWizardButtons(false);
            cardBackButton.setText("Confirm!");
            cardBackButton.setDisable(true);
            readyButton.setDisable(true);
        }
    }

    /**
     * Handles the click on the TowerColor Button. If the button is in the "Confirm!" state then it sends the SET message
     *      * to the server, otherwise it sends the UNSELECT message to the server.
     */
    public void handleTowerColorButton() {
        if (towerColorButton.getText().equals("Confirm!")) {
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_TOWERCOLOR)
                            .setTowerColor(activeTowerColor).serialize().toString()
            );

            activeTowerColor = null;
            setDisableTeamOptions(true);
            towerColorButton.setText("Undo!");

            if(cardBackButton.getText().equals("Undo!")) {
                readyButton.setDisable(false);
            }

        } else {
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.UNSEL_TOWERCOLOR).serialize().toString()
            );

            towerColorButton.setText("Confirm!");

            setDisableTeamOptions(false);

            if(team.getSelectedToggle() == null) {
                towerColorButton.setDisable(true);
            }

            readyButton.setDisable(true);
        }
    }

    /**
     * Handles the click on the Ready button.
     */
    public void handleReadyButton() {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.READY_UP).setReady(true)
                        .setIndex(GUI.getPlayerId()).serialize().toString()
        );

        readyButton.setDisable(true);
        towerColorButton.setDisable(true);
        cardBackButton.setDisable(true);
    }

    // CLEAN UP METHODS

    /**
     * Sets every wizard button to the disable state given as input.
     *
     * @param disable whether the buttons should be disabled or not.
     */
    private void setDisableWizardButtons(boolean disable) {
        for (Button button : wizardButtons){
            button.setDisable(disable);
        }
    }

    /**
     * Sets every tower option to the disable state given as input.
     *
     * @param disable whether the options should be disabled or not.
     */
    private void setDisableTeamOptions(boolean disable) {
        int i = 0;
        for (RadioButton button : teamOptions){
            button.setDisable(disable);
            towerOptions.get(i).setDisable(false);
            i++;
        }
    }

    /**
     * Resets the CSS style of every Wizard Button.
     */
    private void resetWizardButtons() {
        for (Button button : wizardButtons){
            button.setStyle("-fx-border-color: grey; -fx-border-width: 3px;");
        }
    }

    /**
     * Hides all the Ready checkboxes.
     */
    private void resetReadyCheckBoxes() {
        for (CheckBox checkBox : readyCheckBoxes){
            checkBox.setVisible(false);
        }
    }
}
