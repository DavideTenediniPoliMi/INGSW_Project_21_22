package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when a student transfer was not completed.
 */
public class StudentTransferException extends EriantysException {
    public StudentTransferException(String err) {
        super("Error transfering students: " + err);
    }
}
