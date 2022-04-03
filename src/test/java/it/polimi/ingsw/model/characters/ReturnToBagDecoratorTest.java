package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ReturnToBagDecoratorTest {
    ReturnToBagDecorator c, c2, c3;
    Game game;
    Board board;
    Parameters p1, p2, p3;

    @BeforeEach
    public void setUp() throws Exception {
        c = new ReturnToBagDecorator(new GenericCard(2, EffectType.RETURN_TO_BAG));
        c2 = new ReturnToBagDecorator(new GenericCard(2, EffectType.RETURN_TO_BAG));
        c3 = new ReturnToBagDecorator(new GenericCard(2, EffectType.RETURN_TO_BAG));


        game = Game.getInstance();
        board = game.getBoard();
        game.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
        game.addPlayer(1, "luigi", TowerColor.WHITE, CardBack.CB_2, true);
        board.addSchool(game.getPlayerByID(0));
        board.addSchool(game.getPlayerByID(1));


        p1 = new Parameters();
        p1.setSelectedColor(Color.PINK);
        c.setParameters(p1);


        p2 = new Parameters();
        p2.setSelectedColor(Color.RED);
        c2.setParameters(p2);


        p3 = new Parameters();
        p3.setSelectedColor(Color.BLUE);
        c3.setParameters(p3);
    }

    @AfterEach
    public void tearDown() throws Exception {
        c = c2 = c3 = null;
        game = null;
        board = null;
        p1 = p2 = p3 = null;
        Game.resetInstance();
    }

    @Test
    public void testSetParameters() {
    }

    @Test
    public void testActivate_studentsAvailableGreaterThanMax() {
        StudentGroup sg = new StudentGroup(Color.PINK, 2);
        board.addToDiningRoomOf(0, sg);
        StudentGroup sg2 = new StudentGroup(Color.PINK, 4);
        board.addToDiningRoomOf(1, sg2);

        int[] numStudentsBefore = new int[2];
        for(Player pl : game.getPlayers()) {
            numStudentsBefore[pl.getID()] = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p1.getSelectedColor());
        }

        c.activate();

        for(Player pl : game.getPlayers()) {
            int numStud = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p1.getSelectedColor());
            assertEquals(Math.max(0, numStudentsBefore[pl.getID()]-3), numStud);
        }
    }

    @Test
    public void testActivate_studentsAvailableLowerThanMax() {
        StudentGroup sg3 = new StudentGroup(Color.RED, 2);
        board.addToDiningRoomOf(0, sg3);

        StudentGroup sg4 = new StudentGroup(Color.RED, 1);
        board.addToDiningRoomOf(1, sg4);

        int[] numStudentsBefore = new int[2];
        for(Player pl : game.getPlayers()) {
            numStudentsBefore[pl.getID()] = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p2.getSelectedColor());
        }

        c2.activate();

        for(Player pl : game.getPlayers()) {
            int numStud = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p2.getSelectedColor());
            assertEquals(Math.max(0, numStudentsBefore[pl.getID()]-3), numStud);
        }
    }

    @Test
    public void testActivate_noStudentsOfSelectedColor() {
        StudentGroup sg5 = new StudentGroup(Color.BLUE, 3);
        board.addToDiningRoomOf(0, sg5);

        StudentGroup sg6 = new StudentGroup(Color.GREEN, 3);
        board.addToDiningRoomOf(1, sg6);

        int[] numStudentsBefore = new int[2];
        for(Player pl : game.getPlayers()) {
            numStudentsBefore[pl.getID()] = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p3.getSelectedColor());
        }

        c3.activate();

        for(Player pl : game.getPlayers()) {
            int numStud = board.getSchoolByPlayerID(pl.getID()).getNumStudentsInDiningRoomByColor(p3.getSelectedColor());
            assertEquals(Math.max(0, numStudentsBefore[pl.getID()]-3), numStud);
        }
    }
}