package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.view.cli.AnsiCodes;

/**
 * Class to hold the different colors for towers in the game.
 */
public enum TowerColor implements Printable<String> {
    BLACK,
    WHITE,
    GREY;


    @Override
    public String print(boolean...params) {
        return AnsiCodes.getTeamColor(this) + " T " + AnsiCodes.RESET;
    }
}
