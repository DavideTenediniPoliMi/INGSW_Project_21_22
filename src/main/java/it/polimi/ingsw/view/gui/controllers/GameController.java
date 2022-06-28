package it.polimi.ingsw.view.gui.controllers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the controller of the scene handling the game.
 */
public class GameController extends FXController {
    @FXML
    private AnchorPane player1;
    @FXML
    private AnchorPane player2;
    @FXML
    private AnchorPane player3;
    @FXML
    private Label usernameText1;
    @FXML
    private Label usernameText2;
    @FXML
    private Label usernameText3;
    @FXML
    private ImageView tower1;
    @FXML
    private ImageView tower2;
    @FXML
    private ImageView tower3;
    @FXML
    private Label numTowers1;
    @FXML
    private Label numTowers2;
    @FXML
    private Label numTowers3;
    @FXML
    private AnchorPane coins1;
    @FXML
    private AnchorPane coins2;
    @FXML
    private AnchorPane coins3;
    @FXML
    private Label numCoins1;
    @FXML
    private Label numCoins2;
    @FXML
    private Label numCoins3;
    @FXML
    private GridPane entrance1;
    @FXML
    private GridPane entrance2;
    @FXML
    private GridPane entrance3;
    @FXML
    private GridPane diningRoom1;
    @FXML
    private GridPane diningRoom2;
    @FXML
    private GridPane diningRoom3;
    @FXML
    private ImageView assistantCard1;
    @FXML
    private ImageView assistantCard2;
    @FXML
    private ImageView assistantCard3;
    @FXML
    private Label usernameTextHero;
    @FXML
    private ImageView cardBackHero;
    @FXML
    private AnchorPane coinsHero;
    @FXML
    private Label numCoinsHero;
    @FXML
    private GridPane entranceHero;
    @FXML
    private GridPane diningRoomHero;
    @FXML
    private GridPane professorsHero;
    @FXML
    private GridPane towersHero;
    @FXML
    private ImageView selectedCardHero;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;
    @FXML
    private ImageView card5;
    @FXML
    private ImageView card6;
    @FXML
    private ImageView card7;
    @FXML
    private ImageView card8;
    @FXML
    private ImageView card9;
    @FXML
    private ImageView card10;
    @FXML
    private GridPane studentsIsland1;
    @FXML
    private GridPane studentsIsland2;
    @FXML
    private GridPane studentsIsland3;
    @FXML
    private GridPane studentsIsland4;
    @FXML
    private GridPane studentsIsland5;
    @FXML
    private GridPane studentsIsland6;
    @FXML
    private GridPane studentsIsland7;
    @FXML
    private GridPane studentsIsland8;
    @FXML
    private GridPane studentsIsland9;
    @FXML
    private GridPane studentsIsland10;
    @FXML
    private GridPane studentsIsland11;
    @FXML
    private GridPane studentsIsland12;
    @FXML
    private ImageView MNIsland1;
    @FXML
    private ImageView MNIsland2;
    @FXML
    private ImageView MNIsland3;
    @FXML
    private ImageView MNIsland4;
    @FXML
    private ImageView MNIsland5;
    @FXML
    private ImageView MNIsland6;
    @FXML
    private ImageView MNIsland7;
    @FXML
    private ImageView MNIsland8;
    @FXML
    private ImageView MNIsland9;
    @FXML
    private ImageView MNIsland10;
    @FXML
    private ImageView MNIsland11;
    @FXML
    private ImageView MNIsland12;
    @FXML
    private ImageView towerIsland1;
    @FXML
    private ImageView towerIsland2;
    @FXML
    private ImageView towerIsland3;
    @FXML
    private ImageView towerIsland4;
    @FXML
    private ImageView towerIsland5;
    @FXML
    private ImageView towerIsland6;
    @FXML
    private ImageView towerIsland7;
    @FXML
    private ImageView towerIsland8;
    @FXML
    private ImageView towerIsland9;
    @FXML
    private ImageView towerIsland10;
    @FXML
    private ImageView towerIsland11;
    @FXML
    private ImageView towerIsland12;
    @FXML
    private AnchorPane cloudPane1;
    @FXML
    private AnchorPane cloudPane2;
    @FXML
    private AnchorPane cloudPane3;
    @FXML
    private AnchorPane cloudPane4;
    @FXML
    private GridPane studentCloud1;
    @FXML
    private GridPane studentCloud2;
    @FXML
    private GridPane studentCloud3;
    @FXML
    private GridPane studentCloud4;
    @FXML
    private ImageView bridge1;
    @FXML
    private ImageView bridge2;
    @FXML
    private ImageView bridge3;
    @FXML
    private ImageView bridge4;
    @FXML
    private ImageView bridge5;
    @FXML
    private ImageView bridge6;
    @FXML
    private ImageView bridge7;
    @FXML
    private ImageView bridge8;
    @FXML
    private ImageView bridge9;
    @FXML
    private ImageView bridge10;
    @FXML
    private ImageView bridge11;
    @FXML
    private ImageView bridge12;
    @FXML
    private AnchorPane charCardPane1;
    @FXML
    private AnchorPane charCardPane2;
    @FXML
    private AnchorPane charCardPane3;
    @FXML
    private ImageView characterCard1;
    @FXML
    private ImageView characterCard2;
    @FXML
    private ImageView characterCard3;
    @FXML
    private GridPane charCardStudents1;
    @FXML
    private GridPane charCardStudents2;
    @FXML
    private GridPane charCardStudents3;
    @FXML
    private Label charCardCost1;
    @FXML
    private Label charCardCost2;
    @FXML
    private Label charCardCost3;
    @FXML
    private Label actionText;
    @FXML
    private AnchorPane boardCoins;
    @FXML
    private Label numCoinsLeft;
    @FXML
    private AnchorPane studentBag;
    @FXML
    private Button swapButton;

    private final List<AnchorPane> otherPlayers = new ArrayList<>();
    private final List<Label> otherUsernames = new ArrayList<>();
    private final List<ImageView> otherTowers = new ArrayList<>();
    private final List<Label> otherNumTowers = new ArrayList<>();
    private final List<Label> otherNumCoins = new ArrayList<>();
    private final List<GridPane> otherEntrance = new ArrayList<>();
    private final List<GridPane> otherDiningRoom = new ArrayList<>();
    private final List<AnchorPane> clouds = new ArrayList<>();
    private final List<GridPane> cloudStudents = new ArrayList<>();
    private final List<ImageView> cards = new ArrayList<>();
    private final List<GridPane> islandStudents = new ArrayList<>();
    private final List<ImageView> islandMN = new ArrayList<>();
    private final List<ImageView> islandTowers = new ArrayList<>();
    private final List<ImageView> bridges = new ArrayList<>();
    private final List<ImageView> charCards = new ArrayList<>();
    private final List<GridPane> charCardsStudents = new ArrayList<>();
    private final List<AnchorPane> charCardPanes = new ArrayList<>();
    private final List<Label> charCardsCost = new ArrayList<>();
    private final List<BorderPane> studentsEntranceHero = new ArrayList<>();
    private final List<ImageView> otherAssistants = new ArrayList<>();

    private GUIState state;
    private Color selectedColor;
    private int selectedCharCard;
    private StudentGroup selectedStudentsFromCard;
    private StudentGroup selectedStudentsFromEntrance;
    private StudentGroup selectedStudentsFromDiningRoom;

