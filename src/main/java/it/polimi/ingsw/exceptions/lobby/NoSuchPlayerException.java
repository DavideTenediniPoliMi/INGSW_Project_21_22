package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

public class NoSuchPlayerException extends EriantysException {
    public NoSuchPlayerException(int ID) {
        super("No Player with ID: " + ID + " was found.");
    }
}
