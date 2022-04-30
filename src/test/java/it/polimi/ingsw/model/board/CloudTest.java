package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {
    Cloud cloud;
    Cloud cloud1;

    @BeforeEach
    public void setUp() {
        cloud = new Cloud();

        cloud1 = new Cloud();
        StudentGroup sg = new StudentGroup(Color.BLUE, 2);
        sg.addByColor(Color.GREEN, 1);
        cloud1.refillCloud(sg);
    }

    @AfterEach
    void tearDown() {
        cloud = null;
        cloud1 = null;
    }

    @Test
    public void testIsAvailable() {
        assertAll(
                () -> assertTrue(cloud1.isAvailable()),
                () -> assertFalse(cloud.isAvailable()));
    }

    @Test
    public void testRefillCloud() {
        assertAll(
                () -> assertEquals(2, cloud1.getStudents().getByColor(Color.BLUE)),
                () -> assertEquals(1, cloud1.getStudents().getByColor(Color.GREEN)));
    }

    @Test
    public void testCollectStudents() {
        StudentGroup sg = cloud1.collectStudents();

        assertAll(
                () -> assertFalse(cloud.isAvailable()),
                () -> assertEquals(2, sg.getByColor(Color.BLUE)),
                () -> assertEquals(1, sg.getByColor(Color.GREEN)),
                () -> assertEquals(0, cloud1.getStudents().getByColor(Color.BLUE)),
                () -> assertEquals(0, cloud1.getStudents().getByColor(Color.GREEN)));
    }
}