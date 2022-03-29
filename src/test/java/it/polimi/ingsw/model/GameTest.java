package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Card;
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

        g.instantiateCharacterCard(5);
        g.buyCharacterCard(0, 0);
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

    @Test
    public void testRefillClouds() {
        g.createClouds(1);
        g.collectFromCloud(0, 0);
        g.refillClouds(3);
        assertTrue(g.getBoard().getClouds().get(0).isAvailable());
    }

    @Test
    public void testAddInitialStudentToIsland() {
        g.addInitialStudentToIsland(0, Color.GREEN);

        assertEquals(1, g.getBoard().getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    @Test
    public void testTransferStudentToIsland() {
        g.getBoard().addToEntranceOf(0, new StudentGroup(Color.GREEN, 3));
        g.transferStudentToIsland(0, Color.GREEN, 0);

        assertEquals(2, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(Color.GREEN));
        assertEquals(1, g.getBoard().getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    @Test
    public void testTransferStudentToDiningRoom() {
        g.getBoard().addToEntranceOf(0, new StudentGroup(Color.GREEN, 3));
        g.transferStudentToDiningRoom(0, Color.GREEN);

        assertEquals(2, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(Color.GREEN));
        assertEquals(1, g.getBoard().getSchoolByPlayerID(0).getNumStudentsInDiningRoomByColor(Color.GREEN));
    }

    @Test
    public void testAddTowersTo() {
        g.addTowersTo(0, 8);
        assertEquals(8, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
    }

    @Test
    public void testRemoveTowersFrom() {
        g.addTowersTo(0, 8);
        assertEquals(8, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
        g.removeTowersFrom(0, 5);
        assertEquals(3, g.getBoard().getSchoolByPlayerID(0).getNumTowers());
    }

    @Test
    public void testGiveProfessorTo() {
        g.giveProfessorTo(0, Color.GREEN);
        assertEquals(0, g.getBoard().getProfessorOwners().getOwnerIDByColor(Color.GREEN));
    }

    @Test
    public void testAddPlayer() {
        g.addPlayer(2, "trap", TowerColor.BLACK, CardBack.CB_3, true);
        assertEquals("trap", g.getPlayerByID(2).getName());
    }

    @Test
    public void testIsNameTaken() {
        assertTrue(g.isNameTaken("ezio"));
    }

    @Test
    public void testPlayCard() {
        Card c = Card.CARD_1;
        g.playCard(0, c);
        assertTrue(c.isUsed());
    }

    @Test
    public void testResetCards() {
        g.resetCards();
        for(Card c : Card.values()) {
            assertFalse(c.isUsed());
        }
    }

    @Test
    public void testGiveCoinToPlayer() {
        Board b = g.getBoard();
        int coinsBefore = b.getNumCoinsLeft();
        int coinsPlayerBefore = g.getPlayerByID(0).getNumCoins();

        g.giveCoinToPlayer(0);

        assertEquals(coinsBefore - 1, b.getNumCoinsLeft());
        assertEquals(coinsPlayerBefore + 1, g.getPlayerByID(0).getNumCoins());
    }

    @Test
    public void testInstantiateCharacterCard() {
        int numCardsBefore = g.getCharacterCards().size();
        g.instantiateCharacterCard(4);
        assertEquals(numCardsBefore + 1, g.getCharacterCards().size());
    }

    @Test
    public void testGetActiveCharacterCard() {
        assertTrue(g.getActiveCharacterCard().isActive());
    }

    @Test
    public void testBuyCharacterCard() {
        int coinsBoardBefore = g.getBoard().getNumCoinsLeft();
        int coinsCardBefore = g.getActiveCharacterCard().getCost();

        g.buyCharacterCard(0, 0);

        assertEquals(coinsBoardBefore + coinsCardBefore - 1, g.getBoard().getNumCoinsLeft());
        assertEquals(coinsCardBefore + 1, g.getActiveCharacterCard().getCost());
        assertTrue(g.getActiveCharacterCard().isActive());
    }

    @Test
    public void testSetCardParameters() {
    }

    @Test
    public void testActivateCard() {
        assertTrue(g.getActiveCharacterCard().isActive());
    }
}