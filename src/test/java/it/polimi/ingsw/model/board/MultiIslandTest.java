package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;

public class MultiIslandTest extends TestCase {

    Game g;
    Board b;

    public void setUp(){
        g = Game.getInstance();
        b = g.getBoard();

        b.placeMNAt(0);

        b.addStudentsToIsland(0, new StudentGroup(Color.GREEN, 1));
        b.addStudentsToIsland(1, new StudentGroup(Color.YELLOW, 1));

        b.conquerIsland(0, TowerColor.GREY);
        b.placeMNAt(1);
        b.conquerIsland(1, TowerColor.GREY);

        b.mergeIslands(0, 1);
    }

    public void tearDown(){
        g = null;
        b = null;
    }

    public void testGetNumIslands() {
        int numIslands = b.getIslandAt(0).getNumIslands();
        b.conquerIsland(1, TowerColor.GREY);
        b.mergeIslands(0, 1);
        assertEquals(numIslands + 1, b.getIslandAt(0).getNumIslands());
    }

    public void testAddStudents() {
        int numStudentsBefore = b.getIslandAt(0).getNumStudentsByColor(Color.GREEN);

        StudentGroup sg = new StudentGroup(Color.GREEN, 3);
        b.addStudentsToIsland(0, sg);

        assertEquals(numStudentsBefore + 3, b.getIslandAt(0).getNumStudentsByColor(Color.GREEN));
    }

    public void testConquer_sameTeam(){
        b.conquerIsland(1, TowerColor.GREY);
        b.mergeIslands(0, 1);

        assertEquals(TowerColor.GREY, b.getIslandAt(0).getTeamColor());
    }

    public void testConquer_diffTeam(){
        b.conquerIsland(1, TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, b.getIslandAt(1).getTeamColor());

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