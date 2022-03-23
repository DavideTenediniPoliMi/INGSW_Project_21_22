package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.TowerColor;

public class SimpleIsland extends Island {
    public SimpleIsland() {
    }

    @Override
    public int getNumIslands() {
        return 1;
    }
}
