package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;

public class ProfessorTracker implements Cloneable {
    private final int[] owners;

    public ProfessorTracker() {
        owners = new int[Color.NUM_COLORS];

        for(int i = 0; i < Color.NUM_COLORS; i++) {
            owners[i] = -1; // -1 indicates that the professor doesn't have an owner yet
        }
    }

    public int getOwnerIDByColor(Color c) {
        return owners[c.ordinal()];
    }

    protected void setOwnerIDByColor(int playerID, Color c) {
        owners[c.ordinal()] = playerID;
    }

    @Override
    public Object clone() {
        ProfessorTracker temp = new ProfessorTracker();

        System.arraycopy(owners, 0, temp.owners, 0, Color.NUM_COLORS);

        return temp;
    }
}
