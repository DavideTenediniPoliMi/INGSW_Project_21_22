package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysException;

public class EmptyBagException extends EriantysException {
    public EmptyBagException() {
        super("Student bag is empty!");
    }
}
