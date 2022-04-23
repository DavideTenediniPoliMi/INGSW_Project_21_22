package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * Exception thrown when trying to move more students than the ones owned by the specified <code>Player</code>.
 */
public class NotEnoughStudentsException extends EriantysRuntimeException {
    public NotEnoughStudentsException(Color c) {
        super("Not enough " + c + " students");
    }
}
