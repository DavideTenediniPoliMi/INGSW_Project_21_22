package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import junit.framework.TestCase;

public class GameTest extends TestCase {

    Game g;

    public void setUp() {
        g = Game.getInstance();

        g.addPlayer(0, "ezio", TowerColor.WHITE, CardBack.CB_1, true);
        g.addPlayer(1, "bruso", TowerColor.GREY, CardBack.CB_2, true);

        g.addTowersTo(0, 8);
        g.addTowersTo(1, 8);

        g.createClouds(3);
    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
    }

    public void testConquerIsland() {
        g.conquerIsland(0, 0);
        assertEquals(g.getPlayerByID(0).getTeamColor(), g.getBoard().getIslandAt(0).getTeamColor());
    }

    public void testMergeIslands() {
        g.conquerIsland(0, 0);
        g.conquerIsland(1, 0);

        g.mergeIslands(0, 1);
        assertEquals(g.getBoard().getIslandAt(0).getNumIslands(), 2);
    }

    public void testPlaceMNAt() {
    }

    public void testMoveMN() {
    }

    public void testCreateClouds() {
    }

    public void testCollectFromCloud() {
    }

    public void testRefillClouds() {
    }

    public void testAddInitialStudentToIsland() {
    }

    public void testTransferStudentToIsland() {
    }

    public void testTransferStudentToDiningRoom() {
    }

    public void testAddTowersTo() {
    }

    public void testRemoveTowersFrom() {
    }

    public void testGiveProfessorTo() {
    }

    public void testGetPlayers() {
    }

    public void testGetPlayerByID() {
    }

    public void testAddPlayer() {
    }

    public void testIsNameTaken() {
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