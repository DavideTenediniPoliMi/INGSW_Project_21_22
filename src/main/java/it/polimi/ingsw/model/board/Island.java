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

}
