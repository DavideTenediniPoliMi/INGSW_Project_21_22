package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentStateControllerExpertTest {
    StudentsStateController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        game.addPlayer(new Player(0, "lollo", TowerColor.BLACK, CardBack.CB_1, true));
        game.giveCoinToPlayer(0);
        game.getBoard().addSchool(game.getPlayerByID(0));
        game.addPlayer(new Player(1, "lello", TowerColor.WHITE, CardBack.CB_2, true));

        game.instantiateCharacterCard(CharacterCards.EXCHANGE_STUDENTS.ordinal());

        matchInfo = MatchInfo.getInstance();
        matchInfo.setSelectedNumPlayer(2);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setExpertMode(true);
        matchInfo.setNumPlayersConnected(2);
        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        RoundStateController oldState = new RoundStateController(new IslandController(), new DiningRoomController());
        controller = new StudentsStateController(oldState);
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
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.definePlayOrder()),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.playCard(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.collectFromCloud(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.moveMN(1))
        );
    }

    @Test
    public void testBuyCharacterCardBadParam() {
        assertThrowsExactly(BadParametersException.class, () -> controller.buyCharacterCard(3));
    }

    @Test
    public void activateTest() {
        Game game = Game.getInstance();
        game.getBoard().addToEntranceOf(0, new StudentGroup(Color.BLUE, 2));
        game.getBoard().addToEntranceOf(0, new StudentGroup(Color.RED, 2));
        game.transferStudentToDiningRoom(0,Color.BLUE);
        game.transferStudentToDiningRoom(0,Color.BLUE);

        controller.buyCharacterCard(0);
        CardParameters params = new CardParameters();
        params.setFromOrigin(new StudentGroup(Color.RED, 2));
        params.setFromDestination(new StudentGroup(Color.BLUE, 2));
        params.setPlayerID(0);
        controller.setCardParameters(params);

        controller.activateCard();

        assertAll(
                () -> assertEquals(2, game.getBoard().getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.RED)),
                () -> assertEquals(2, game.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(Color.BLUE))
        );
    }
}
