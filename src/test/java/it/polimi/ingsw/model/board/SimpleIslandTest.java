package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;

public class SimpleIslandTest extends TestCase {

    Game g;
    Board b;

    public void setUp(){
        g = Game.getInstance();
        b = g.getBoard();

        b.placeMNAt(0);
    }

    public void tearDown(){
        g = null;
        b = null;
    }

    public void testAddStudents() {
        int numBefore = b.getIslandAt(0).getNumStudentsByColor(Color.GREEN);

        StudentGroup sg = new StudentGroup(Color.GREEN, 3);
        b.addStudentsToIsland(0, sg);

        assertEquals(numBefore + 3, b.getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    public void testConquer_sameTeam(){
        b.conquerIsland(0, TowerColor.BLACK);
        b.conquerIsland(0, TowerColor.BLACK);

        assertEquals(TowerColor.BLACK, b.getIslandAt(0).getTeamColor());
    }

    public void testConquer_diffTeam(){
        b.conquerIsland(0, TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, b.getIslandAt(0).getTeamColor());

        b.conquerIsland(0, TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, b.getIslandAt(0).getTeamColor());
    }

    public void testMoveMNTo(){
        assertTrue(b.getIslandAt(0).isMotherNatureOnIsland());

        b.moveMN(1);

        assertTrue(b.getIslandAt(1).isMotherNatureOnIsland());
        assertFalse(b.getIslandAt(0).isMotherNatureOnIsland());
    }

}