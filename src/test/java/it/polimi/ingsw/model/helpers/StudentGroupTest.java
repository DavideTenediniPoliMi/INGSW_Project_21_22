package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class StudentGroupTest {

    StudentGroup studentGroup;
    StudentGroup studentGroup1;

    @BeforeEach
    public void setUp(){
        studentGroup = new StudentGroup(Color.BLUE, 4);
        studentGroup1 = new StudentGroup();
    }

    @AfterEach
    public void tearDown(){
        studentGroup = null;
    }

    @Test
    public void testGetByColor() {
        assertEquals(4, studentGroup.getByColor(Color.BLUE));

        for(Color c: Color.values()) {
            if(c != Color.BLUE) {
                assertEquals(0, studentGroup.getByColor(c));
            }
            assertEquals(0, studentGroup1.getByColor(c));
        }
    }

    @Test
    public void testAddByColor() {
        studentGroup.addByColor(Color.BLUE, 1);
        assertEquals(5, studentGroup.getByColor(Color.BLUE));

        for(Color c: Color.values()) {
            if(c != Color.BLUE) {
                assertEquals(0, studentGroup.getByColor(c));
            }
        }
    }

    @Test
    public void testTransferAllToSingleColor() {
        studentGroup.transferAllTo(studentGroup1);
        assertEquals(4, studentGroup1.getByColor(Color.BLUE));
        assertEquals(0, studentGroup.getByColor(Color.BLUE));
    }

    @Test
    public void testTransferAllToMultipleColors() {
        studentGroup.addByColor(Color.GREEN, 3);

        studentGroup.transferAllTo(studentGroup1);

        assertEquals(4, studentGroup1.getByColor(Color.BLUE));
        assertEquals(3, studentGroup1.getByColor(Color.GREEN));
        assertEquals(0, studentGroup.getByColor(Color.GREEN));
    }

    @Test
    public void testTransferTo() {
        StudentGroup tmp = new StudentGroup(Color.BLUE, 1);
        studentGroup.transferTo(studentGroup1, tmp);

        assertEquals(1, studentGroup1.getByColor(Color.BLUE));
        assertEquals(3, studentGroup.getByColor(Color.BLUE));
    }

    @Test
    public void testClone() {
        StudentGroup s = new StudentGroup(Color.BLUE, 1);
        StudentGroup clone = (StudentGroup) s.clone();
        assertEquals(1, clone.getByColor(Color.BLUE));
    }

    @Test
    public void testEquals() {
        StudentGroup s = new StudentGroup(Color.GREEN, 1);
        assertEquals(s, s.clone());
        assertNotEquals(s, new StudentGroup());
        assertEquals(s.hashCode(), s.clone().hashCode());
    }

    @Test
    void testContains() {
        assertFalse(studentGroup.contains(new StudentGroup(Color.RED, 1)));
    }
}