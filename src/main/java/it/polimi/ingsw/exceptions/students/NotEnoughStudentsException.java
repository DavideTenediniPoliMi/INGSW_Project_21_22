package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.Color;

public class NotEnoughStudentsException extends EriantysException {
    public NotEnoughStudentsException(Color c) {
        super("Not enough " + c + " students");
    }
}
