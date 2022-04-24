package it.polimi.ingsw.exceptions.player;

import it.polimi.ingsw.exceptions.EriantysException;

public class NameTakenException extends EriantysException {
    public NameTakenException(String message) {
        super("Name: " + message + " is already taken!");
    }
}
