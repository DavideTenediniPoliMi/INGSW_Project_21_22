package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to move Mother Nature more steps than the maximum allowed.
 */
public class MNOutOfRangeException extends EriantysException {
    public MNOutOfRangeException(int requested, int maxSteps) {
        super("Tried moving MN " + requested + ", max movement allowed: " + maxSteps);
    }
}