    //   INIT
    @FXML
    public void initialize() {
        Board board =  Game.getInstance().getBoard();
        MatchInfo matchInfo = MatchInfo.getInstance();

        prepArrays();

        if(matchInfo.getSelectedNumPlayer() >= 3) {
            player2.setVisible(true);
            otherPlayers.add(player2);
            otherUsernames.add(usernameText2);
            otherTowers.add(tower2);
            otherNumTowers.add(numTowers2);
            otherEntrance.add(entrance2);
            otherDiningRoom.add(diningRoom2);
            cloudPane3.setVisible(true);
            clouds.add(cloudPane3);
            cloudStudents.add(studentCloud3);
            otherAssistants.add(assistantCard2);
            assistantCard2.setUserData("");
            otherNumCoins.add(numCoins2);

            if(matchInfo.getSelectedNumPlayer() == 4) {
                player3.setVisible(true);
                otherPlayers.add(player3);
                otherUsernames.add(usernameText3);
                otherTowers.add(tower3);
                otherNumTowers.add(numTowers3);
                otherEntrance.add(entrance3);
                otherDiningRoom.add(diningRoom3);
                cloudPane4.setVisible(true);
                clouds.add(cloudPane4);
                cloudStudents.add(studentCloud4);
                otherAssistants.add(assistantCard3);
                assistantCard3.setUserData("");
                otherNumCoins.add(numCoins3);
            } else if(matchInfo.getSelectedNumPlayer() == 3){
                for(Node node : entranceHero.getChildren()) {
                    if(GridPane.getRowIndex(node) == 4) {
                        node.setVisible(true);
                    }
                }

                for(Node node : towersHero.getChildren()) {
                    if(GridPane.getRowIndex(node) == 3) {
                        node.setVisible(false);
                    }
                }
            }
        }

        if(matchInfo.isExpertMode()) {
            coins1.setVisible(true);

            if(matchInfo.getSelectedNumPlayer() >= 3) {
                coins2.setVisible(true);

                if(matchInfo.getSelectedNumPlayer() == 4) {
                    coins3.setVisible(true);
                }
            }

            charCardPane1.setVisible(true);
            charCardPane2.setVisible(true);
            charCardPane3.setVisible(true);

            charCardPanes.add(charCardPane1);
            charCardPanes.add(charCardPane2);
            charCardPanes.add(charCardPane3);

            charCards.add(characterCard1);
            charCards.add(characterCard2);
            charCards.add(characterCard3);

            charCardsStudents.add(charCardStudents1);
            charCardsStudents.add(charCardStudents2);
            charCardsStudents.add(charCardStudents3);

            charCardsCost.add(charCardCost1);
            charCardsCost.add(charCardCost2);
            charCardsCost.add(charCardCost3);

            boardCoins.setVisible(true);
            coinsHero.setVisible(true);

            List<CharacterCard> cards = Game.getInstance().getCharacterCards();

            for(int i = 0 ; i < cards.size(); i++) {
                charCards.get(i).setImage(CharacterCards.valueOf(cards.get(i).getName()).getImage());
                charCards.get(i).setUserData(i);

                if(cards.get(i).getEffectType().equals(EffectType.STUDENT_GROUP)) {
                    charCardsStudents.get(i).setVisible(true);
                    if(cards.get(i).getName().equals("POOL_SWAP")) {
                        getNodeFromCard(4, charCardsStudents.get(i)).setVisible(true);
                        getNodeFromCard(5, charCardsStudents.get(i)).setVisible(true);
                    }

                    int cardIndex = 0;
                    for(Color c : Color.values()) {
                        int numStud = ((StudentGroupDecorator)cards.get(i)).getStudentsByColor(c);
                        for(int j = 0; j < numStud; j++) {
                            BorderPane stud = (BorderPane) getNodeFromCard(cardIndex, charCardsStudents.get(i));
                            stud.setUserData(c);
                            ImageView image = (ImageView) stud.getCenter();
                            image.setImage(c.getImage());
                            cardIndex++;
                        }
                    }
                } else {
                    getNodeFromCard(4, charCardsStudents.get(i)).setVisible(true);

                    for(int j = 0; j < Color.NUM_COLORS ; j++) {
                        BorderPane stud = (BorderPane) getNodeFromCard(j, charCardsStudents.get(i));
                        stud.setUserData(Color.values()[j]);
                        ImageView image = (ImageView) stud.getCenter();
                        image.setImage(Color.values()[j].getImage());
                    }
                }

                charCardsCost.get(i).setText(String.valueOf(cards.get(i).getCost()));
            }
        }

        for(Color c : Color.values()) {
            for (int j = 0; j < 10; j++) {
                getNodeFromDiningHero(c, j).setUserData(c);
            }
        }

        initPlayers();

        islandMN.get(board.getMNPosition()).setVisible(true);
        numCoinsLeft.setText(String.valueOf(board.getNumCoinsLeft()));

        for(ImageView card : cards) {
            card.setOnMouseClicked(this::handleCardClick);
            card.setOnMouseClicked(null);
        }

        for(int i = 0; i < 12; i++) {
            islandStudents.get(i).setUserData(i);
        }

        for(int i = 0; i < clouds.size(); i++) {
            clouds.get(i).setUserData(i);
        }

        applyChangesSchools();
        applyChangesCharCards();
        applyChangesIslands();
        applyChangesClouds();
        studentBag.setVisible(Game.getInstance().isStudentBagEmpty());
        applyChangesProfessors();
        numCoinsLeft.setText(String.valueOf(Game.getInstance().getBoard().getNumCoinsLeft()));
        applyChangesCards();
        applyChangesPlayers();
        applyChangesCurrentPlayer();

        handleInteraction((matchInfo.getCurrentPlayerID() == GUI.getPlayerId()) ? getCurrentState() : GUIState.WAIT_ACTION);
    }

    /**
     * Returns the current GUIState corresponding to the current TurnState.
     *
     * @return the current GUIState.
     */
    private GUIState getCurrentState() {
        switch (MatchInfo.getInstance().getStateType()) {
            case PLANNING:
                return GUIState.PLANNING;
            case STUDENTS:
                return GUIState.MOVE_STUDENT;
            case MOTHER_NATURE:
                return GUIState.MOVE_MN;
            case CLOUD:
                return GUIState.CLOUD;
            default:
                return GUIState.WAIT_RESPONSE;
        }
    }

    /**
     * Populates all the arrays used with the different elements of the graphic in the predetermined ordered.
     */
    private void prepArrays() {
        otherPlayers.add(player1);
        otherUsernames.add(usernameText1);
        otherTowers.add(tower1);
        otherNumTowers.add(numTowers1);
        otherEntrance.add(entrance1);
        otherDiningRoom.add(diningRoom1);
        otherAssistants.add(assistantCard1);
        assistantCard1.setUserData("");
        otherNumCoins.add(numCoins1);

        clouds.add(cloudPane1);
        clouds.add(cloudPane2);
        cloudStudents.add(studentCloud1);
        cloudStudents.add(studentCloud2);

        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
        cards.add(card6);
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        cards.add(card10);

        islandStudents.add(studentsIsland1);
        islandStudents.add(studentsIsland2);
        islandStudents.add(studentsIsland3);
        islandStudents.add(studentsIsland4);
        islandStudents.add(studentsIsland5);
        islandStudents.add(studentsIsland6);
        islandStudents.add(studentsIsland7);
        islandStudents.add(studentsIsland8);
        islandStudents.add(studentsIsland9);
        islandStudents.add(studentsIsland10);
        islandStudents.add(studentsIsland11);
        islandStudents.add(studentsIsland12);

        islandMN.add(MNIsland1);
        islandMN.add(MNIsland2);
        islandMN.add(MNIsland3);
        islandMN.add(MNIsland4);
        islandMN.add(MNIsland5);
        islandMN.add(MNIsland6);
        islandMN.add(MNIsland7);
        islandMN.add(MNIsland8);
        islandMN.add(MNIsland9);
        islandMN.add(MNIsland10);
        islandMN.add(MNIsland11);
        islandMN.add(MNIsland12);

        islandTowers.add(towerIsland1);
        islandTowers.add(towerIsland2);
        islandTowers.add(towerIsland3);
        islandTowers.add(towerIsland4);
        islandTowers.add(towerIsland5);
        islandTowers.add(towerIsland6);
        islandTowers.add(towerIsland7);
        islandTowers.add(towerIsland8);
        islandTowers.add(towerIsland9);
        islandTowers.add(towerIsland10);
        islandTowers.add(towerIsland11);
        islandTowers.add(towerIsland12);

        bridges.add(bridge1);
        bridges.add(bridge2);
        bridges.add(bridge3);
        bridges.add(bridge4);
        bridges.add(bridge5);
        bridges.add(bridge6);
        bridges.add(bridge7);
        bridges.add(bridge8);
        bridges.add(bridge9);
        bridges.add(bridge10);
        bridges.add(bridge11);
        bridges.add(bridge12);

        for(Node node : entranceHero.getChildren()) {
            studentsEntranceHero.add((BorderPane)node);
        }
    }

