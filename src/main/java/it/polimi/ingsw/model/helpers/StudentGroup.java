package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.Arrays;

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

    @Override
    public Object clone() {
        StudentGroup temp = new StudentGroup();

        System.arraycopy(students, 0, temp.students, 0, Color.NUM_COLORS);

        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGroup that = (StudentGroup) o;
        return Arrays.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(students);
    }
}
