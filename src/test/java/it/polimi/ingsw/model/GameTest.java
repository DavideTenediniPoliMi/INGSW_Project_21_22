package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

public class GameTest extends TestCase {

    Game g;

    public void setUp() {
        g = Game.getInstance();

        g.addPlayer(0, "ezio", TowerColor.WHITE, CardBack.CB_1, true);
        g.addPlayer(1, "bruso", TowerColor.GREY, CardBack.CB_2, true);

        g.getPlayers().stream().forEach((p) -> g.getBoard().addSchool(p));
    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
    }

    @Test
    public void testConquerIsland() {
        g.conquerIsland(0, 0);
        assertEquals(g.getPlayerByID(0).getTeamColor(), g.getBoard().getIslandAt(0).getTeamColor());
    }

    @Test
    public void testMergeIslands() {
        g.conquerIsland(0, 0);
        g.conquerIsland(1, 0);

        g.mergeIslands(0, 1);
        assertEquals(g.getBoard().getIslandAt(0).getNumIslands(), 2);
    }

    @Test
    public void testPlaceMNAt() {
        g.placeMNAt(0);
        assertTrue(g.getBoard().getIslandAt(0).isMotherNatureOnIsland());
    }

    @Test
    public void testMoveMN() {
        g.placeMNAt(0);
        g.moveMN(2);
        assertFalse(g.getBoard().getIslandAt(0).isMotherNatureOnIsland());
        assertTrue(g.getBoard().getIslandAt(2).isMotherNatureOnIsland());
    }

    @Test
    public void testCreateClouds() {
        g.createClouds(1);
        assertEquals(1, g.getBoard().getClouds().size());
    }

    @Test
    public void testCollectFromCloud() {
        g.createClouds(1);
        g.refillClouds(3);
        int before = Arrays.stream(Color.values())
                .map( (c) -> g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(c))
                .reduce( (tot, amt) -> tot += amt).orElse(0);

        g.collectFromCloud(0, 0);

        int after = Arrays.stream(Color.values())
                .map( (c) -> g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(c))
                .reduce( (tot, amt) -> tot += amt).orElse(0);

        assertEquals(before + 3, after);
    }

    public void testRefillClouds() {
        g.createClouds(1);
        g.collectFromCloud(0, 0);
        g.refillClouds(3);
        assertTrue(g.getBoard().getClouds().get(0).isAvailable());
    }

    public void testAddInitialStudentToIsland() {
        g.addInitialStudentToIsland(0, Color.GREEN);

        assertEquals(1, g.getBoard().getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    public void testTransferStudentToIsland() {
        g.getBoard().addToEntranceOf(0, new StudentGroup(Color.GREEN, 3));
        g.transferStudentToIsland(0, Color.GREEN, 0);

        assertEquals(2, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(Color.GREEN));
        assertEquals(1, g.getBoard().getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    public void testTransferStudentToDiningRoom() {
        g.getBoard().addToEntranceOf(0, new StudentGroup(Color.GREEN, 3));
        g.transferStudentToDiningRoom(0, Color.GREEN);

        assertEquals(2, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(Color.GREEN));
        assertEquals(1, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.GREEN));
    }

    public void testAddTowersTo() {
        g.addTowersTo(0, 8);
        assertEquals(8, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
    }

    public void testRemoveTowersFrom() {
        g.addTowersTo(0, 8);
        assertEquals(8, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
        g.removeTowersFrom(0, 5);
        assertEquals(3, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
    }

    public void testGiveProfessorTo() {
        g.giveProfessorTo(0, Color.GREEN);
        assertEquals(0, g.getBoard().getProfessorOwners().getOwnerIDByColor(Color.GREEN));
    }

    public void testAddPlayer() {
        g.addPlayer(2, "trap", TowerColor.BLACK, CardBack.CB_3, true);
        assertEquals("trap", g.getPlayerByID(2).getName());
    }

    public void testIsNameTaken() {
        assertTrue(g.isNameTaken("ezio"));
    }

    public void testPlayCard() {
    }

    public void testResetCards() {
    }

    public void testGiveCoinToPlayer() {
    }

    public void testGiveStudentsTo() {
    }

    public void testInstantiateCharacterCard() {
    }

    public void testGetActiveCharacterCard() {
    }

    public void testBuyCharacterCard() {
    }

    public void testSetCardParameters() {
    }

    public void testActivateCard() {
    }

    public void testDrawStudents() {
    }

    public void testPutStudentsBack() {
    }
}