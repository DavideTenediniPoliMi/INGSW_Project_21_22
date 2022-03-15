package it.polimi.ingsw.model.rules;

public class FourPlayersRules extends Rules{

    public FourPlayersRules(boolean expertModeActive) {
        super(expertModeActive, 4);
    }

    // TODO
    public void initialization() {}

    protected void distributeInitialStudents() {}

    protected void distributeInitialStudentsToClouds() {}

    protected void distributeInitialStudentsToIslands() {}

    protected void distributeInitialStudentsToSchools() {}
}
