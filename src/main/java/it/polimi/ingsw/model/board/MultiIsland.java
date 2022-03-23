package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.TowerColor;

public class MultiIsland extends Island {
    private final Island leftIsland;
    private final Island rightIsland;

    public MultiIsland(Island leftIsland, Island rightIsland) {
        this.leftIsland = leftIsland;
        this.rightIsland = rightIsland;
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
