package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;

/**
 * Class used to track the owners of the various professors.
 */
public class ProfessorTracker implements Cloneable {
    private final int[] owners;

    /**
     * Sole constructor, sets each <code>Color</code> to <code>null</code> owner (ID -1)
     */
    public ProfessorTracker() {
        owners = new int[Color.NUM_COLORS];

        for(int i = 0; i < Color.NUM_COLORS; i++) {
            owners[i] = -1; // -1 indicates that the professor doesn't have an owner yet
        }
    }

    /**
     * Returns the ID of the owner of the specified <code>Color</code>
     * @param c the <code>Color</code> to check
     * @return ID of the owner of the professor
     */
    public int getOwnerIDByColor(Color c) {
        return owners[c.ordinal()];
    }

    /**
     * Sets new owner ID for specified professor <code>Color</code>
     * @param playerID the ID of the new owner
     * @param c the <code>Color</code> of the professor
     */
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
