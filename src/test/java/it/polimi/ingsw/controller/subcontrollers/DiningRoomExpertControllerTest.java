package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiningRoomExpertControllerTest {
    DiningRoomExpertController dc;
    Game game;
    Board board;

    @BeforeEach
    void setUp() {
        dc = new DiningRoomExpertController();
        game = Game.getInstance();
        board = game.getBoard();

        game.addPlayer(0, "ezio", TowerColor.BLACK, CardBack.CB_1, true);

        board.addSchool(game.getPlayerByID(0));
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        dc = null;
        board = null;
    }

    @Test
    void testManageDiningRoomOf_zeroStudents() {
        dc.manageDiningRoomOf(0, new StudentGroup());
        assertEquals(0, game.getPlayerByID(0).getNumCoins());
    }
    @Test
    void testManageDiningRoomOf_notEnoughStudentsForCoin() {
        StudentGroup sg = new StudentGroup(Color.BLUE, 1);
        board.addToDiningRoomOf(0, (StudentGroup) sg.clone());
        dc.manageDiningRoomOf(0, sg);
        assertEquals(0, game.getPlayerByID(0).getNumCoins());
    }

    @Test
    void testManageDiningRoomOf_giveCoin() {
        StudentGroup sg = new StudentGroup(Color.BLUE, 3);
        board.addToDiningRoomOf(0, (StudentGroup) sg.clone());
        dc.manageDiningRoomOf(0, sg);
        assertEquals(1, game.getPlayerByID(0).getNumCoins());
    }
    @Test
    void testManageDiningRoomOf_moreCoins() {
        StudentGroup sg = new StudentGroup(Color.BLUE, 3);
        board.addToDiningRoomOf(0, (StudentGroup) sg.clone());
        dc.manageDiningRoomOf(0, sg);
        assertEquals(1, game.getPlayerByID(0).getNumCoins());
        StudentGroup sg1 = new StudentGroup(Color.BLUE, 3);
        board.addToDiningRoomOf(0, (StudentGroup) sg1.clone());
        dc.manageDiningRoomOf(0, sg1);
        assertEquals(2, game.getPlayerByID(0).getNumCoins());
    }
}