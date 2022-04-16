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

class DiningRoomControllerTest {
    DiningRoomController dc;
    Game game;
    Board board;
    StudentGroup sg0, sg1, sg2;

    @BeforeEach
    void setUp() {
        dc = new DiningRoomController();
        game = Game.getInstance();
        board = game.getBoard();

        game.addPlayer(0, "ezio", TowerColor.BLACK, CardBack.CB_1, true);
        game.addPlayer(1, "enzo", TowerColor.WHITE, CardBack.CB_2, true);
        game.addPlayer(2, "leve", TowerColor.GREY, CardBack.CB_3, true);

        board.addSchool(game.getPlayerByID(0));
        board.addSchool(game.getPlayerByID(1));
        board.addSchool(game.getPlayerByID(2));

        sg0 = new StudentGroup(Color.BLUE, 1);
        sg1 = new StudentGroup(Color.BLUE, 3);
        sg2 = new StudentGroup(Color.BLUE, 2);

        board.addToDiningRoomOf(0, (StudentGroup) sg0.clone());
        board.addToDiningRoomOf(1, (StudentGroup) sg1.clone());
        board.addToDiningRoomOf(2, (StudentGroup) sg2.clone());

    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        dc = null;
        board = null;
    }

    @Test
    void testManageDiningRoomOf_noProfessors() {
        dc.manageDiningRoomOf(0, sg0);
        assertEquals(0, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
    }

    @Test
    void testManageDiningRoomOf_conquerProfessor() {
        dc.manageDiningRoomOf(0, sg0);
        assertEquals(0, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
        dc.manageDiningRoomOf(1, sg1);
        assertEquals(1, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
    }

    @Test
    void testManageDiningRoomOf_noConquerProfessor() {
        dc.manageDiningRoomOf(1, sg1);
        assertEquals(1, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
        dc.manageDiningRoomOf(2, sg2);
        assertEquals(1, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
    }
}