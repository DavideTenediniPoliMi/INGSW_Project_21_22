package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;

public abstract class Island {
    private final StudentGroup students = new StudentGroup();
    private TowerColor teamColor;
    private boolean motherNature;

    protected void addStudents(StudentGroup toAdd) {
        toAdd.transferAllTo(students);
    }

    public TowerColor getTeamColor() {
        return teamColor;
    }

    private void setTeamColor(TowerColor teamColor) {
        this.teamColor = teamColor;
    }

    public boolean isMotherNatureOnIsland() {
        return motherNature;
    }

    protected void setMotherNatureTo(boolean motherNature) {
        this.motherNature = motherNature;
    }

    public int getNumStudentsByColor(Color c) {
        return students.getByColor(c);
    }

    protected void conquerIsland(TowerColor teamColor) {
        setTeamColor(teamColor);
    }

    public abstract int getNumIslands();
}
