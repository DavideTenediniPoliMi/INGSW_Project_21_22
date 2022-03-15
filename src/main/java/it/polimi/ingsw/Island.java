package it.polimi.ingsw;

public class Island {
    private final StudentHolder students;
    private int numTowers = 0;
    private TowerColor towerColor = null;
    private boolean motherNature;

    public Island(boolean motherNature) {
        this.students = new StudentHolder();
        this.motherNature = motherNature;
    }

    public StudentHolder getStudents() {
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
