package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class GameTest extends TestCase {

    Game g;

    @BeforeEach
    public void setUp() {
        g = Game.getInstance();

        g.addPlayer(0, "ezio", TowerColor.WHITE, CardBack.CB_1, true);
        g.addPlayer(1, "bruso", TowerColor.GREY, CardBack.CB_2, true);

        g.getPlayers().stream().forEach((p) -> g.getBoard().addSchool(p));

        g.instantiateCharacterCard(5);
        g.buyCharacterCard(0, 0);
    }

    @AfterEach
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

    public void testInstantiateCharacterCard() {
        int numCardsBefore = g.getCharacterCards().size();
        g.instantiateCharacterCard(4);
        assertEquals(numCardsBefore + 1, g.getCharacterCards().size());
    }

    public void testGetActiveCharacterCard() {
        assertTrue(g.getActiveCharacterCard().isActive());
    }

    public void testBuyCharacterCard() {
        int coinsBoardBefore = g.getBoard().getNumCoinsLeft();
        int coinsCardBefore = g.getActiveCharacterCard().getCost();

        g.buyCharacterCard(0, 0);

        assertEquals(coinsBoardBefore + coinsCardBefore - 1, g.getBoard().getNumCoinsLeft());
        assertEquals(coinsCardBefore + 1, g.getActiveCharacterCard().getCost());
        assertTrue(g.getActiveCharacterCard().isActive());
    }

    public void testSetCardParameters() {
    }

    public void testActivateCard() {
        assertTrue(g.getActiveCharacterCard().isActive());
    }

}