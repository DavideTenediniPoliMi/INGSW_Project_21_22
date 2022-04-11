package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysException;

public class StudentTransferException extends EriantysException {
    public StudentTransferException(String err) {
        super("Error transfering students: " + err);
    }
}
