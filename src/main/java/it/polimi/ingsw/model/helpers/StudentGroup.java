package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup {
    private int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS]; // already initialized to 0
    }

}