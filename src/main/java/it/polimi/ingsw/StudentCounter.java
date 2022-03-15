package it.polimi.ingsw;

public class StudentCounter {
    private int[] counters;

    public StudentCounter() {
        this.counters = new int[Color.NUM_COLORS]; // already initialized to 0
    }

    public int getByColor(Color c) {
        return counters[c.ordinal()];
    }

    public void addByColor(Color c, int numStudents) {
        counters[c.ordinal()] += numStudents;
    }

    public void transferByColorTo(StudentCounter recipient, Color c, int numStudents) {
        // TODO
    }

    public void transferAllTo(StudentCounter recipient) {
        // TODO
    }
}
