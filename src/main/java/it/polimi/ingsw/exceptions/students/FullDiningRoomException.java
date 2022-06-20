package it.polimi.ingsw.exceptions.students;

import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.model.enumerations.Color;

public class FullDiningRoomException extends EriantysRuntimeException {
    public FullDiningRoomException(Color color) {
        super(color + "students' dining room is full");
    }
}
