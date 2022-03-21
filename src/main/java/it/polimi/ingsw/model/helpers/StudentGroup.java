package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup {
    private int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS]; // already initialized to 0
    }

    public int getByColor(Color c) {
        return students[c.ordinal()];
    }

    public void addByColor(Color c, int numStudents) {
        students[c.ordinal()] += numStudents;
    }

    public void transferByColorTo(StudentGroup recipient, Color c, int numStudents) {
        // TODO
    }

    public void transferAllTo(StudentGroup recipient) {
        // TODO
    }
}