    /**
     * Initializes all the graphic elements of the Hero player and every other player in this game.
     * The behaviour changes based on the type and size of game.
     */
    private void initPlayers() {
        Game game = Game.getInstance();
        List<Player> players = game.getPlayers();
        int otherPlayersIndex = 0;

        for(Player player : players) {
            School school = game.getBoard().getSchoolByPlayerID(player.getID());

            if(player.getID() == GUI.getPlayerId()) {
                usernameTextHero.setText(player.getName());
                cardBackHero.setImage(player.getCardBack().getImage());

                int entranceIndex = 0;

                for(Color c : Color.values()) {
                    int amount = school.getNumStudentsInEntranceByColor(c);

                    for(int i = 0; i < amount; i++) {
                        ImageView image = (ImageView) studentsEntranceHero.get(entranceIndex).getCenter();
                        image.setImage(c.getImage());
                        entranceIndex++;
                    }
                }

                for(Node tower : towersHero.getChildren()) {
                    ImageView towerView = (ImageView) tower;

                    towerView.setImage(player.getTeamColor().getImage());

                    if(!player.isTowerHolder()) {
                        tower.setVisible(false);
                    }
                }
            } else {
                otherUsernames.get(otherPlayersIndex).setText(player.getName());
                otherTowers.get(otherPlayersIndex).setImage(player.getTeamColor().getImage());
                otherNumTowers.get(otherPlayersIndex).setText(String.valueOf(school.getNumTowers()));

                for(Color c : Color.values()) {
                    Label numStud = (Label) getNodeFromSchoolEntrance(c, otherEntrance.get(otherPlayersIndex));
                    numStud.setText(String.valueOf(school.getNumStudentsInEntranceByColor(c)));
                }

                otherPlayersIndex++;
            }
        }
    }

