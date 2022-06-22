package it.polimi.ingsw.view.gui.controllers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Label> charCardsCost = new ArrayList<>();
    private final List<ImageView> studentsEntranceHero = new ArrayList<>();
    private final List<ImageView> otherAssistants = new ArrayList<>();

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

            otherNumCoins.add(numCoins1);

            if(matchInfo.getSelectedNumPlayer() >= 3) {
                otherNumCoins.add(numCoins2);
                coins2.setVisible(true);

                if(matchInfo.getSelectedNumPlayer() == 4) {
                    otherNumCoins.add(numCoins3);
                    coins3.setVisible(true);
                }
            }

            charCardPane1.setVisible(true);
            charCardPane2.setVisible(true);
            charCardPane3.setVisible(true);

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
                            ImageView stud = (ImageView) getNodeFromCard(cardIndex, charCardsStudents.get(i));
                            stud.setImage(c.getImage());
                            cardIndex++;
                        }
                    }
                }
                charCardsCost.get(i).setText(String.valueOf(cards.get(i).getCost()));
            }
        }

        initPlayers();

        applyChangesClouds();

        islandMN.get(board.getMNPosition()).setVisible(true);
        numCoinsLeft.setText(String.valueOf(board.getNumCoinsLeft()));
    }

    private void prepArrays() {
        otherPlayers.add(player1);
        otherUsernames.add(usernameText1);
        otherTowers.add(tower1);
        otherNumTowers.add(numTowers1);
        otherEntrance.add(entrance1);
        otherDiningRoom.add(diningRoom1);
        otherAssistants.add(assistantCard1);

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
            studentsEntranceHero.add((ImageView)node);
        }
    }

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
                        studentsEntranceHero.get(entranceIndex).setImage(c.getImage());
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
    }

    private void applyChangesPlayers() {
        int otherIndex = 0;
        for(Player player: Game.getInstance().getPlayers()) {
            if(player.getID() == GUI.getPlayerId()) {
                applyChangesCards();

                if(player.getSelectedCard() != null && !player.getSelectedCard().equals(Card.CARD_AFK)) {
                    selectedCardHero.setImage(player.getSelectedCard().getImage());
                }

                numCoinsHero.setText(String.valueOf(player.getNumCoins()));
            } else {
                otherPlayers.get(otherIndex).setDisable(!player.isConnected());
                otherUsernames.get(otherIndex).setDisable(!player.isConnected());

                otherAssistants.get(otherIndex).setImage(player.getSelectedCard().getImageHalf());
                otherNumCoins.get(otherIndex).setText(String.valueOf(player.getNumCoins()));
                otherIndex++;
            }
        }
    }

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

    private void applyChangesProfessors() {
        ProfessorTracker profs = Game.getInstance().getBoard().getProfessorOwners();

        resetProfs();

        for(Color c : Color.values()) {
            int ownerID = profs.getOwnerIDByColor(c);

            if(ownerID == GUI.getPlayerId()) {
                getProfFromHeroSchool(c, professorsHero).setVisible(true);
            } else {
                getProfFromOtherSchool(c, otherDiningRoom.get(getAdjustedOtherPlayerIndex(ownerID))).setVisible(true);
            }
        }
    }

    private int getAdjustedOtherPlayerIndex(int ownerID) {
        Game game = Game.getInstance();
        int targetId = game.getIndexOfPlayer(game.getPlayerByID(ownerID));
        return (game.getIndexOfPlayer(game.getPlayerByID(GUI.getPlayerId())) < targetId) ? targetId - 1 : targetId;
    }

    private void resetProfs() {
        for(GridPane otherProf : otherDiningRoom) {
            for(Color c: Color.values()) {
                getProfFromOtherSchool(c, otherProf).setVisible(false);
            }
        }

        for(Color c: Color.values()) {
            getProfFromHeroSchool(c, professorsHero).setVisible(false);
        }
    }

    private void applyChangesIslands() {
        resetIslands();

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

    private void resetIslands() {
        for(int i = 0; i < 12; i++) {
            islandMN.get(i).setVisible(false);
            islandTowers.get(i).setVisible(false);
        }
    }

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

    private void applyChangesCharCards() {
        List<CharacterCard> charCards = Game.getInstance().getCharacterCards();

        for(int i = 0; i < charCards.size(); i++) {
            charCardsCost.get(i).setText(String.valueOf(charCards.get(i).getCost()));

            this.charCards.get(i).setDisable(Game.getInstance().getActiveCharacterCard() != null
                    && !Game.getInstance().getActiveCharacterCard().equals(charCards.get(i)));

            if(charCards.get(i).getEffectType().equals(EffectType.STUDENT_GROUP)) {
                int cardIndex = 0;
                for(Color c : Color.values()) {
                    int numStud = ((StudentGroupDecorator)charCards.get(i)).getStudentsByColor(c);
                    for(int j = 0; j < numStud; j++) {
                        ImageView stud = (ImageView) getNodeFromCard(cardIndex, charCardsStudents.get(i));
                        stud.setImage(c.getImage());
                        cardIndex++;
                    }
                }
            }
        }
    }

    private void applyChangesSchools() {
        List<School> schools = Game.getInstance().getBoard().getSchools();
        int otherIndex = 0;

        for(School school : schools) {
            if(school.getOwner().getID() == GUI.getPlayerId()) {
                int entranceIndex = 0;

                for(Color c : Color.values()) {
                    int amount = school.getNumStudentsInEntranceByColor(c);

                    for(int i = 0; i < amount; i++) {
                        studentsEntranceHero.get(entranceIndex).setImage(c.getImage());
                        entranceIndex++;
                    }
                }

                for(Color c : Color.values()) {
                    for (int j = 0; j < 10; j++) {
                        ImageView stud = (ImageView) getNodeFromDiningHero(c, j, diningRoomHero);
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
                    numTextEntrance.setText(String.valueOf(schools.get(otherIndex).getNumStudentsInEntranceByColor(c)));
                    Label numTextDining = (Label) getNodeFromSchoolDining(c, otherDiningRoom.get(otherIndex));
                    numTextDining.setText(String.valueOf(schools.get(otherIndex).getNumStudentsInDiningRoomByColor(c)));
                }

                otherNumTowers.get(otherIndex).setText(String.valueOf(school.getNumTowers()));

                otherIndex++;
            }
        }
    }

    private Node getProfFromHeroSchool(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getRowIndex(n) == c.ordinal())
                return n;
        }

        return new ImageView();
    }

    private Node getNodeFromDiningHero(Color c, int i, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getRowIndex(n) == c.ordinal() && GridPane.getColumnIndex(n) == i)
                return n;
        }

        return new ImageView();
    }

    private Node getProfFromOtherSchool(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && GridPane.getRowIndex(n) == 0)
                return n;
        }

        return new ImageView();
    }

    private Node getNodeFromSchoolEntrance(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && n instanceof Label)
                return n;
        }

        return new Label();
    }

    private Node getNodeFromSchoolDining(Color c, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == c.ordinal() && GridPane.getRowIndex(n) == 1)
                return n;
        }

        return new Label();
    }

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

    private Node getNodeFromCard(int i, GridPane grid) {
        for(Node n : grid.getChildren()) {
            if(GridPane.getColumnIndex(n) == i)
                return n;
        }

        return new ImageView();
    }
}
