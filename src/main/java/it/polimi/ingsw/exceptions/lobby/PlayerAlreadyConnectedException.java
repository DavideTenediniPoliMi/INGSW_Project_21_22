package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;

/**
 * Exception thrown when a player with the same name is already connected to the server
 */
public class PlayerAlreadyConnectedException extends EriantysException {
    public PlayerAlreadyConnectedException(String name) {
        super("Player with name \"" + name + "\" is already connected.");
    }
}
