package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.ExpertModeDisabledException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.students.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.students.StudentTransferException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentStateControllerTest {
    StudentsStateController controller;
    Game game;
    Board board;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        board = game.getBoard();
        game.addPlayer(new Player(0, "lollo", TowerColor.BLACK, CardBack.WIZARD_1, true));
        board.addSchool(game.getPlayerByID(0));
        game.addPlayer(new Player(1, "lello", TowerColor.WHITE, CardBack.WIZARD_2, true));
        board.addSchool(game.getPlayerByID(1));
        game.addPlayer(new Player(2, "lillo", TowerColor.GREY, CardBack.WIZARD_3, true));
        board.addSchool(game.getPlayerByID(2));

        matchInfo = MatchInfo.getInstance();
        matchInfo.setUpGame(3,false);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setNumPlayersConnected(3);
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
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.moveMN(1)),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.buyCharacterCard(0)),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.setCardParameters(new CardParameters())),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.activateCard())
        );
    }

    @Test
    void transferStudentToIslandTest_islandIndexOutOfBound() {
        game.giveStudentsTo(0, 2);
        StudentGroup entrance = new StudentGroup();
        for (Color c : Color.values()) {
            entrance.addByColor(c, board.getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(c));
            if(entrance.getByColor(c) > 0) {
                assertThrowsExactly(BadParametersException.class, () -> controller.transferStudentToIsland(-1, c));
                assertThrowsExactly(BadParametersException.class, () -> controller.transferStudentToIsland(12, c));
                return;
            }
        }
    }

    @Test
    void transferStudentToIslandTest_movableStudentsExceed() {
        board.addToEntranceOf(0, new StudentGroup(Color.BLUE, 5));
        matchInfo.setUpGame(3,true);

        for(int i=0; i < 4; i++) {
            assertDoesNotThrow(() -> controller.transferStudentToIsland(0, Color.BLUE));
        }
        assertThrowsExactly(StudentTransferException.class, () -> controller.transferStudentToIsland(0, Color.BLUE));
    }

    @Test
    void transferStudentToIslandTest_notEnoughStudents() {
        board.addToEntranceOf(0, new StudentGroup(Color.BLUE, 1));
        matchInfo.setUpGame(3,true);

        assertDoesNotThrow(() -> controller.transferStudentToIsland(0, Color.BLUE));
        assertThrowsExactly(NotEnoughStudentsException.class, () -> controller.transferStudentToIsland(0, Color.BLUE));
    }

    @Test
    void transferStudentToIslandTest() {
        board.addToEntranceOf(0, new StudentGroup(Color.BLUE, 1));
        matchInfo.setUpGame(3,true);

        int numStudentsMovedBefore = matchInfo.getNumMovedStudents();

        assertAll(
                () -> assertDoesNotThrow(() -> controller.transferStudentToIsland(0, Color.BLUE)),
                () -> assertEquals(numStudentsMovedBefore+1, matchInfo.getNumMovedStudents()),
                () -> assertEquals(1, board.getIslandAt(0).getNumStudentsByColor(Color.BLUE))
        );
    }

    @Test
    void transferStudentToDiningRoom() {
        board.addToEntranceOf(0, new StudentGroup(Color.BLUE, 5));
        matchInfo.setUpGame(3,true);

        for(int i=0; i < 4; i++) {
            assertDoesNotThrow(() -> controller.transferStudentToDiningRoom(Color.BLUE));
        }
        assertThrowsExactly(StudentTransferException.class, () -> controller.transferStudentToDiningRoom(Color.BLUE));
    }

    @Test
    void transferStudentToDiningRoom_notEnoughStudents() {
        board.addToEntranceOf(0, new StudentGroup(Color.BLUE, 1));
        matchInfo.setUpGame(3,true);

        assertDoesNotThrow(() -> controller.transferStudentToDiningRoom(Color.BLUE));
        assertThrowsExactly(NotEnoughStudentsException.class, () -> controller.transferStudentToDiningRoom(Color.BLUE));
    }
}
