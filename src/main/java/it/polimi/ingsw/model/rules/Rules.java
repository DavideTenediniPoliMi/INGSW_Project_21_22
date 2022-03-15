package it.polimi.ingsw.model.rules;

import it.polimi.ingsw.model.player.Player;

public abstract class Rules {
    private final boolean expertModeActive;
    private final int numMovableStudents;

    public Rules(boolean expertModeActive, int numMovableStudents) {
        this.expertModeActive = expertModeActive;
        this.numMovableStudents = numMovableStudents;
    }

    public boolean isExpertModeActive() {
        return expertModeActive;
    }

    public int getNumMovableStudents() {
        return numMovableStudents;
    }

    public void giveInitialCoinAmount(Player player) {
        if(!isExpertModeActive()) return;

        player.addCoins(1);
    }

    public void checkWinConditions() {
        // TODO
    }

    public abstract void initialization();

    protected abstract void distributeInitialStudents();

    protected abstract void distributeInitialStudentsToClouds();

    protected abstract void distributeInitialStudentsToIslands();

    protected abstract void distributeInitialStudentsToSchools();
}
