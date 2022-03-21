package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class Island {
    private final StudentGroup students;
    private int numTowers = 0;
    private TowerColor towerColor = null;
    private boolean motherNature;

    public Island(boolean motherNature) {
        this.students = new StudentGroup();
        this.motherNature = motherNature;
    }

    public StudentGroup getStudents() {
        return students;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public boolean isMotherNature() {
        return motherNature;
    }

    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }
}
