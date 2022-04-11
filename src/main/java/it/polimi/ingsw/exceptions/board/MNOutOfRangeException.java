package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.exceptions.EriantysException;

public class MNOutOfRangeException extends EriantysException {
    public MNOutOfRangeException(int requested, int maxSteps) {
        super("Tried moving MN " + requested + ", max movement allowed: " + maxSteps);
    }
}
