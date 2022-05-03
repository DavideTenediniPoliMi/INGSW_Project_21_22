package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MultiIslandTest {
    Island is;
    @BeforeEach
    public void setUp(){
        Island is1 = new SimpleIsland();
        Island is2 = new SimpleIsland();

        is1.conquerIsland(TowerColor.BLACK);
        is2.conquerIsland(TowerColor.BLACK);

        is1.addStudents(new StudentGroup(Color.BLUE, 2));
        is2.addStudents(new StudentGroup(Color.GREEN, 3));

        is = new MultiIsland(is1, is2);
    }

    @AfterEach
    void tearDown(){
        is = null;
    }

    @Test
    public void motherNatureTest() {
        assertTrue(is.isMotherNatureOnIsland());
        is.setMotherNatureTo(false);
        assertFalse(is.isMotherNatureOnIsland());
    }

    @Test
    public void numStudentTest() {
        assertAll(
                () -> assertEquals(2, is.getNumStudentsByColor(Color.BLUE)),
                () -> assertEquals(3, is.getNumStudentsByColor(Color.GREEN))
        );
    }

    @Test
    public void numIslandTest() {
        assertEquals(2, is.getNumIslands());

        Island is1 = new SimpleIsland();
        is1.conquerIsland(TowerColor.BLACK);
        Island is2 = new MultiIsland(is, is1);

        assertEquals(3, is2.getNumIslands());
    }

    @Test
    public void towerColorTest() {
        assertEquals(TowerColor.BLACK, is.getTeamColor());
        is.conquerIsland(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, is.getTeamColor());
    }

    @Test
    public void testSerialize() {
        System.out.println(is.serialize());
    }
}