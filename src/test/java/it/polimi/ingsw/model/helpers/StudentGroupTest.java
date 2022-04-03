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
    }

    @Test
    public void testAddByColor() {
        studentGroup.addByColor(Color.BLUE, 1);
        assertEquals(5, studentGroup.getByColor(Color.BLUE));
    }

    @Test
    public void testTransferAllTo() {
        studentGroup.transferAllTo(studentGroup1);
        assertEquals(4, studentGroup1.getByColor(Color.BLUE));
        assertEquals(0, studentGroup.getByColor(Color.BLUE));
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
        StudentGroup clone = s.clone();
        assertEquals(1, clone.getByColor(Color.BLUE));
    }
}