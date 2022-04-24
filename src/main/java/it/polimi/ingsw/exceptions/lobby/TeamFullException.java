package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Exception thrown when trying to join a team that is full.
 */
public class TeamFullException extends EriantysException {
    public TeamFullException(TowerColor towerColor) {
        super("Team " + towerColor + " is full!");
    }
}
