package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;

public class SchoolTest extends TestCase {

    Game g;
    Board b;

    public void setUp() {
        g = Game.getInstance();
        b = g.getBoard();

        g.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
        g.addPlayer(1, "luigi", TowerColor.WHITE, CardBack.CB_2, true);

        for(Player p : g.getPlayers()){
            b.addSchool(p);
        }
        b.addTowersTo(1, 1);

    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
        b = null;
    }

    public void testGetOwner() {
        assertEquals(b.getSchoolByPlayerID(0).getOwner(), g.getPlayerByID(0));
        assertEquals(b.getSchoolByPlayerID(1).getOwner(), g.getPlayerByID(1));
    }

    public void testGetNumTowers() {
        assertEquals(0, b.getSchoolByPlayerID(0).getNumTowers());
    }

    public void testRemoveTowers() {
        int numTowersBefore = b.getSchoolByPlayerID(1).getNumTowers();
        b.removeTowerFrom(1, 1);
        assertEquals(numTowersBefore - 1, b.getSchoolByPlayerID(1).getNumTowers());
    }

    public void testAddTowers() {
        b.addTowersTo(0, 1);
        assertEquals(1, b.getSchoolByPlayerID(0).getNumTowers());
    }

    public void testGetNumStudentsInDiningRoomByColor() {
        int numBefore = b.getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.BLUE);
        b.addToDiningRoomOf(0, new StudentGroup(Color.BLUE, 3));
        assertEquals(numBefore + 3, b.getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.BLUE));
    }

    public void testRemoveFromEntrance() {
        b.addToEntranceOf(1, new StudentGroup(Color.GREEN, 3));
        b.removeFromEntranceOf(1, new StudentGroup(Color.GREEN, 1));
        assertEquals(2, b.getSchoolByPlayerID(1).getNumStudentsInEntranceByColor(Color.GREEN));
    }

    public void testAddToEntrance() {
        b.addToEntranceOf(1, new StudentGroup(Color.RED, 1));
        assertEquals(1, b.getSchoolByPlayerID(1).getNumStudentsInEntranceByColor(Color.RED));
    }

    public void testRemoveFromDiningRoom() {
        b.addToDiningRoomOf(0, new StudentGroup(Color.BLUE, 3));
        b.removeFromDiningRoomOf(0, new StudentGroup(Color.BLUE, 1));
        assertEquals(2, b.getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.BLUE));
    }

    public void testAddToDiningRoom() {
        b.addToDiningRoomOf(0, new StudentGroup(Color.RED, 1));
        assertEquals(1, b.getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.RED));
    }
}