package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class Island {
    private final StudentGroup students = new StudentGroup();
    private int numTowers;
    private TowerColor teamColor;
    private boolean motherNature;

    public Island() {
    }

    public int getNumTowers() {
        return numTowers;
    }

    public TowerColor getTeamColor() {
        return teamColor;
    }

    public boolean isMotherNatureOnIsland() {
        return motherNature;
    }

    protected void addStudents(StudentGroup students) {
        students.transferAllTo(this.students);
    }

    protected void addTower() {
        numTowers += 1;
    }

    protected void conquerIsland(TowerColor teamColor) {
        this.teamColor = teamColor;
    }

    protected void setMotherNatureTo(boolean motherNature) {
        this.motherNature = motherNature;
    }
}
