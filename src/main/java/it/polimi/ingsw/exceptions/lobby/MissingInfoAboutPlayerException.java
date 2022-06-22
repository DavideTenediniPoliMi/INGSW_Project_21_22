package it.polimi.ingsw.exceptions.lobby;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.model.Player;

/**
 * Exception thrown when missing some info (like card back or team color) about the player in the Lobby
 */
public class MissingInfoAboutPlayerException extends EriantysException {
    public MissingInfoAboutPlayerException(Player player) {
        super("Player cannot be ready! Missing choice of "
                + ((player.getCardBack() == null) ? "CardBack" : "")
                + ((player.getCardBack() == null && player.getTeamColor() == null) ? ", " : "")
                + ((player.getTeamColor() == null) ? "Team Color" : ""));
    }
}
