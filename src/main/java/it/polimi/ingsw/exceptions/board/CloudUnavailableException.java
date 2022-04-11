package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.exceptions.EriantysException;

public class CloudUnavailableException extends EriantysException {
    public CloudUnavailableException(int cloudIndex) {
        super("Cloud " + cloudIndex + " unavailable!");
    }
}