    // APPLY CHANGES
    @Override
    public void applyChanges(JsonObject jsonObject) {
        if(jsonObject.has("schools")) {
            applyChangesSchools();
        }

        if(jsonObject.has("characterCards")) {
            applyChangesCharCards();
        }

        if(jsonObject.has("clouds")) {
            applyChangesClouds();
        }

        if(jsonObject.has("islands")) {
            applyChangesIslands();
        }

        if(jsonObject.has("bagEmpty")) {
            studentBag.setVisible(Game.getInstance().isStudentBagEmpty());
        }

        if(jsonObject.has("professors")) {
            applyChangesProfessors();
        }

        if(jsonObject.has("coinsLeft")) {
            numCoinsLeft.setText(String.valueOf(Game.getInstance().getBoard().getNumCoinsLeft()));
        }

        if(jsonObject.has("cards")) {
            applyChangesCards();
        }
        
        if(jsonObject.has("players")) {
            applyChangesPlayers();
        }

        if(jsonObject.has("matchInfo")) {
            applyChangesCurrentPlayer();
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Schools changes.
     */
    private void applyChangesSchools() {
        List<School> schools = Game.getInstance().getBoard().getSchools();
        int otherIndex = 0;

        for(School school : schools) {
            if(school.getOwner().getID() == GUI.getPlayerId()) {
                int entranceIndex = 0;

                for(Color c : Color.values()) {
                    int amount = school.getNumStudentsInEntranceByColor(c);

                    for(int i = 0; i < amount; i++) {
                        studentsEntranceHero.get(entranceIndex).setVisible(true);
                        ImageView image = (ImageView) studentsEntranceHero.get(entranceIndex).getCenter();
                        image.setImage(c.getImage());
                        studentsEntranceHero.get(entranceIndex).setUserData(c);
                        entranceIndex++;
                    }
                }

                for(int i = entranceIndex; i < MatchInfo.getInstance().getInitialNumStudents(); i++) {
                    studentsEntranceHero.get(i).setVisible(false);
                }


                for(Color c : Color.values()) {
                    for (int j = 0; j < 10; j++) {
                        BorderPane stud = (BorderPane) getNodeFromDiningHero(c, j);
                        stud.setVisible(j < school.getNumStudentsInDiningRoomByColor(c));
                    }
                }

                int j = 0;
                for(Node tower : towersHero.getChildren()) {
                    ImageView towerView = (ImageView) tower;
                    towerView.setVisible(j < school.getNumTowers());
                    j++;
                }
            } else {
                for(Color c : Color.values()) {
                    Label numTextEntrance = (Label) getNodeFromSchoolEntrance(c, otherEntrance.get(otherIndex));
                    numTextEntrance.setText(String.valueOf(school.getNumStudentsInEntranceByColor(c)));
                    Label numTextDining = (Label) getNodeFromSchoolDining(c, otherDiningRoom.get(otherIndex));
                    numTextDining.setText(String.valueOf(school.getNumStudentsInDiningRoomByColor(c)));
                }

                otherNumTowers.get(otherIndex).setText(String.valueOf(school.getNumTowers()));

                otherIndex++;
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Character Cards changes.
     */
    private void applyChangesCharCards() {
        List<CharacterCard> charCards = Game.getInstance().getCharacterCards();

        for(int i = 0; i < charCards.size(); i++) {
            charCardsCost.get(i).setText(String.valueOf(charCards.get(i).getCost()));

            charCardPanes.get(i).setDisable(Game.getInstance().getActiveCharacterCard() != null
                    && !Game.getInstance().getActiveCharacterCard().equals(charCards.get(i)));

            if(charCards.get(i).getEffectType().equals(EffectType.STUDENT_GROUP)) {
                int cardIndex = 0;
                for(Color c : Color.values()) {
                    int numStud = ((StudentGroupDecorator)charCards.get(i)).getStudentsByColor(c);
                    for(int j = 0; j < numStud; j++) {
                        BorderPane stud = (BorderPane) getNodeFromCard(cardIndex, charCardsStudents.get(i));
                        stud.setUserData(c);
                        ImageView image = (ImageView) stud.getCenter();
                        image.setImage(c.getImage());
                        cardIndex++;
                    }
                }
            } else if(Game.getInstance().getActiveCharacterCard() == null) {
                charCardsStudents.get(i).setVisible(false);
                for(int j = 0 ; j < Color.NUM_COLORS; j++) {
                    Node n = getNodeFromCard(j, charCardsStudents.get(i));
                    n.getStyleClass().clear();
                    n.getStyleClass().add("def");
                }
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Clouds changes.
     */
    private void applyChangesClouds() {
        List<Cloud> cloudsList = Game.getInstance().getBoard().getClouds();

        for(int i = 0; i < cloudsList.size(); i++) {
            clouds.get(i).setDisable(!cloudsList.get(i).isAvailable());

            for(Color c : Color.values()) {
                Label numStud = (Label) getNodeFromCloud(c, cloudStudents.get(i));
                numStud.setText(String.valueOf(cloudsList.get(i).getStudents().getByColor(c)));
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Island changes.
     */
    private void applyChangesIslands() {
        for(int i = 0; i < 12; i++) {
            islandMN.get(i).setVisible(false);
            islandTowers.get(i).setVisible(false);
        }

        List<Boolean> bridgeList = Game.getInstance().getBoard().findBridges();

        for(int i = 0; i < bridges.size() ; i ++){
            bridges.get(i).setVisible(bridgeList.get(i));
        }

        for(int i = 0; i < 12; i++) {
            Island island = Game.getInstance().getBoard().getIslandOfAbsoluteIndexForGraphics(i);

            islandMN.get(i).setVisible(island.isMotherNatureOnIsland());
            islandTowers.get(i).setVisible(island.getTeamColor() != null);

            if(island.getTeamColor() != null) {
                islandTowers.get(i).setImage(island.getTeamColor().getImage());
            }

            for(Color c : Color.values()) {
                Label numStud = (Label) getNodeFromIsland(c, islandStudents.get(i));
                numStud.setText(String.valueOf(island.getNumStudentsByColor(c)));
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Professors changes.
     */
    private void applyChangesProfessors() {
        ProfessorTracker profs = Game.getInstance().getBoard().getProfessorOwners();

        for(GridPane otherProf : otherDiningRoom) {
            for(Color c: Color.values()) {
                getProfFromOtherSchool(c, otherProf).setVisible(false);
            }
        }

        for(Color c: Color.values()) {
            getProfFromHeroSchool(c).setVisible(false);
        }

        for(Color c : Color.values()) {
            int ownerID = profs.getOwnerIDByColor(c);
            if(ownerID == -1) continue;

            if(ownerID == GUI.getPlayerId()) {
                getProfFromHeroSchool(c).setVisible(true);
            } else {
                getProfFromOtherSchool(c, otherDiningRoom.get(getAdjustedOtherPlayerIndex(ownerID))).setVisible(true);
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Cards changes.
     */
    private void applyChangesCards() {
        Player hero = Game.getInstance().getPlayerByID(GUI.getPlayerId());

        for(int i = 0; i < cards.size(); i++) {
            ImageView card = cards.get(i);

            if(Card.values()[i].isUsed() || !hero.getPlayableCards().contains(Card.values()[i])) {
                card.setImage(Card.values()[i].getImageBW());
            } else {
                card.setImage(Card.values()[i].getImage());
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Players changes.
     */
    private void applyChangesPlayers() {
        int otherIndex = 0;
        for(Player player: Game.getInstance().getPlayers()) {
            if(player.getID() == GUI.getPlayerId()) {
                applyChangesCards();

                if(player.getSelectedCard() != null && !player.getSelectedCard().equals(Card.CARD_AFK)) {
                    selectedCardHero.setVisible(true);
                    selectedCardHero.setImage(player.getSelectedCard().getImage());
                }

                numCoinsHero.setText(String.valueOf(player.getNumCoins()));
            } else {
                otherUsernames.get(otherIndex).setDisable(!player.isConnected());
                otherPlayers.get(otherIndex).setDisable(!player.isConnected());

                if(player.getSelectedCard() != null) {
                    otherAssistants.get(otherIndex).setVisible(true);
                    otherAssistants.get(otherIndex).setImage(player.getSelectedCard().getImageHalf());
                    if(player.getSelectedCard().equals(Card.CARD_AFK)){
                        otherAssistants.get(otherIndex).setUserData("AFK");
                    }
                } else if("AFK".equals(otherAssistants.get(otherIndex).getUserData().toString())) {
                    otherAssistants.get(otherIndex).setUserData("");
                    otherAssistants.get(otherIndex).setVisible(false);
                }
                otherNumCoins.get(otherIndex).setText(String.valueOf(player.getNumCoins()));
                otherIndex++;
            }
        }
    }

    /**
     * Changes various elements of the graphic to match the changes after a message regarding Current Player changes.
     */
    private void applyChangesCurrentPlayer() {
        for(Player p : Game.getInstance().getPlayers()) {
            if(p.getID() == GUI.getPlayerId()) continue;

            int otherIndex = getAdjustedOtherPlayerIndex(p.getID());
            if(otherIndex < 0 || otherIndex >= otherPlayers.size()) continue;

            otherPlayers.get(otherIndex).getStyleClass().clear();
            otherPlayers.get(otherIndex).getStyleClass().add(
                    (MatchInfo.getInstance().getCurrentPlayerID() == p.getID()) ? "target" : "def"
            );
        }
    }

    // GETTER FOR GRIDPANES

    /**
     * Returns the position of the graphic elements regarding the player with the specified ID inside the various
     * lists of graphic elements.
     *
     * @param ownerID the ID of the player.
     * @return the position of the player's elements.
     */
    private int getAdjustedOtherPlayerIndex(int ownerID) {
        Game game = Game.getInstance();
        int targetId = game.getIndexOfPlayer(game.getPlayerByID(ownerID));
        return (game.getIndexOfPlayer(game.getPlayerByID(GUI.getPlayerId())) < targetId) ? targetId - 1 : targetId;
    }

    /**
     * Returns the Node corresponding to the Hero's professor of the color specified.
     *
     * @param c the color of the professor wanted.
     * @return the node containing the BorderPane of the professor.
     */
    private Node getProfFromHeroSchool(Color c) {
        for(Node n : professorsHero.getChildren()) {
            switch (c) {
                case RED:
                    if(GridPane.getRowIndex(n) == 1)
                        return n;
                    break;
                case GREEN:
                    if(GridPane.getRowIndex(n) == 0)
                        return n;
                    break;
                case YELLOW:
                    if(GridPane.getRowIndex(n) == 2)
                        return n;
                    break;
                case PINK:
                    if(GridPane.getRowIndex(n) == 3)
                        return n;
                    break;
                case BLUE:
                    if(GridPane.getRowIndex(n) == 4)
                        return n;
                    break;
            }
        }

        return new BorderPane();
    }

    /**
     * Returns the Node corresponding to the Hero's student in the dining room of the color and position specified.
     *
     * @param c the color of the student wanted.
     * @param i the position of the student wanted, from 0 to 9.
     * @return the node containing the BorderPane of the student.
     */
    private Node getNodeFromDiningHero(Color c, int i) {
        for(Node n : diningRoomHero.getChildren()) {
            switch (c) {
                case RED:
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == i)
                        return n;
                    break;
                case GREEN:
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == i)
                        return n;
                    break;
                case YELLOW:
                    if(GridPane.getRowIndex(n) == 2 && GridPane.getColumnIndex(n) == i)
                        return n;
                    break;
                case PINK:
                    if(GridPane.getRowIndex(n) == 3 && GridPane.getColumnIndex(n) == i)
                        return n;
                    break;
                case BLUE:
                    if(GridPane.getRowIndex(n) == 4 && GridPane.getColumnIndex(n) == i)
                        return n;
                    break;
            }
        }

        return new BorderPane();
    }

    /**
     * Returns the Node of the professor of the specified color from the grid of a player other than the Hero.
     *
     * @param c the color of the professor wanted.
     * @param grid the other player's dining room.
     * @return the ImageView containing the Professor.
     */
    private Node getProfFromOtherSchool(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && GridPane.getRowIndex(n) == 0)
                return n;
        }

        return new ImageView();
    }

    /**
     * Returns the Node of the label for the students of the specified color from the entrance of a player other than the Hero.
     *
     * @param c the color of the students wanted.
     * @param grid the other player's entrance.
     * @return the Label containing the count.
     */
    private Node getNodeFromSchoolEntrance(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && n instanceof Label)
                return n;
        }

        return new Label();
    }

    /**
     * Returns the Node of the label for the students of the specified color from the dining room of a player other than the Hero.
     *
     * @param c the color of the students wanted.
     * @param grid the other player's dining room.
     * @return the Label containing the count.
     */
    private Node getNodeFromSchoolDining(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && GridPane.getRowIndex(n) == 1)
                return n;
        }

        return new Label();
    }

    /**
     * Returns the Node of the label for the students of the specified color from the cloud.
     *
     * @param c the color of the students wanted.
     * @param grid the cloud.
     * @return the Label containing the count.
     */
    private Node getNodeFromCloud(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(!(n instanceof Label)) continue;

            switch (c) {
                case RED:
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 1)
                        return n;
                    break;
                case GREEN:
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case YELLOW:
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
                case PINK:
                    if(GridPane.getRowIndex(n) == 2 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case BLUE:
                    if(GridPane.getRowIndex(n) == 2 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
            }
        }

        return new Label();
    }

    /**
     * Returns the Node of the label for the students of the specified color from the island.
     *
     * @param c the color of the students wanted.
     * @param grid the island.
     * @return the Label containing the count.
     */
    private Node getNodeFromIsland(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(!(n instanceof Label)) continue;

            switch (c) {
                case RED:
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
                case GREEN:
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case YELLOW:
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 4)
                        return n;
                    break;
                case PINK:
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 1)
                        return n;
                    break;
                case BLUE:
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 3)
                        return n;
                    break;
            }
        }

        return new Label();
    }

    /**
     * Returns the Node of the label for the students of the specified index from the card.
     *
     * @param i the index of the students wanted.
     * @param grid the card.
     * @return the BorderPane containing the count.
     */
    private Node getNodeFromCard(int i, GridPane grid) {
        for(Node n : grid.getChildren()) {
            switch (i) {
                case 0 :
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case 1 :
                    if(GridPane.getRowIndex(n) == 0 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
                case 2 :
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case 3 :
                    if(GridPane.getRowIndex(n) == 1 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
                case 4 :
                    if(GridPane.getRowIndex(n) == 2 && GridPane.getColumnIndex(n) == 0)
                        return n;
                    break;
                case 5 :
                    if(GridPane.getRowIndex(n) == 2 && GridPane.getColumnIndex(n) == 2)
                        return n;
                    break;
            }
        }

        return new BorderPane();
    }

    // HANDLE INTERACTIONS
    @Override
    public void handleInteraction(GUIState newState) {
        Game game = Game.getInstance();
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(newState == GUIState.DISCONNECTION) {
            return;
        }

        if(!GUIState.REPEAT_ACTION.equals(newState)) {
            state = newState;
        }

        disableGraphic();

        switch (state) {
            case WAIT_RESPONSE:
                actionText.setText("Waiting for a server response");
                break;
            case WAIT_ACTION:
                Player player = game.getPlayerByID(MatchInfo.getInstance().getCurrentPlayerID());
                actionText.setText("It's " + player.getName()  + "'s turn!");
                break;
            case PLANNING:
                actionText.setText("Chose an Assistant Card!");
                Player hero = Game.getInstance().getPlayerByID(GUI.getPlayerId());

                for(int i = 0; i < cards.size(); i++) {
                    ImageView card = cards.get(i);

                    if((!Card.values()[i].isUsed() || hero.getPlayableCards().size() == 1)
                            && hero.getPlayableCards().contains(Card.values()[i])) {
                        card.setOnMouseClicked(this::handleCardClick);
                        card.getStyleClass().add("assistantCard");
                    }
                }
                break;
            case MOVE_STUDENT:
                actionText.setText("Select a Student from your Entrance!");

                prepCharCards();

                entranceHero.getStyleClass().clear();
                entranceHero.getStyleClass().add("target");

                for(BorderPane stud : studentsEntranceHero) {
                    stud.setOnMouseClicked(this::handleEntranceSelect);
                }
                break;
            case PAUSE:
                StringBuilder message = new StringBuilder("Game is paused");

                if(MatchInfo.getInstance().getNumPlayersConnected() == 1)
                    message.append(" and ending in 1 minute");
                actionText.setText(message + "!");
                break;
            case MOVE_MN:
                actionText.setText("Select an Island to move MN!");

                prepCharCards();

                for(int i = 0; i < 12; i++) {
                    int islandAbsoluteIndex = game.getBoard().getOriginalIndexOf(i);
                    int islandIndex = game.getBoard().getIndexOfMultiIslandContainingIslandOfIndex(islandAbsoluteIndex);
                    int MNPosition = game.getBoard().getMNPosition();
                    int steps = game.getPlayerByID(matchInfo.getCurrentPlayerID()).getSelectedCard().RANGE;

                    if(islandIndex > MNPosition) {
                        if(islandIndex - MNPosition > steps) continue;
                    } else {
                        if(game.getBoard().getNumIslands() + islandIndex - MNPosition > steps) continue;
                    }

                    islandStudents.get(i).getStyleClass().clear();
                    islandStudents.get(i).getStyleClass().add("target");
                    islandStudents.get(i).setOnMouseClicked(this::handleIslandSelectionWhileMoveMN);
                }
                break;
            case CLOUD:
                actionText.setText("Pick a Cloud!");

                List<Cloud> cloudList = game.getBoard().getClouds();
                for(int i = 0; i < cloudList.size(); i++) {
                    if(cloudList.get(i).isAvailable()) {
                        clouds.get(i).getStyleClass().clear();
                        clouds.get(i).getStyleClass().add("cloud");
                        clouds.get(i).getStyleClass().add("target");
                        clouds.get(i).setOnMouseClicked(this::handleCloudSelection);
                    }
                }
                break;
            case SET_CARD_PARAMS:
                manageSetParams();
                break;
            case END_GAME:
                Alert winnerAlert = new Alert(Alert.AlertType.INFORMATION);
                winnerAlert.setTitle("The Game is OVER!");
                winnerAlert.setHeaderText("Thanks for playing!");

                message = new StringBuilder("The game");
                if(matchInfo.isGameTied()) {
                    message.append(" ended in a TIE between :\n");
                } else {
                    message.append(" was WON by :\n");
                }

                for(Player p: game.getPlayers()) {
                    if(matchInfo.getWinners().contains(p.getTeamColor())) {
                        message.append(p.getName()).append(" (TEAM ").append(p.getTeamColor()).append(")\n");
                    }
                }

                winnerAlert.setContentText(message.toString());

                winnerAlert.showAndWait();
                System.exit(0);
                break;
        }
    }

    /**
     * Changes various graphic elements to accommodate the user of the cards.
     */
    private void prepCharCards() {
        Game game = Game.getInstance();

        if(!MatchInfo.getInstance().isExpertMode() || Game.getInstance().getActiveCharacterCard() != null)
            return;

        List<CharacterCard> charCardList = game.getCharacterCards();

        for(int i = 0 ; i < charCardList.size() ; i++) {
            if(game.getPlayerByID(GUI.getPlayerId()).getNumCoins() >= charCardList.get(i).getCost()) {
                if(charCardList.get(i).getName().equals("EXCHANGE_STUDENTS")) {
                    int studInEntrance = 0;
                    School school = game.getBoard().getSchoolByPlayerID(GUI.getPlayerId());

                    for(Color c : Color.values()) {
                        studInEntrance += school.getNumStudentsInDiningRoomByColor(c);
                    }

                    if(studInEntrance == 0) {
                        charCards.get(i).setOnMouseClicked(this::handleCharCardNoStudents);
                        continue;
                    }
                }
                charCards.get(i).setOnMouseClicked(this::handleCharCardSelect);
            } else {
                charCards.get(i).setOnMouseClicked(this::handleCharCardNoMoney);
            }
        }
    }

    /**
     * Handles the interaction of setting the parameters of all the cards.
     */
    private void manageSetParams() {
        GridPane students = charCardsStudents.get(selectedCharCard);
        switch (Game.getInstance().getActiveCharacterCard().getName()) {
            case "IGNORE_COLOR":
            case "RETURN_TO_BAG":
                students.setVisible(true);
                students.getStyleClass().clear();
                students.getStyleClass().add("target");

                actionText.setText("Select a Color from the Card!");

                for(int i = 0 ; i < Color.NUM_COLORS; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(this::handleCardStudentSelection);
                }
                break;
            case "MOVE_TO_DINING_ROOM":
            case "MOVE_TO_ISLAND":
                students.getStyleClass().clear();
                students.getStyleClass().add("target");

                actionText.setText("Select a Student from the Card!");

                for(int i = 0 ; i < 4; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(this::handleCardStudentSelection);
                }
                break;
            case "POOL_SWAP":
                students.getStyleClass().clear();
                students.getStyleClass().add("target");
                selectedStudentsFromCard = new StudentGroup();
                selectedStudentsFromEntrance = new StudentGroup();

                actionText.setText("Select Students from the Card and Entrance!");

                for(int i = 0 ; i < 6; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(this::handleCardStudentSelection);
                }

                entranceHero.getStyleClass().clear();
                entranceHero.getStyleClass().add("target");

                for(BorderPane stud : studentsEntranceHero) {
                    stud.setOnMouseClicked(this::handleEntranceSelectWhileSettingParams);
                }

                swapButton.setVisible(true);
                break;
            case "EXCHANGE_STUDENTS":
                selectedStudentsFromEntrance = new StudentGroup();
                selectedStudentsFromDiningRoom = new StudentGroup();

                actionText.setText("Select Students from Entrance and Dining Room!");

                entranceHero.getStyleClass().clear();
                entranceHero.getStyleClass().add("target");

                for(BorderPane stud : studentsEntranceHero) {
                    stud.setOnMouseClicked(this::handleEntranceSelectWhileSettingParams);
                }

                diningRoomHero.getStyleClass().clear();
                diningRoomHero.getStyleClass().add("target");

                for(Node stud : diningRoomHero.getChildren()) {
                    stud.setOnMouseClicked(this::handleDiningSelectionWhileSettingParams);
                }

                swapButton.setVisible(true);
                break;
        }
    }

    // EVENT LISTENERS

    /**
     * Handles the selection of the Assistant cards.
     *
     * @param event the event fired by the click of the card.
     */
    private void handleCardClick(MouseEvent event) {
        ImageView card = (ImageView) event.getSource();
        String id = card.getId();
        String numText = id.substring(4);

        int num;
        try {
            num = Integer.parseInt(numText);
        } catch (NumberFormatException e) {
            showError("Invalid choice! Try again!");
            return;
        }

        disableGraphic();
        actionText.setText("Waiting for a server response");

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.PLAY_CARD)
                        .setIndex(num - 1)
                        .serialize().toString()
        );
    }

    /**
     * Handles the selection of a student in the Entrance of the Hero during the Move students state. After the
     * selection the user will be prompted to selection the destination of such student.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleEntranceSelect(MouseEvent mouseEvent) {
        BorderPane stud = (BorderPane) mouseEvent.getSource();
        selectedColor = (Color) stud.getUserData();

        entranceHero.getStyleClass().clear();
        entranceHero.getStyleClass().add("def");

        for(BorderPane node : studentsEntranceHero) {
            if(!stud.equals(node)) {
                node.setOnMouseClicked(null);
            } else {
                node.setOnMouseClicked(this::handleEntranceDeselect);
                node.getStyleClass().clear();
                node.getStyleClass().add("target");
            }
        }

        String optional = "";
        if(Game.getInstance().getBoard().getSchoolByPlayerID(GUI.getPlayerId()).getNumStudentsInDiningRoomByColor(selectedColor) < 10) {
            diningRoomHero.getStyleClass().clear();
            diningRoomHero.getStyleClass().add("target");
            diningRoomHero.setOnMouseClicked(this::handleDiningSelection);
            optional += "or the Dining Room";
        }

        for(GridPane island : islandStudents) {
            island.getStyleClass().clear();
            island.getStyleClass().add("target");
            island.setOnMouseClicked(this::handleIslandSelectionWhileMoveStudent);
        }

        actionText.setText("Select an Island " + optional + "!");
    }

    /**
     * Handles the deselection of the selected student from the Entrance of the Hero during the Move student state.
     * The user will be prompted to selecting a student again.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleEntranceDeselect(MouseEvent mouseEvent) {
        selectedColor = null;

        handleInteraction(GUIState.MOVE_STUDENT);
    }

    /**
     * Handles the selection of a student from the Entrance of the Hero during the Set Params state.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleEntranceSelectWhileSettingParams(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        Color c = (Color) target.getUserData();

        int maxNumStud;
        StudentGroup targetSG;

        if (Game.getInstance().getActiveCharacterCard().getName().equals("POOL_SWAP")) {
            maxNumStud = 3;
            targetSG = selectedStudentsFromCard;
        } else {
            maxNumStud = 2;
            targetSG = selectedStudentsFromDiningRoom;
        }

        target.getStyleClass().clear();
        target.getStyleClass().add("target");
        target.setOnMouseClicked(this::handleEntranceDeselectWhileSettingParams);

        selectedStudentsFromEntrance.addByColor(c, 1);

        if(selectedStudentsFromEntrance.getTotalAmount() == maxNumStud) {
            for(BorderPane stud : studentsEntranceHero) {
                if(!stud.getStyleClass().contains("target")) {
                    stud.setDisable(true);
                }
            }
        }

        swapButton.setDisable(targetSG.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                || selectedStudentsFromEntrance.getTotalAmount() == 0);
    }

    /**
     * Handles the deselection of a student from the Entrance of the Hero during the Set Params state.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleEntranceDeselectWhileSettingParams(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        Color c = (Color) target.getUserData();

        int maxNumStud;
        StudentGroup targetSG;

        if (Game.getInstance().getActiveCharacterCard().getName().equals("POOL_SWAP")) {
            maxNumStud = 3;
            targetSG = selectedStudentsFromCard;
        } else {
            maxNumStud = 2;
            targetSG = selectedStudentsFromDiningRoom;
        }

        target.getStyleClass().clear();
        target.getStyleClass().add("def");
        target.setOnMouseClicked(this::handleEntranceSelectWhileSettingParams);

        selectedStudentsFromEntrance.removeByColor(c, 1);

        if(selectedStudentsFromEntrance.getTotalAmount() == maxNumStud - 1) {
            for(BorderPane stud : studentsEntranceHero) {
                if(!stud.getStyleClass().contains("target")) {
                    stud.setDisable(false);
                }
            }
        }

        swapButton.setDisable(targetSG.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                || selectedStudentsFromEntrance.getTotalAmount() == 0);
    }

    /**
     * Handles the selection of a student in the Dining Room of the Hero during the Move students state.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleDiningSelection(MouseEvent mouseEvent) {
        disableGraphic();
        actionText.setText("Waiting for a server response");

        notify(
                new RequestParameters().setCommandType(CommandType.TRANSFER_STUDENT_TO_DINING_ROOM)
                        .setColor(selectedColor).serialize().toString()
        );
    }

    /**
     * Handles the selection of a student from the Dining Room of the Hero during the Set Params state.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleDiningSelectionWhileSettingParams(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        Color color = (Color) target.getUserData();

        target.getStyleClass().clear();
        target.getStyleClass().add("target");
        target.setOnMouseClicked(this::handleDiningDeselectionWhileSettingParams);

        selectedStudentsFromDiningRoom.addByColor(color, 1);

        if(selectedStudentsFromDiningRoom.getTotalAmount() == 2) {
            for(Color c : Color.values()) {
                for(int i = 0 ; i < 10 ; i++) {
                    Node n = getNodeFromDiningHero(c, i);
                    if (!n.getStyleClass().contains("target")) {
                        n.setDisable(true);
                    }
                }
            }
        }

        swapButton.setDisable(selectedStudentsFromDiningRoom.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                || selectedStudentsFromDiningRoom.getTotalAmount() == 0);
    }

    /**
     * Handles the deselection of a student from the Dining Room of the Hero during the Set Params state.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleDiningDeselectionWhileSettingParams(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        Color color = (Color) target.getUserData();

        target.getStyleClass().clear();
        target.getStyleClass().add("def");
        target.setOnMouseClicked(this::handleDiningSelectionWhileSettingParams);

        selectedStudentsFromDiningRoom.removeByColor(color, 1);

        if(selectedStudentsFromDiningRoom.getTotalAmount() == 1) {
            for(Color c : Color.values()) {
                for(int i = 0 ; i < 10 ; i++) {
                    Node n = getNodeFromDiningHero(c, i);
                    if (!n.getStyleClass().contains("target")) {
                        n.setDisable(false);
                    }
                }
            }
        }

        swapButton.setDisable(selectedStudentsFromDiningRoom.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                || selectedStudentsFromDiningRoom.getTotalAmount() == 0);
    }

    /**
     * Handles the selection of an Island during the Move MN state.
     *
     * @param mouseEvent the event fired by the click of the island.
     */
    private void handleIslandSelectionWhileMoveMN(MouseEvent mouseEvent) {
        GridPane island = (GridPane) mouseEvent.getSource();
        Board board = Game.getInstance().getBoard();

        int islandAbsoluteIndex = board.getOriginalIndexOf(Integer.parseInt(island.getUserData().toString()));
        int islandIndex = board.getIndexOfMultiIslandContainingIslandOfIndex(islandAbsoluteIndex);

        disableGraphic();
        actionText.setText("Waiting for a server response");

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.MOVE_MN)
                        .setIndex(islandIndex)
                        .serialize().toString()
        );
    }

    /**
     * Handles the selection of an Island during the Move student state.
     *
     * @param mouseEvent the event fired by the click of the island.
     */
    private void handleIslandSelectionWhileMoveStudent(MouseEvent mouseEvent) {
        GridPane island = (GridPane) mouseEvent.getSource();

        disableGraphic();
        actionText.setText("Waiting for a server response");

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.TRANSFER_STUDENT_TO_ISLAND)
                        .setIndex(Integer.parseInt(island.getUserData().toString()))
                        .setColor(selectedColor)
                        .serialize().toString()
        );
    }

    /**
     * Handles the selection of an Island during the Set Params state.
     *
     * @param mouseEvent the event fired by the click of the island.
     */
    private void handleIslandSelectionWhileSettingParams(MouseEvent mouseEvent) {
        GridPane island = (GridPane) mouseEvent.getSource();
        Board board = Game.getInstance().getBoard();
        GridPane students = charCardsStudents.get(selectedCharCard);

        int islandIndex = board.getOriginalIndexOf(Integer.parseInt(island.getUserData().toString()));

        disableGraphic();
        actionText.setText("Waiting for a server response");

        notifyCardParameters(
                new CardParameters().setFromOrigin(new StudentGroup(selectedColor,1))
                        .setPlayerID(GUI.getPlayerId())
                        .setIslandIndex(islandIndex)
                        .setFromDestination(new StudentGroup()));  //not needed

        for(int i = 0 ; i < 4; i++) {
            getNodeFromCard(i, students).getStyleClass().clear();
            getNodeFromCard(i, students).getStyleClass().add("def");
        }

        selectedColor = null;
        notifyCardActivation();
    }

    /**
     * Handles the selection of a cloud during the Cloud State.
     *
     * @param mouseEvent the event fired by the click of the cloud.
     */
    private void handleCloudSelection(MouseEvent mouseEvent) {
        AnchorPane cloud = (AnchorPane) mouseEvent.getSource();

        disableGraphic();
        actionText.setText("Waiting for a server response");

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.COLLECT_FROM_CLOUD)
                        .setIndex(Integer.parseInt(cloud.getUserData().toString()))
                        .serialize().toString()
        );
    }

    /**
     * Handles the click on an available CharacterCard during Move student and MN states.
     *
     * @param mouseEvent the event fired by the click of the card.
     */
    private void handleCharCardSelect(MouseEvent mouseEvent) {
        ImageView card = (ImageView) mouseEvent.getSource();

        selectedCharCard = Integer.parseInt(card.getUserData().toString());
        disableCharCards();

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.BUY_CHARACTER_CARD)
                        .setIndex(selectedCharCard)
                        .serialize().toString()
        );

        if(Game.getInstance().getCharacterCards().get(selectedCharCard).getName().equals("INFLUENCE_ADD_TWO")) {
            CardParameters cardParameters = new CardParameters()
                    .setBoostedTeam(Game.getInstance().getPlayerByID(GUI.getPlayerId()).getTeamColor())
                    .setSelectedColor(Color.RED);   //not needed

            notifyCardParameters(cardParameters);
        }
    }

