package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

public class PlayerAlreadyConnectedException extends EriantysException {
    public PlayerAlreadyConnectedException(String name) {
        super("Player with name \"" + name + "\" is already connected.");
    }
}
