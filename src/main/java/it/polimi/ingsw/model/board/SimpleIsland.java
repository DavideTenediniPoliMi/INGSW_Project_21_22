package it.polimi.ingsw.model.board;

/**
 * Class representing a single Island
 */
public class SimpleIsland extends Island {
    /**
     * Sole constructor
     */
    public SimpleIsland() {
    }

    @Override
    public int getNumIslands() {
        return 1;
    }
}