    /**
     * Handles the click on EXCHANGE STUDENTS CharacterCard when the Hero does not have enough students in their Dining Room.
     *
     * @param mouseEvent the event fired by the click of the card.
     */
    private void handleCharCardNoStudents(MouseEvent mouseEvent) {
        showError("Not enough Students in Dining Room!");
    }

    /**
     * Handles the click on a CharacterCard when the Hero does not have enough money to buy it.
     *
     * @param mouseEvent the event fired by the click of the card.
     */
    private void handleCharCardNoMoney(MouseEvent mouseEvent) {
        showError("Not enough Coins!");
    }

    /**
     * Handles the click on a CharacterCard during any state other than Move students and MN.
     *
     * @param mouseEvent the event fired by the click of the card.
     */
    private void handleCharCardForbidden(MouseEvent mouseEvent) {
        showError("Cannot buy a card at this stage!");
    }

    /**
     * Handles the selection of a student from the card while setting the card parameters.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleCardStudentSelection(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        GridPane students = charCardsStudents.get(selectedCharCard);
        Color c = (Color) target.getUserData();

        switch (Game.getInstance().getActiveCharacterCard().getName()) {
            case "IGNORE_COLOR":
                target.getStyleClass().clear();
                target.getStyleClass().add("target");

                for(int i = 0 ; i < Color.NUM_COLORS; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(null);
                }

                students.getStyleClass().clear();
                students.getStyleClass().add("def");

                notifyCardParameters(new CardParameters().setSelectedColor(c)
                        .setBoostedTeam(TowerColor.BLACK)); // not needed
                break;
            case "RETURN_TO_BAG":
                target.getStyleClass().clear();
                target.getStyleClass().add("target");

                for(int i = 0 ; i < Color.NUM_COLORS; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(null);
                }

                students.getStyleClass().clear();
                students.getStyleClass().add("def");

                actionText.setText("Waiting for a server response");
                notifyCardParameters(new CardParameters().setSelectedColor(c));
                notifyCardActivation();
                break;
            case "MOVE_TO_DINING_ROOM":
                for(int i = 0 ; i < 4; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(null);
                }

                students.getStyleClass().clear();
                students.getStyleClass().add("def");

                actionText.setText("Waiting for a server response");
                notifyCardParameters(new CardParameters().setFromOrigin(new StudentGroup(c,1))
                        .setPlayerID(GUI.getPlayerId())
                        .setIslandIndex(0)      //not needed
                        .setFromDestination(new StudentGroup()));    //not needed
                notifyCardActivation();
                break;
            case "MOVE_TO_ISLAND":
                target.getStyleClass().clear();
                target.getStyleClass().add("target");

                for(int i = 0 ; i < 4; i++) {
                    getNodeFromCard(i, students).setOnMouseClicked(null);
                }

                students.getStyleClass().clear();
                students.getStyleClass().add("def");

                actionText.setText("Select the Destination Island!");

                for(GridPane stud : islandStudents) {
                    stud.setOnMouseClicked(this::handleIslandSelectionWhileSettingParams);
                    stud.getStyleClass().clear();
                    stud.getStyleClass().add("target");
                }

                selectedColor = c;
                break;
            case "POOL_SWAP":
                target.getStyleClass().clear();
                target.getStyleClass().add("target");
                target.setOnMouseClicked(this::handleCardStudentDeselection);

                selectedStudentsFromCard.addByColor(c, 1);

                if(selectedStudentsFromCard.getTotalAmount() == 3) {
                    for(int i = 0 ; i < 6; i++) {
                        Node n = getNodeFromCard(i,  students);
                        if(!n.getStyleClass().contains("target")) {
                            n.setDisable(true);
                        }
                    }
                }

                swapButton.setDisable(selectedStudentsFromCard.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                        || selectedStudentsFromCard.getTotalAmount() == 0);
                break;
        }
    }

    /**
     * Handles the deselection of a student from the card while setting the card parameters.
     *
     * @param mouseEvent the event fired by the click of the student.
     */
    private void handleCardStudentDeselection(MouseEvent mouseEvent) {
        BorderPane target = (BorderPane) mouseEvent.getSource();
        Color c = (Color) target.getUserData();

        target.getStyleClass().clear();
        target.getStyleClass().add("def");
        target.setOnMouseClicked(this::handleCardStudentSelection);

        selectedStudentsFromCard.removeByColor(c, 1);

        if(selectedStudentsFromCard.getTotalAmount() == 2) {
            for(int i = 0 ; i < 6; i++) {
                Node n = getNodeFromCard(i,  charCardsStudents.get(selectedCharCard));
                if(!n.getStyleClass().contains("target")) {
                    n.setDisable(false);
                }
            }
        }

        swapButton.setDisable(selectedStudentsFromCard.getTotalAmount() != selectedStudentsFromEntrance.getTotalAmount()
                || selectedStudentsFromCard.getTotalAmount() == 0);
    }

