package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import junit.framework.TestCase;

public class StudentBagTest extends TestCase {
    private StudentBag studentBag;

    public void setUp() throws Exception {
        studentBag = new StudentBag();
    }

    public void tearDown() throws Exception {
        studentBag = null;
    }

    public void testDrawStudents() {
        int numStudents = 1;
        StudentGroup studentGroup = studentBag.drawStudents(numStudents);

        int temp = 0;
        for(Color c : Color.values()) {
            temp += studentGroup.getByColor(c);
        }
        assertEquals(temp, numStudents);
    }

    public void testPutStudentsBack() {
        StudentGroup studentGroup = new StudentGroup(Color.BLUE, 1);

        studentBag.putStudentsBack(studentGroup);

        assertEquals(0, studentGroup.getByColor(Color.BLUE));
    }
}