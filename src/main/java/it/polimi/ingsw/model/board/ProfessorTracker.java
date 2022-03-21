package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;

public class ProfessorTracker {
    private final int[] owners;

    public ProfessorTracker() {
        this.owners = new int[Color.NUM_COLORS];

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
}
