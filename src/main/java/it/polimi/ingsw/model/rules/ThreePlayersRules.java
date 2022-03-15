package it.polimi.ingsw.model.rules;

public class ThreePlayersRules extends Rules{

    public ThreePlayersRules(boolean expertModeActive) {
        super(expertModeActive, 3);
    }

    // TODO
    public void initialization() {}

    protected void distributeInitialStudents() {}

    protected void distributeInitialStudentsToClouds() {}

    protected void distributeInitialStudentsToIslands() {}

    protected void distributeInitialStudentsToSchools() {}
}
