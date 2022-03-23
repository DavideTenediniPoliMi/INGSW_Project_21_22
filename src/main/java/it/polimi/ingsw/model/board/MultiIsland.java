package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class MultiIsland extends Island {
    private final Island leftIsland;
    private final Island rightIsland;

    public MultiIsland(Island leftIsland, Island rightIsland) {
        this.leftIsland = leftIsland;
        this.rightIsland = rightIsland;

        super.conquerIsland(leftIsland.getTeamColor());
        setMotherNatureTo(true);
    }

    @Override
    public int getNumStudentsByColor(Color c) {
        return super.getNumStudentsByColor(c)
                + leftIsland.getNumStudentsByColor(c)
                + rightIsland.getNumStudentsByColor(c);
    }

    @Override
    protected void conquerIsland(TowerColor teamColor) {
        super.conquerIsland(teamColor);

        leftIsland.conquerIsland(teamColor);
        rightIsland.conquerIsland(teamColor);
    }

    @Override
    public int getNumIslands() {
        return leftIsland.getNumIslands() + rightIsland.getNumIslands();
    }
}
