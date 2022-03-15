package it.polimi.ingsw.model.rules;

public class TwoPlayersRules extends Rules{

    public TwoPlayersRules(boolean expertModeActive) {
        super(expertModeActive, 2);
    }

    // TODO
    public void initialization() {}

    protected void distributeInitialStudents() {}

    protected void distributeInitialStudentsToClouds() {}

    protected void distributeInitialStudentsToIslands() {}

    protected void distributeInitialStudentsToSchools() {}
}
