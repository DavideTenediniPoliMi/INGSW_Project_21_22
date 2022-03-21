package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup {
    private int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS]; // already initialized to 0
    }

    public int getByColor(Color c){
        return students[c.ordinal()];
    }

    public void addByColor(Color c, int amt){
        students[c.ordinal()] += 1;
    }

    public void transferAllTo(StudentGroup recipient){
        transferTo(recipient, this);
    }

    public void transferTo(StudentGroup recipient, StudentGroup toTransfer){
        int amt;
        for(Color c : Color.values()){
            amt = toTransfer.getByColor(c);
            if(this.getByColor(c) >= amt){
                students[c.ordinal()] -= amt;
                recipient.addByColor(c, amt);
            }
        }
    }

}
