package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * Exception thrown when trying to move more students than the ones owned by the specified <code>Player</code>.
 */
public class NotEnoughStudentsException extends EriantysException {
    public NotEnoughStudentsException(Color c) {
        super("Not enough " + c + " students");
    }
}
