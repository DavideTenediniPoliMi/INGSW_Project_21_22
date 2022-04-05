package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class AlterInfluenceDecoratorTest {
    private Game game;
    private Board board;
    private AlterInfluenceDecorator c1, c2, c3, c4, c5;
    private Parameters p1, p2, p3, p4, p5;

    @BeforeEach
    public void setUp() throws Exception {
        game = Game.getInstance();
        board = game.getBoard();
        game.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
        game.placeMNAt(0);
        game.conquerIsland(TowerColor.BLACK);
        game.giveProfessorTo(0, Color.BLUE);

        //addTwo
        c1 = new AlterInfluenceDecorator(new GenericCard(1, EffectType.ALTER_INFLUENCE), true, false, false);

        Parameters p0 = new Parameters();
        p0.setBoostedTeam(TowerColor.BLACK);
        c1.setParameters(p0);

        //ignoreTowers
        c2 = new AlterInfluenceDecorator(new GenericCard(1,EffectType.ALTER_INFLUENCE), false, true, false);

        //params same team
        p1 = new Parameters();
        p1.setCurrentTeam(TowerColor.BLACK);
        p1.setIslandIndex(0);

        //params different team
        p2 = new Parameters();
        p2.setCurrentTeam(TowerColor.WHITE);
        p2.setIslandIndex(0);

        //ignoreColor

        //selected color
        c3 = new AlterInfluenceDecorator(new GenericCard(1, EffectType.ALTER_INFLUENCE), false, false, true);
        p3 = new Parameters();
        p3.setSelectedColor(Color.BLUE);
        p3.setIslandIndex(0);
        p3.setCurrentTeam(TowerColor.BLACK);
        c3.setParameters(p3);

        //not selected color
        c4 = new AlterInfluenceDecorator(new GenericCard(1, EffectType.ALTER_INFLUENCE), false, false, true);
        p4 = new Parameters();
        p4.setSelectedColor(Color.GREEN);
        p4.setIslandIndex(0);
        p4.setCurrentTeam(TowerColor.BLACK);
        c4.setParameters(p4);

        //selected color but not current team
        c5 = new AlterInfluenceDecorator(new GenericCard(1, EffectType.ALTER_INFLUENCE), false, false, true);
        p5 = new Parameters();
        p5.setSelectedColor(Color.BLUE);
        p5.setIslandIndex(0);
        p5.setCurrentTeam(TowerColor.WHITE);
        c5.setParameters(p5);

    }

    @AfterEach
    public void tearDown() throws Exception {
        Game.resetInstance();
        game = null;
        board = null;
        c1 = c2 = c3 = c4 = c5 = null;
        p1 = p2 = p3 = p4 = p5 = null;
    }

    @Test
    public void testSetParameters() {

    }

    @Test
    public void testActivate_isAddTwo_sameTeam() {
        c1.setParameters(p1);
        int delta = c1.activate();
        assertTrue(c1.isActive());
        assertEquals(2, delta);
    }

    @Test
    public void testActivate_isAddTwo_differentTeam() {

        c1.setParameters(p2);
        int delta = c1.activate();
        assertTrue(c1.isActive());
        assertEquals(0, delta);
    }

    @Test
    public void testActivate_isIgnoreTowers_sameTeam() {
        c2.setParameters(p1);
        int delta = c2.activate();
        assertTrue(c2.isActive());
        assertEquals(Math.negateExact(board.getIslandAt(p1.getIslandIndex()).getNumIslands()), delta);
    }

    @Test
    public void testActivate_isIgnoreTowers_differentTeam() {
        c2.setParameters(p2);
        int delta = c2.activate();
        assertTrue(c2.isActive());
        assertEquals(0, delta);
    }

    @Test
    public void testActivate_isIgnoreColor_professorOwner() {
        int delta = c3.activate();
        assertTrue(c3.isActive());
        Island island = board.getIslandAt(p3.getIslandIndex());
        assertEquals(Math.negateExact(island.getNumStudentsByColor(p3.getSelectedColor())) , delta);
    }
    @Test
    public void testActivate_isIgnoreColor_notProfessorOwner() {
        int delta = c4.activate();
        assertTrue(c4.isActive());
        Island island = board.getIslandAt(p4.getIslandIndex());
        assertEquals(0 , delta);
    }

    @Test
    public void testActivate_isIgnoreColor_professorOwner_sameTeam() {
        c5.setParameters(p1);
        int delta = c5.activate();
        assertTrue(c5.isActive());
        Island island = board.getIslandAt(p5.getIslandIndex());
        assertEquals(0, delta);
    }

    @Test
    public void testActivate_isIgnoreColor_professorOwner_differentTeam() {
        int delta = c5.activate();
        assertTrue(c5.isActive());
        Island island = board.getIslandAt(p5.getIslandIndex());
        assertEquals(0, delta);
    }
}