package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleIslandTest {

    Game g;
    Board b;

    @BeforeEach
    public void setUp(){
        g = Game.getInstance();
        b = g.getBoard();

        b.placeMNAt(0);
    }

    @AfterEach
    public void tearDown(){
        Game.resetInstance();
        g = null;
        b = null;
    }

    @Test
    public void testAddStudents() {
        int numBefore = b.getIslandAt(0).getNumStudentsByColor(Color.GREEN);

        StudentGroup sg = new StudentGroup(Color.GREEN, 3);
        b.addStudentsToIsland(0, sg);

        assertEquals(numBefore + 3, b.getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    @Test
    public void testConquer_sameTeam(){
        b.conquerIsland(TowerColor.BLACK);
        b.conquerIsland(TowerColor.BLACK);

        assertEquals(TowerColor.BLACK, b.getIslandAt(0).getTeamColor());
    }

    @Test
    public void testConquer_diffTeam(){
        b.conquerIsland(TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, b.getIslandAt(0).getTeamColor());

        b.conquerIsland(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, b.getIslandAt(0).getTeamColor());
    }

    @Test
    public void testMoveMNTo(){
        assertTrue(b.getIslandAt(0).isMotherNatureOnIsland());

        b.moveMN(1);

        assertTrue(b.getIslandAt(1).isMotherNatureOnIsland());
        assertFalse(b.getIslandAt(0).isMotherNatureOnIsland());
    }

}