package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;

public class CloudTest extends TestCase {

    Game g;
    Board b;
    Cloud c;

    public void setUp() {
        g = Game.getInstance();
        b = g.getBoard();
        c = new Cloud();
        b.createClouds(1);
    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
        b = null;
        c = null;
    }

    public void testIsAvailable() {
        assertTrue(c.isAvailable());
        c.collectStudents();
        assertFalse(c.isAvailable());
    }

    public void testRefillCloud() {
        StudentGroup sg = new StudentGroup();

        b.collectFromCloud(0);

        b.refillClouds(3);

        b.collectFromCloud(0).transferAllTo(sg);
        int num = 0;
        for(Color c: Color.values()){
            num += sg.getByColor(c);
        }

        assertFalse(num == 0);
    }

    public void testCollectStudents() {
        b.refillClouds(3);
        StudentGroup sg = b.collectFromCloud(0);

        int num = 0;

        for(Color c: Color.values()){
            num += sg.getByColor(c);
        }

        assertEquals(num, 3);
    }
}