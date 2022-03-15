package it.polimi.ingsw;

public class ProfessorTracker {
    private int[] owners;

    public ProfessorTracker() {
        this.owners = new int[Color.NUM_COLORS];
        for(int i = 0; i < Color.NUM_COLORS; i++) {
            owners[i] = -1;
        }
    }

    public int getOwnerIDByColor(Color c) {
        return owners[c.ordinal()];
    }

    public void setOwnerIDByColor(Color c, int studentID) {
        owners[c.ordinal()] = studentID;
    }
}
