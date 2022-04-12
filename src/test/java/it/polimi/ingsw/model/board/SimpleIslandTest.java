package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleIslandTest {
    Island is;

    @BeforeEach
    public void setUp(){
        is = new SimpleIsland();
    }

    @AfterEach
    public void tearDown(){
        is = null;
    }

    @Test
    public void motherNatureTest() {
        assertFalse(is.isMotherNatureOnIsland());
        is.setMotherNatureTo(true);
        assertTrue(is.isMotherNatureOnIsland());
        is.setMotherNatureTo(false);
        assertFalse(is.isMotherNatureOnIsland());
    }

    @Test
    public void conquerTest() {
        assertNull(is.getTeamColor());
        is.conquerIsland(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, is.getTeamColor());
        is.conquerIsland(TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, is.getTeamColor());
    }

    @Test
    public void numIslandTest() {
        assertEquals(1, is.getNumIslands());
    }

    @Test
    public void studentTest() {
        for(Color c: Color.values()) {
            assertEquals(0, is.getNumStudentsByColor(c));
        }

        is.addStudents(new StudentGroup(Color.BLUE, 3));

        assertEquals(3, is.getNumStudentsByColor(Color.BLUE));
    }
}