    /**
     * Handles the press of the Swap Button after having set the card parameters and activates the card bought.
     * This button is only used for SWAP POOL and EXCHANGE STUDENTS cards.
     */
    public void handleSwapButton() {
        charCardsStudents.get(selectedCharCard).getStyleClass().clear();
        charCardsStudents.get(selectedCharCard).getStyleClass().add("def");

        for(int i = 0 ; i < 6; i++) {
            Node n = getNodeFromCard(i, charCardsStudents.get(selectedCharCard));
            n.getStyleClass().clear();
            n.getStyleClass().add("def");
            n.setDisable(false);
            n.setOnMouseClicked(null);
        }

        entranceHero.getStyleClass().clear();
        entranceHero.getStyleClass().add("def");

        for(BorderPane node : studentsEntranceHero) {
            node.setDisable(false);
            node.setOnMouseClicked(null);
            node.getStyleClass().clear();
            node.getStyleClass().add("def");
        }

        diningRoomHero.getStyleClass().clear();
        diningRoomHero.getStyleClass().add("def");

        for(Color c : Color.values()) {
            for(int i = 0 ; i < 10 ; i++) {
                Node n = getNodeFromDiningHero(c, i);
                n.setDisable(false);
                n.setOnMouseClicked(null);
                n.getStyleClass().clear();
                n.getStyleClass().add("def");
            }
        }

        swapButton.setDisable(true);
        swapButton.setVisible(false);

        if(Game.getInstance().getActiveCharacterCard().getName().equals("POOL_SWAP")) {
            notifyCardParameters(new CardParameters().setFromOrigin(selectedStudentsFromCard)
                    .setPlayerID(GUI.getPlayerId())
                    .setFromDestination(selectedStudentsFromEntrance)
                    .setIslandIndex(0));     //not needed
        } else {
            notifyCardParameters(new CardParameters().setFromOrigin(selectedStudentsFromEntrance)
                    .setPlayerID(GUI.getPlayerId())
                    .setFromDestination(selectedStudentsFromDiningRoom));
        }

        notifyCardActivation();
    }

