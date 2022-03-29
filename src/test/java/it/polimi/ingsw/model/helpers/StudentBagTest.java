package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class StudentBagTest extends TestCase {
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
    public void testPutStudentsBack() {
        StudentGroup studentGroup = new StudentGroup(Color.BLUE, 1);

        studentBag.putStudentsBack(studentGroup);

        assertEquals(0, studentGroup.getByColor(Color.BLUE));
    }
}