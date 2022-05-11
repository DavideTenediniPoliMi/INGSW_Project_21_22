package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.PlanningStateController;
import it.polimi.ingsw.controller.round.RoundStateController;
import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomExpertController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.controller.subcontrollers.IslandExpertController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.game.GameNotStartedException;
import it.polimi.ingsw.exceptions.game.GamePausedException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.game.NotCurrentPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.commands.*;
import it.polimi.ingsw.network.parameters.CardParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController gameController;
    Game game;
    Board board;
    Lobby lobby;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        game = Game.getInstance();
        board = game.getBoard();
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();

        lobby.addPlayer(0, "a");
        lobby.addPlayer(1, "b");
        lobby.selectTeam(0, TowerColor.BLACK);
        lobby.selectTeam(1, TowerColor.WHITE);
        lobby.selectCardBack(0, CardBack.CB_1);
        lobby.selectCardBack(1, CardBack.CB_2);

        matchInfo.setUpGame(2,true);
        matchInfo.setNumPlayersConnected(2);

        gameController.createGame();
    }

    @AfterEach
    void tearDown() {
        gameController = null;
        Game.resetInstance();
        MatchInfo.resetInstance();
        Lobby.resetLobby();
    }

    @Test
    void gameNotStartedTest() {
        matchInfo.setGameStatus(GameStatus.LOBBY);
        Command command = new PlayCardCommand(0, gameController);
        assertThrowsExactly(GameNotStartedException.class, () -> gameController.requestCommand(matchInfo.getCurrentPlayerID(), command));
    }

    @Test
    void wrongPlayerTest() {
        int wrongPlayerID = matchInfo.getCurrentPlayerID() == 0 ? 1 : 0;
        Command command = new PlayCardCommand(0, gameController);

        assertThrowsExactly(NotCurrentPlayerException.class, () -> gameController.requestCommand(wrongPlayerID, command));
    }

    @Test
    void playCardTest() throws EriantysException {
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        assertFalse(game.getPlayerByID(playerID).getPlayableCards().contains(Card.CARD_1));
    }

    @Test
    void transferToIslandTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToIslandCommand(0, Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 1));
        int blueStudentsBefore = board.getIslandAt(0).getNumStudentsByColor(Color.BLUE);

        gameController.requestCommand(playerID, command);

        assertEquals(blueStudentsBefore + 1, board.getIslandAt(0).getNumStudentsByColor(Color.BLUE));
    }

    @Test
    void transferToDiningTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 1));

        gameController.requestCommand(playerID, command);

        assertEquals(1, board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.BLUE));
    }

    @Test
    void moveMNTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        assertAll(
                () -> assertEquals((MNPositionBefore + 2) % board.getNumIslands(), board.getMNPosition()),
                () -> assertFalse(board.getIslandAt(MNPositionBefore).isMotherNatureOnIsland())
        );
    }

    @Test
    void collectFromCloudTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        StudentGroup cloudStudentsBefore = board.getClouds().get(0).getStudents();
        StudentGroup entranceBefore = new StudentGroup();
        for(Color c : Color.values()) {
            entranceBefore.addByColor(c, board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(c));
        }

        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        for(Color c : Color.values()) {
            int entranceColor = entranceBefore.getByColor(c);
            int cloudColor = cloudStudentsBefore.getByColor(c);
            int finalPlayerID = playerID;
            assertAll(
                    () -> assertEquals(0, board.getClouds().get(0).getStudents().getByColor(c)),
                    () -> assertEquals(entranceColor + cloudColor, board.getSchoolByPlayerID(finalPlayerID).getNumStudentsInEntranceByColor(c))
            );
        }
    }

    @Test
    void buyCharacterCardTest() throws EriantysException {
        Game.resetInstance();
        MatchInfo.resetInstance();
        board = null;

        gameController = new GameController();
        game = Game.getInstance();
        matchInfo = MatchInfo.getInstance();
        board = game.getBoard();

        matchInfo.setUpGame(2,true);
        matchInfo.setNumPlayersConnected(2);

        for(Player player : lobby.getPlayers()) {
            game.addPlayer(player);
            game.addSchool(player.getID());
            game.giveStudentsTo(player.getID(), matchInfo.getInitialNumStudents());
            game.addTowersTo(player.getID(), matchInfo.getMaxTowers());
            if(matchInfo.isExpertMode()) {
                game.giveCoinToPlayer(player.getID());
            }
        }

        game.placeMNAt(new Random().nextInt(game.getBoard().getNumIslands()));

        StudentBag islandBag = new StudentBag(2);
        int MNPosition = game.getBoard().getMNPosition();
        for(int i = 0; i < 12; i++){
            if(i != MNPosition && i != (MNPosition + 6) % game.getBoard().getNumIslands())
                game.addInitialStudentToIsland(i, islandBag.drawStudents(1));
        }

        game.createClouds(game.getPlayers().size());

        if(matchInfo.isExpertMode()) {
            game.instantiateCharacterCard(1);
            game.instantiateCharacterCard(2);
            game.instantiateCharacterCard(7);
        }

        matchInfo.setStateType(TurnState.PLANNING);

        RoundStateController roundStateController = new RoundStateController(new IslandExpertController(new CharacterCardController())
                , new DiningRoomExpertController());
        gameController.setState(new PlanningStateController(roundStateController));

        roundStateController.defineFirstPlayOrder();
        matchInfo.setGameStatus(GameStatus.IN_GAME);

        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //BUY CHARACTER CARD PHASE
        command = new BuyCharacterCardCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        assertTrue(game.getCharacterCards().get(0).isActive());
    }

    @Test
    void setCardParametersTest() throws EriantysException {
        Game.resetInstance();
        MatchInfo.resetInstance();
        board = null;

        gameController = new GameController();
        game = Game.getInstance();
        matchInfo = MatchInfo.getInstance();
        board = game.getBoard();

        matchInfo.setUpGame(2,true);
        matchInfo.setNumPlayersConnected(2);

        for(Player player : lobby.getPlayers()) {
            game.addPlayer(player);
            game.addSchool(player.getID());
            game.giveStudentsTo(player.getID(), matchInfo.getInitialNumStudents());
            game.addTowersTo(player.getID(), matchInfo.getMaxTowers());
            if(matchInfo.isExpertMode()) {
                game.giveCoinToPlayer(player.getID());
            }
        }

        game.placeMNAt(new Random().nextInt(game.getBoard().getNumIslands()));

        StudentBag islandBag = new StudentBag(2);
        int MNPosition = game.getBoard().getMNPosition();
        for(int i = 0; i < 12; i++){
            if(i != MNPosition && i != (MNPosition + 6) % game.getBoard().getNumIslands())
                game.addInitialStudentToIsland(i, islandBag.drawStudents(1));
        }

        game.createClouds(game.getPlayers().size());

        if(matchInfo.isExpertMode()) {
            game.instantiateCharacterCard(1);
            game.instantiateCharacterCard(2);
            game.instantiateCharacterCard(7);
        }

        matchInfo.setStateType(TurnState.PLANNING);

        RoundStateController roundStateController = new RoundStateController(new IslandExpertController(new CharacterCardController())
                , new DiningRoomExpertController());
        gameController.setState(new PlanningStateController(roundStateController));

        roundStateController.defineFirstPlayOrder();
        matchInfo.setGameStatus(GameStatus.IN_GAME);

        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //BUY CHARACTER CARD PHASE
        command = new BuyCharacterCardCommand(2, gameController);

        gameController.requestCommand(playerID, command);

        //SET PARAMETERS PHASE
        CardParameters cardParams = new CardParameters().setSelectedColor(Color.BLUE);
        command = new SetCardParametersCommand(2, cardParams, gameController);

        int finalPlayerID = playerID;
        Command finalCommand = command;
        assertDoesNotThrow(() -> gameController.requestCommand(finalPlayerID, finalCommand));
    }

    @Test
    void activateCharacterCardTest() throws EriantysException {
        Game.resetInstance();
        MatchInfo.resetInstance();
        board = null;

        gameController = new GameController();
        game = Game.getInstance();
        matchInfo = MatchInfo.getInstance();
        board = game.getBoard();

        matchInfo.setUpGame(2,true);
        matchInfo.setNumPlayersConnected(2);

        for(Player player : lobby.getPlayers()) {
            game.addPlayer(player);
            game.addSchool(player.getID());
            game.giveStudentsTo(player.getID(), matchInfo.getInitialNumStudents());
            game.addTowersTo(player.getID(), matchInfo.getMaxTowers());
            if(matchInfo.isExpertMode()) {
                game.giveCoinToPlayer(player.getID());
            }
        }

        game.placeMNAt(new Random().nextInt(game.getBoard().getNumIslands()));

        StudentBag islandBag = new StudentBag(2);
        int MNPosition = game.getBoard().getMNPosition();
        for(int i = 0; i < 12; i++){
            if(i != MNPosition && i != (MNPosition + 6) % game.getBoard().getNumIslands())
                game.addInitialStudentToIsland(i, islandBag.drawStudents(1));
        }

        game.createClouds(game.getPlayers().size());

        if(matchInfo.isExpertMode()) {
            game.instantiateCharacterCard(1);
            game.instantiateCharacterCard(2);
            game.instantiateCharacterCard(7);
        }

        matchInfo.setStateType(TurnState.PLANNING);

        RoundStateController roundStateController = new RoundStateController(new IslandExpertController(new CharacterCardController())
                , new DiningRoomExpertController());
        gameController.setState(new PlanningStateController(roundStateController));

        roundStateController.defineFirstPlayOrder();
        matchInfo.setGameStatus(GameStatus.IN_GAME);

        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(1);
        game.giveCoinToPlayer(1);
        game.giveCoinToPlayer(1);
        game.giveCoinToPlayer(1);

        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //BUY CHARACTER CARD PHASE
        command = new BuyCharacterCardCommand(0, gameController);

        gameController.requestCommand(playerID, command);

        //SET PARAMETERS PHASE
        StudentGroupDecorator card = (StudentGroupDecorator) game.getCharacterCards().get(0);
        StudentGroup cardStudents = new StudentGroup();
        for(Color c : Color.values()) {
            cardStudents.addByColor(c, card.getStudentsByColor(c));
        }
        CardParameters cardParams = new CardParameters().setFromOrigin(cardStudents)
                .setPlayerID(playerID)
                .setFromDestination(new StudentGroup())
                .setIslandIndex(0);
        command = new SetCardParametersCommand(0, cardParams, gameController);

        gameController.requestCommand(playerID, command);

        //ACTIVATE CARD
        command = new ActivateCharacterCardCommand(gameController);
        gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);



        //SECOND PLAYER TURN
        //STUDENT PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //BUY CHARACTER CARD PHASE
        command = new BuyCharacterCardCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        //SET PARAMETERS PHASE
        card = (StudentGroupDecorator) game.getCharacterCards().get(1);
        cardStudents = new StudentGroup();
        StudentGroup entranceStudents = new StudentGroup();
        for(Color c : Color.values()) {
            cardStudents.addByColor(c, card.getStudentsByColor(c));
            entranceStudents.addByColor(c, board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(c));
        }
        StudentGroup fromOrigin = new StudentGroup();
        int j = 0;
        for(Color c : Color.values()) {
            int studentsColor = cardStudents.getByColor(c);
            for (int z = 0; z < studentsColor; z++) {
                fromOrigin.addByColor(c, 1);
                j++;
            }

            if(j>=2)
                break;
        }
        StudentGroup fromDestination = new StudentGroup();
        j = 0;
        for(Color c : Color.values()) {
            int studentsColor = entranceStudents.getByColor(c);
            for (int z = 0; z < studentsColor; z++) {
                fromDestination.addByColor(c, 1);
                j++;
            }
            if(j>=2)
                break;
        }

        cardParams = new CardParameters().setFromOrigin(fromOrigin)
                .setPlayerID(playerID)
                .setFromDestination(fromDestination)
                .setIslandIndex(0);
        command = new SetCardParametersCommand(1, cardParams, gameController);

        gameController.requestCommand(playerID, command);

        //ACTIVATE CARD
        command = new ActivateCharacterCardCommand(gameController);
        gameController.requestCommand(playerID, command);

        //MN PHASE 2
        MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 2
        command = new CollectCloudCommand(1, gameController);

        gameController.requestCommand(playerID, command);


        //SECOND ROUND


        //PLANNING PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(9, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(8, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //BUY CHARACTER CARD PHASE
        command = new BuyCharacterCardCommand(2, gameController);

        gameController.requestCommand(playerID, command);

        //SET PARAMETERS PHASE
        cardParams = new CardParameters().setSelectedColor(Color.BLUE);
        command = new SetCardParametersCommand(2, cardParams, gameController);

        gameController.requestCommand(playerID, command);

        //ACTIVATE CARD
        int[] blueStudentsBefore = new int[5];
        int cont = 0;
        for(Player p : game.getPlayers()) {
            blueStudentsBefore[cont] = board.getSchoolByPlayerID(p.getID())
                    .getNumStudentsInDiningRoomByColor(Color.BLUE);
            cont++;
        }
        command = new ActivateCharacterCardCommand(gameController);
        gameController.requestCommand(playerID, command);

        cont = 0;
        for(Player p : game.getPlayers()) {
            assertEquals(Math.max(blueStudentsBefore[cont] - 3, 0), board.getSchoolByPlayerID(p.getID())
                    .getNumStudentsInDiningRoomByColor(Color.BLUE));
            cont++;
        }
    }

    @Test
    void checkWinnerForTowersTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        //checkEndConditionAfterMN
        game.removeTowersFrom(playerID, 8);

        gameController.requestCommand(playerID, command);

        assertEquals(game.getPlayerByID(playerID).getTeamColor(), matchInfo.getWinners().get(0));
    }

    @Test
    void checkWinnerForIslandTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        //checkEndConditionAfterMN
        game.removeTowersFrom(playerID, 1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);
        game.mergeIslands(0,1);

        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 1, gameController);

        gameController.requestCommand(playerID, command);

        assertEquals(game.getPlayerByID(playerID).getTeamColor(), matchInfo.getWinners().get(0));
    }

    @Test
    void checkWinnerForNoRemainingCards() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);


        //STUDENT PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.RED, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.RED, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 2
        MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 2
        for(Card c : Card.values()) {
            game.playCard(0, c);
        }
        game.removeTowersFrom(0, 3);

        command = new CollectCloudCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        assertEquals(game.getPlayerByID(0).getTeamColor(), matchInfo.getWinners().get(0));
    }

    @Test
    void checkWinnerForNoRemainingStudents() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);


        //STUDENT PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.RED, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.RED, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 2
        MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 2
        game.drawStudents(110);
        game.removeTowersFrom(0, 3);

        command = new CollectCloudCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        assertEquals(game.getPlayerByID(0).getTeamColor(), matchInfo.getWinners().get(0));
    }

    @Test
    void checkWinnerByProfessorsTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);


        //STUDENT PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.RED, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.RED, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 2
        MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 2
        game.drawStudents(110);
        game.giveProfessorTo(0, Color.BLUE);
        game.giveProfessorTo(0, Color.RED);
        game.giveProfessorTo(0, Color.GREEN);

        int numTowers = board.getSchoolByPlayerID(0).getNumTowers();
        if(numTowers < 8)
            game.addTowersTo(0, 8-numTowers);
        numTowers = board.getSchoolByPlayerID(1).getNumTowers();
        if(numTowers < 8)
            game.addTowersTo(1, 8-numTowers);

        command = new CollectCloudCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        assertEquals(game.getPlayerByID(0).getTeamColor(), matchInfo.getWinners().get(0));
    }

    @Test
    void checkTieWinTest() throws EriantysException {
        //PLANNING PHASE 1
        int playerID = matchInfo.getCurrentPlayerID();
        Command command = new PlayCardCommand(4, gameController);

        gameController.requestCommand(playerID, command);

        //PLANNING PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new PlayCardCommand(5, gameController);

        gameController.requestCommand(playerID, command);

        //STUDENT PHASE 1
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.BLUE, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.BLUE, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 1
        int MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 1
        command = new CollectCloudCommand(0, gameController);

        gameController.requestCommand(playerID, command);


        //STUDENT PHASE 2
        playerID = matchInfo.getCurrentPlayerID();
        command = new TransferToDiningCommand(Color.RED, gameController);

        board.addToEntranceOf(playerID, new StudentGroup(Color.RED, 3));

        for(int i=0; i < 3; i++)
            gameController.requestCommand(playerID, command);

        //MN PHASE 2
        MNPositionBefore = board.getMNPosition();
        command = new MoveMNCommand(MNPositionBefore + 2, gameController);

        gameController.requestCommand(playerID, command);

        //CLOUD PHASE 2
        game.drawStudents(110);
        ProfessorTracker professors = board.getProfessorOwners();
        for(Color c : Color.values()) {
            if (professors.getOwnerIDByColor(c) != -1)
                game.giveProfessorTo(-1, c);
        }

        int numTowers = board.getSchoolByPlayerID(0).getNumTowers();
        if(numTowers < 8)
            game.addTowersTo(0, 8-numTowers);
        numTowers = board.getSchoolByPlayerID(1).getNumTowers();
        if(numTowers < 8)
            game.addTowersTo(1, 8-numTowers);

        command = new CollectCloudCommand(1, gameController);

        gameController.requestCommand(playerID, command);

        assertEquals(2, matchInfo.getWinners().size());
    }

    @Test
    void disconnectionTest() throws EriantysException {
        Command c1;
        Command c2;

        matchInfo.removePlayer();
        matchInfo.removePlayer();

        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        c1 = new PlayCardCommand(2, gameController);
        c2 = new PlayCardCommand(4, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);

        //Player 2 disconnects
        c2 = new DisconnectCommand(1, null, gameController);
        c2.execute(); //Command is urgent

        //Game should be paused now for 60s
        //Testing pause
        Command c1f = new TransferToDiningCommand(Color.BLUE, gameController);
        assertThrows(GamePausedException.class, () -> gameController.requestCommand(0, c1f));

        //Reconnecting to game
        c2 = new ReconnectCommand(1, gameController);
        c2.execute(); //Command is urgent

        //Testing if game is unpaused
        Color selected = getAvailable(0);
        Command c1ff = new TransferToDiningCommand(selected, gameController);
        assertDoesNotThrow( () -> gameController.requestCommand(0, c1ff));

        //Testing illegal move
        RoundStateController rscTest = new RoundStateController((IslandController) null, null);
        assertThrows(IllegalActionException.class, rscTest::skip);
    }

    @Test
    void disconnectionTest_ForceWinner() throws EriantysException {
        Command c1;
        Command c2;

        matchInfo.removePlayer();
        matchInfo.removePlayer();

        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        c1 = new PlayCardCommand(2, gameController);
        c2 = new PlayCardCommand(4, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);

        //Player 2 disconnects
        c2 = new DisconnectCommand(1, null, gameController);
        c2.execute(); //Command is urgent

        // Refletction to force instant Game termination
        try{
            Method forceWinMethod = gameController.getClass().getDeclaredMethod("forceDeclareWinner");
            forceWinMethod.setAccessible(true);
            forceWinMethod.invoke(gameController);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertTrue(matchInfo.isGameOver());
    }

    @Test
    void disconnectionTest_TurnSkipping() throws EriantysException {
        //Three players are needed, so we re-create the game
        gameController = null;
        Game.resetInstance();
        MatchInfo.resetInstance();
        Lobby.resetLobby();


        gameController = new GameController();
        game = Game.getInstance();
        board = game.getBoard();
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();

        lobby.addPlayer(0, "a");
        lobby.addPlayer(1, "b");
        lobby.addPlayer(2, "c");
        lobby.selectTeam(0, TowerColor.BLACK);
        lobby.selectTeam(1, TowerColor.WHITE);
        lobby.selectTeam(2, TowerColor.GREY);
        lobby.selectCardBack(0, CardBack.CB_1);
        lobby.selectCardBack(1, CardBack.CB_2);
        lobby.selectCardBack(2, CardBack.CB_3);

        matchInfo.setUpGame(3,true);
        matchInfo.setNumPlayersConnected(3);

        gameController.createGame();

        Command c1;
        Command c2;
        Command c3;

        matchInfo.removePlayer();
        matchInfo.removePlayer();
        matchInfo.removePlayer();

        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);
        matchInfo.addPlayer(2);

        c1 = new PlayCardCommand(2, gameController);
        c2 = new PlayCardCommand(4, gameController);
        c3 = new PlayCardCommand(6, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);
        gameController.requestCommand(2, c3);

        //Player 1 disconnects
        c2 = new DisconnectCommand(1, null, gameController);
        c2.execute(); //Command is urgent

        //Game should NOT be paused now
        //Testing pause
        Color selected = getAvailable(0);
        Command c1ff = new TransferToDiningCommand(selected, gameController);
        assertDoesNotThrow( () -> gameController.requestCommand(0, c1ff));

        //Finishing player 0's turn
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);

        int MNPositionBefore = board.getMNPosition();
        c1 = new MoveMNCommand(MNPositionBefore + 1, gameController);
        gameController.requestCommand(0, c1);

        c1 = new CollectCloudCommand(0, gameController);
        gameController.requestCommand(0, c1);

        //PLAYER 1'S TURN -> AFK HANDLING
        assertFalse(game.getPlayerByID(1).isConnected());
        // Skipping students
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);
        // Skipping MNMovement
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);
        //Skipping clouds
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);

        //PLAYER 2'S TURN
        assertEquals((MNPositionBefore + 1) % game.getBoard().getNumIslands(), game.getBoard().getMNPosition());

        //Finishing player 2's turn
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);

        MNPositionBefore = board.getMNPosition();
        c1 = new MoveMNCommand(MNPositionBefore + 1, gameController);
        gameController.requestCommand(2, c1);

        c1 = new CollectCloudCommand(1, gameController);
        gameController.requestCommand(2, c1);

        //PLANNING PHASE 2
        c1 = new PlayCardCommand(1, gameController);
        c2 = new SkipTurnCommand(2, gameController);
        c3 = new PlayCardCommand(5, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);
        gameController.requestCommand(2, c3);

        matchInfo.removePlayer();
        matchInfo.removePlayer();

        assertEquals(1, matchInfo.getCurrentPlayerID()); //PLAYER 1 SHOULD BE LAST
        assertEquals(Card.CARD_AFK, game.getPlayerByID(1).getSelectedCard());

        //Reconnecting to game
        c2 = new ReconnectCommand(1, gameController);
        c2.execute(); //Command is urgent

        assertTrue(game.getPlayerByID(1).isConnected());
        assertEquals(3, matchInfo.getNumPlayersConnected());
    }

    @Test
    void disconnectionTest_MIDTurnSkipping() throws EriantysException {
        //Three players are needed, so we re-create the game
        gameController = null;
        Game.resetInstance();
        MatchInfo.resetInstance();
        Lobby.resetLobby();


        gameController = new GameController();
        game = Game.getInstance();
        board = game.getBoard();
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();

        lobby.addPlayer(0, "a");
        lobby.addPlayer(1, "b");
        lobby.addPlayer(2, "c");
        lobby.selectTeam(0, TowerColor.BLACK);
        lobby.selectTeam(1, TowerColor.WHITE);
        lobby.selectTeam(2, TowerColor.GREY);
        lobby.selectCardBack(0, CardBack.CB_1);
        lobby.selectCardBack(1, CardBack.CB_2);
        lobby.selectCardBack(2, CardBack.CB_3);

        matchInfo.setUpGame(3,true);
        matchInfo.setNumPlayersConnected(3);

        gameController.createGame();

        Command c1;
        Command c2;
        Command c3;

        matchInfo.removePlayer();
        matchInfo.removePlayer();
        matchInfo.removePlayer();

        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);
        matchInfo.addPlayer(2);

        c1 = new PlayCardCommand(2, gameController);
        c2 = new PlayCardCommand(4, gameController);
        c3 = new PlayCardCommand(6, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);
        gameController.requestCommand(2, c3);

        //Game should NOT be paused now
        //Testing pause
        Color selected = getAvailable(0);
        Command c1ff = new TransferToDiningCommand(selected, gameController);
        assertDoesNotThrow( () -> gameController.requestCommand(0, c1ff));

        //Finishing player 0's turn
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);
        selected = getAvailable(0);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(0, c1);

        int MNPositionBefore = board.getMNPosition();
        c1 = new MoveMNCommand(MNPositionBefore + 1, gameController);
        gameController.requestCommand(0, c1);

        c1 = new CollectCloudCommand(0, gameController);
        gameController.requestCommand(0, c1);

        //PLAYER 1'S TURN

        selected = getAvailable(1);
        c2 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(1, c2);
        selected = getAvailable(1);
        c2 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(1, c2);

        //Player 1 disconnects
        c2 = new DisconnectCommand(1, null, gameController);
        c2.execute(); //Command is urgent

        //VirtualView sends Skip turn
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);

        int students = 0;
        for(Color color : Color.values()) {
            students += game.getBoard().getSchoolByPlayerID(1).getNumStudentsInEntranceByColor(color);
        }

        assertEquals(matchInfo.getInitialNumStudents(), students); //Check if the entrance has been refilled

        // Removing students manually to test refilling in MNState
        game.getBoard().removeFromEntranceOf(1, new StudentGroup(getAvailable(1), 1));
        game.getBoard().removeFromEntranceOf(1, new StudentGroup(getAvailable(1), 1));
        game.getBoard().removeFromEntranceOf(1, new StudentGroup(getAvailable(1), 1));
        game.getBoard().removeFromEntranceOf(1, new StudentGroup(getAvailable(1), 1));

        // Skipping MNMovement
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);

        students = 0;
        for(Color color : Color.values()) {
            students += game.getBoard().getSchoolByPlayerID(1).getNumStudentsInEntranceByColor(color);
        }

        assertEquals(matchInfo.getInitialNumStudents(), students); //Check if the entrance has been refilled

        //Skipping clouds
        c2 = new SkipTurnCommand(1, gameController);
        gameController.requestCommand(1, c2);

        //PLAYER 2'S TURN
        assertEquals((MNPositionBefore + 1 ) % game.getBoard().getNumIslands(), game.getBoard().getMNPosition());

        //Finishing player 2's turn
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);
        selected = getAvailable(2);
        c1 = new TransferToDiningCommand(selected, gameController);
        gameController.requestCommand(2, c1);

        MNPositionBefore = board.getMNPosition();
        c1 = new MoveMNCommand(MNPositionBefore + 1, gameController);
        gameController.requestCommand(2, c1);

        c1 = new CollectCloudCommand(1, gameController);
        gameController.requestCommand(2, c1);

        //PLANNING PHASE 2
        c1 = new PlayCardCommand(1, gameController);
        c2 = new SkipTurnCommand(2, gameController);
        c3 = new PlayCardCommand(5, gameController);

        // 0 plays CARD_2, 1 plays CARD_4
        gameController.requestCommand(0, c1);
        gameController.requestCommand(1, c2);
        gameController.requestCommand(2, c3);

        matchInfo.removePlayer();
        matchInfo.removePlayer();

        assertEquals(1, matchInfo.getCurrentPlayerID()); //PLAYER 1 SHOULD BE LAST
        assertEquals(Card.CARD_AFK, game.getPlayerByID(1).getSelectedCard());

        //Reconnecting to game
        c2 = new ReconnectCommand(1, gameController);
        c2.execute(); //Command is urgent

        assertTrue(game.getPlayerByID(1).isConnected());
        assertEquals(3, matchInfo.getNumPlayersConnected());
    }

    private Color getAvailable(int playerID) {
        Color selected = null; //Will get changed
        for(Color color : Color.values()) {
            if(game.getBoard().getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(color) > 0) {
                selected = color;
                break;
            }
        }
        return selected;
    }
}