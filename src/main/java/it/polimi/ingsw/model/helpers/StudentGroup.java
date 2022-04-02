package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentGroup implements Cloneable{
    private final int[] students;

    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS];
    }

    public StudentGroup(Color c, int amount) {
        this();
        addByColor(c, amount);
    }

    public int getByColor(Color c) {
        return students[c.ordinal()];
    }

    public void addByColor(Color c, int amount) {
        students[c.ordinal()] += amount;
    }

    public void transferAllTo(StudentGroup recipient) {
        transferTo(recipient, this);
    }

    public void transferTo(StudentGroup recipient, StudentGroup toTransfer) {
        for(Color c : Color.values()) {
            int amount = toTransfer.getByColor(c);

            if(getByColor(c) >= amount) {
                students[c.ordinal()] -= amount;
                recipient.addByColor(c, amount);
            }
        }
    }

    public StudentGroup clone() {
        StudentGroup temp = new StudentGroup();
        for(Color c: Color.values()) {
            temp.addByColor(c, this.getByColor(c));
        }
        return temp;
    }
}
