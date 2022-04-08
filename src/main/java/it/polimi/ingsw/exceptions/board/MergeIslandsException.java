package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;

public class MergeIslandsException extends EriantysRuntimeException {
    public MergeIslandsException(int island1, int island2) {
        super("Error merging islands: " + island1 + ", " + island2);
    }
}