    // CARD UTILS

    /**
     * Sends the parameters to the server.
     *
     * @param cardParams the parameters to be sent.
     */
    private void notifyCardParameters(CardParameters cardParams) {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.SET_CARD_PARAMETERS)
                        .setIndex(selectedCharCard)
                        .setCardParams(cardParams)
                        .serialize().toString()
        );
    }

    /**
     * Sends a ACTIVATE CARD command to the server.
     */
    private void notifyCardActivation() {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.ACTIVATE_CARD)
                        .serialize().toString()
        );
    }

    // CLEAN UP METHODS

    /**
     * Resets the state of various graphic elements in order to show a neutral state in between state changes.
     */
    private void disableGraphic() {
        entranceHero.getStyleClass().clear();
        entranceHero.getStyleClass().add("def");

        for(ImageView card : cards) {
            card.setOnMouseClicked(null);
            card.getStyleClass().clear();
        }

        for(BorderPane stud : studentsEntranceHero) {
            stud.setOnMouseClicked(null);
            stud.getStyleClass().clear();
            stud.getStyleClass().add("def");
        }

        diningRoomHero.getStyleClass().clear();
        diningRoomHero.getStyleClass().add("def");
        diningRoomHero.setOnMouseClicked(null);

        for(GridPane island : islandStudents) {
            island.getStyleClass().clear();
            island.getStyleClass().add("def");
            island.setOnMouseClicked(null);
        }

        for(AnchorPane cloud : clouds) {
            cloud.getStyleClass().clear();
            cloud.getStyleClass().add("cloud");
            cloud.getStyleClass().add("def");
            cloud.setOnMouseClicked(null);
        }

        disableCharCards();

        actionText.setText("");
    }

    /**
     * Disables the character cards when a Cloud state or SetParams state begins.
     */
    private void disableCharCards() {
        if(!MatchInfo.getInstance().isExpertMode())
            return;

        for (ImageView charCard : charCards) {
            charCard.setOnMouseClicked(this::handleCharCardForbidden);
        }
    }
}
