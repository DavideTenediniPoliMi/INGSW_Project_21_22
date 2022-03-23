package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.TowerColor;

public class MultiIsland extends Island {
    private Island leftIsland;
    private Island rightIsland;

    public MultiIsland() {

    }

    @Override
    public int getNumIslands() {
        return leftIsland.getNumIslands() + rightIsland.getNumIslands();
    }

    @Override
    protected void conquerIsland(TowerColor teamColor) {
        leftIsland.conquerIsland(teamColor);
        rightIsland.conquerIsland(teamColor);
    }
}
