package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class StudentBagTest {
    private final int TOT_STUDENTS = 120;
    private StudentBag studentBag;

    @BeforeEach
    public void setUp() throws Exception {
        studentBag = new StudentBag(24);
    }

    @AfterEach
    void tearDown() {
        studentBag = null;
    }

    @Test
    public void testDrawStudents() {
        assertFalse(studentBag.isEmpty());
        int numStudents = 3;
        StudentGroup studentGroup = studentBag.drawStudents(numStudents);

        int temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }
        assertEquals(temp, numStudents);
    }

    @Test
    public void testDrawStudents_overflow() {
        int numStudents = 150;
        StudentGroup studentGroup = studentBag.drawStudents(numStudents);

        int temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }
        assertEquals(temp, TOT_STUDENTS);
    }

    @Test
    public void testDrawStudents_drawWhenEmpty() {
        StudentGroup studentGroup = studentBag.drawStudents(TOT_STUDENTS);

        int temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }
        assertEquals(temp, TOT_STUDENTS);

        assertTrue(studentBag.isEmpty());

        studentGroup = studentBag.drawStudents(1);
        temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }

        assertEquals(temp, 0);
    }

    @Test
    public void testDrawStudents_refillAndDraw() {
        StudentGroup studentGroup = studentBag.drawStudents(TOT_STUDENTS);

        int temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }
        assertEquals(temp, TOT_STUDENTS);

        studentBag.putStudentsBack(studentGroup);
        assertFalse(studentBag.isEmpty());

        studentGroup = studentBag.drawStudents(10);
        temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }

        assertEquals(temp, 10);
    }

    @Test
    public void testPutStudentsBack() {
        StudentGroup studentGroup = new StudentGroup(Color.BLUE, 1);

        studentBag.putStudentsBack(studentGroup);

        assertEquals(0, studentGroup.getByColor(Color.BLUE));
    }
}