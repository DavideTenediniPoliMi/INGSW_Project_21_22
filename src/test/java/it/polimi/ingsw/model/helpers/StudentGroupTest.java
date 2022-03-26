package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import junit.framework.TestCase;

public class StudentGroupTest extends TestCase {

    StudentGroup studentGroup;
    StudentGroup studentGroup1;

    public void setUp(){
        studentGroup = new StudentGroup(Color.BLUE, 4);
        studentGroup1 = new StudentGroup();
    }

    public void tearDown(){
        studentGroup = null;
    }

    public void testGetByColor() {
        assertEquals(4, studentGroup.getByColor(Color.BLUE));
    }

    public void testAddByColor() {
        studentGroup.addByColor(Color.BLUE, 1);
        assertEquals(5, studentGroup.getByColor(Color.BLUE));
    }

    public void testTransferAllTo() {
        studentGroup.transferAllTo(studentGroup1);
        assertEquals(4, studentGroup1.getByColor(Color.BLUE));
        assertEquals(0, studentGroup.getByColor(Color.BLUE));
    }

    public void testTransferTo() {
        StudentGroup tmp = new StudentGroup(Color.BLUE, 1);
        studentGroup.transferTo(studentGroup1, tmp);

        assertEquals(1, studentGroup1.getByColor(Color.BLUE));
        assertEquals(3, studentGroup.getByColor(Color.BLUE));

    }
}