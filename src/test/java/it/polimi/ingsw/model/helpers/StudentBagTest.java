package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class StudentBagTest extends TestCase {
    private final int TOT_STUDENTS = 120;
    private StudentBag studentBag;

    @BeforeEach
    public void setUp() throws Exception {
        studentBag = new StudentBag();
    }

    @AfterEach
    public void tearDown() throws Exception {
        studentBag = null;
    }

    @Test
    public void testDrawStudents() {
        int numStudents = 1;
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