package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when trying to collect students from an unavailable <code>Cloud</code>
 */
public class CloudUnavailableException extends EriantysException {
    public CloudUnavailableException(int cloudIndex) {
        super("Cloud " + cloudIndex + " unavailable!");
    }
}
