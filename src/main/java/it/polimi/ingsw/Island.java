package it.polimi.ingsw;

public class Island {
    private final StudentCounter students;
    private int numTowers = 0;
    private TowerColor towerColor = null;
    private boolean motherNature;

    public Island(boolean motherNature) {
        this.students = new StudentCounter();
        this.motherNature = motherNature;
    }

    public StudentCounter getStudents() {
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
