package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;

public abstract class Island {
    private final StudentGroup students = new StudentGroup();
    protected TowerColor teamColor;
    private boolean motherNature;

    public TowerColor getTeamColor() {
        return teamColor;
    }

    public boolean isMotherNatureOnIsland() {
        return motherNature;
    }

    public int getNumStudentsByColor(Color c){
        return students.getByColor(c);
    }

    public abstract int getNumIslands();

    protected void addStudents(StudentGroup students) {
        students.transferAllTo(this.students);
    }

    protected abstract void conquerIsland(TowerColor teamColor);

    protected void setMotherNatureTo(boolean motherNature) {
        this.motherNature = motherNature;
    }
}
