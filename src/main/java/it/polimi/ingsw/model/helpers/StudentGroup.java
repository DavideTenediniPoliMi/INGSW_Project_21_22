package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup {
    private int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS]; // already initialized to 0
    }

    public int getByColor(Color c){
        return -1;
    }

    public void addByColor(Color c){

    }

    public void transferTo(StudentGroup recipient, StudentGroup toTransfer){

    }

    public void transferAllTo(StudentGroup recipient){

    }
}
