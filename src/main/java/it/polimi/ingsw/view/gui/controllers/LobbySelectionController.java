package it.polimi.ingsw.view.gui.controllers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class LobbySelectionController extends FXController {
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
    private RadioButton blackOption;
    @FXML
    private RadioButton whiteOption;
    @FXML
    private RadioButton greyOption;
    @FXML
    private ImageView greyTower;
    @FXML
    private Button cardBackButton;
    @FXML
    private ToggleGroup team;

    private CardBack activeCardBack;

    private final List<Label> usernames = new ArrayList<>();
    private final List<ImageView> images = new ArrayList<>();
    private final List<ImageView> towers = new ArrayList<>();
    private final List<Button> wizardButtons = new ArrayList<>();
    private final List<RadioButton> teamOptions = new ArrayList<>();

    @FXML
    public void initialize() {
        Lobby lobby = Lobby.getLobby();
        MatchInfo matchInfo = MatchInfo.getInstance();

        prepArrays();

        maxPlayers.setText(String.valueOf(matchInfo.getSelectedNumPlayer()));

        for(int i = 0; i < 4; i ++) {
            if(i >= matchInfo.getSelectedNumPlayer())
                images.get(i).setStyle("-fx-opacity: 0.45;");
            //TODO improve visibility (possibly change opacity to a grey out version)
        }

        if(matchInfo.getSelectedNumPlayer() != 3) {
            greyOption.setVisible(false);
            greyTower.setVisible(false);
        }

        applyChanges();
    }

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
    }

    @Override
    public void applyChanges() {
        Image defaultImage = new Image("/images/Personaggi_retro.jpg");
        Lobby lobby = Lobby.getLobby();
        MatchInfo matchInfo = MatchInfo.getInstance();
        List<Player> players = lobby.getPlayers();

        List<TowerColor> pickedTeams = new ArrayList<>();

        playersConnected.setText(String.valueOf(lobby.getPlayers().size()));

        if(cardBackButton.getText().equals("Confirm!")) {
            setDisableWizardButtons(false);
        }

        for(RadioButton option : teamOptions) {
            option.setDisable(false);
        }

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
                } else {
                    if(pickedTeams.contains(player.getTeamColor())) {
                        teamOptions.get(player.getTeamColor().ordinal()).setDisable(true);
                        teamOptions.get(player.getTeamColor().ordinal()).setSelected(false);
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
        }

    }

    public void handleWizardButtons(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        activeCardBack = getCardBackOfButton(button);

        resetWizardButtons();

        button.setStyle("-fx-border-color: yellow; -fx-border-width: 5px;");
        cardBackButton.setDisable(false);
    }

    private void setDisableWizardButtons(boolean disable) {
        for (Button button : wizardButtons){
            button.setDisable(disable);
        }
    }

    private void resetWizardButtons() {
        for (Button button : wizardButtons){
            button.setStyle("-fx-border-color: grey; -fx-border-width: 3px;");
        }
    }

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

            cardBackButton.setText("Undo!");
        } else {
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.UNSEL_CARDBACK).serialize().toString()
            );

            setDisableWizardButtons(false);
            cardBackButton.setText("Confirm!");
            cardBackButton.setDisable(true);
        }
    }
}
