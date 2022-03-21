package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup {
    private final int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS]; // already initialized to 0
    }

    public StudentGroup(Color c, int amount) {
        this();
        addByColor(c, amount);
    }

    public int getByColor(Color c){
        return students[c.ordinal()];
    }

    public void addByColor(Color c, int amount){
        students[c.ordinal()] += amount;
    }

    public void transferAllTo(StudentGroup recipient){
        transferTo(recipient, this);
    }

    public void transferTo(StudentGroup recipient, StudentGroup toTransfer){
        int amount;
        for(Color c : Color.values()){
            amount = toTransfer.getByColor(c);
            if(this.getByColor(c) >= amount){
                students[c.ordinal()] -= amount;
                recipient.addByColor(c, amount);
            }
        }
    }

}
