package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.board.CloudUnavailableException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CloudStateControllerTest {
    CloudStateController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        game.addPlayer(new Player(0, "lollo", TowerColor.BLACK, CardBack.WIZARD1, true));
        game.addPlayer(new Player(1, "lello", TowerColor.WHITE, CardBack.WIZARD2, true));
        game.addPlayer(new Player(2, "lillo", TowerColor.GREY, CardBack.WIZARD3, true));

        matchInfo = MatchInfo.getInstance();
        matchInfo.setUpGame(3,true);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setNumPlayersConnected(3);
        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        RoundStateController oldState = new RoundStateController(new IslandController(), new DiningRoomController());
        controller = new CloudStateController(oldState);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        MatchInfo.resetInstance();
        controller = null;
        game = null;
        matchInfo = null;
    }

    @Test
    public void testIllegalActions() {
        assertAll(
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.playCard(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToDiningRoom(Color.BLUE)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToIsland(0,Color.BLUE)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.moveMN(1)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.buyCharacterCard(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.setCardParameters(new CardParameters())),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.activateCard())
        );
    }

    @Test
    void definePlayOrderTest() {
        matchInfo.removePlayer();
        matchInfo.removePlayer();
        game.playCard(0, Card.OSTRICH);
        game.playCard(1, Card.LION);
        game.playCard(2, Card.CAT);

        controller.definePlayOrder();

        assertEquals(1, matchInfo.getPlayOrder().peek());
        matchInfo.removePlayer();
        assertEquals(2, matchInfo.getPlayOrder().peek());
        matchInfo.removePlayer();
        assertEquals(0, matchInfo.getPlayOrder().peek());
    }

    @Test
    void collectFromCloudTest_nullPlayer() {
        matchInfo.removePlayer();
        matchInfo.removePlayer();

        assertThrowsExactly(NullPlayerException.class, () -> controller.collectFromCloud(0));
    }

    @Test
    void collectFromCloudTest_cloudUnavailable() {
        game.createClouds(1);
        assertThrowsExactly(CloudUnavailableException.class, () -> controller.collectFromCloud(0));
    }

    @Test
    void collectFromCloudTest() {
        game.createClouds(1);
        game.refillClouds(2);
        Board board = game.getBoard();
        StudentGroup cloudStudents = board.getClouds().get(0).getStudents();
        board.addSchool(game.getPlayerByID(0));

        try {
            controller.collectFromCloud(0);
        } catch (CloudUnavailableException e) {
            throw new RuntimeException(e);
        }

        for(Color c : Color.values()) {
            assertEquals(cloudStudents.getByColor(c), board.getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(c));
        }
    }
}
