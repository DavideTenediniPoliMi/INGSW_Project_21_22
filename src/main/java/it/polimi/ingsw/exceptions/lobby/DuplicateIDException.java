package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

public class DuplicateIDException extends EriantysException {
    public DuplicateIDException(int ID) {
        super("There is already another player with ID: " + ID);
    }
}
