package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Class that contains the <code>AnsiCodes</code> for the color to be printed in the CLI
 */
public enum AnsiCodes {
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),// BLUE
    GREEN_BACKGROUND_BRIGHT("\033[42m"),// GREEN
    PURPLE_BACKGROUND_BRIGHT("\033[0;105m"), // PURPLE
    RED_BACKGROUND_BRIGHT("\033[0;101m"),// RED
    //YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),// YELLOW
    YELLOW_BACKGROUND_BRIGHT("\u001b[48;5;220m"),// YELLOW
    //YELLOW_BACKGROUND_BRIGHT("\033[43m"),// YELLOW
    BROWN_BACKGROUND("\u001b[48;5;130m"),
    LIGHT_GREEN_BACKGROUND("\u001b[48;5;194m"),
    BLUE_TEXT("\033[1;94m"),
    GREEN_TEXT("\033[1;32m"),
    PURPLE_TEXT("\033[1;95m"),
    RED_TEXT("\033[1;91m"),
    YELLOW_TEXT("\033[1;33m"),
    BLACK_TEXT("\033[1;90m"),
    WHITE_TEXT("\033[0;37m"),
    WHITE_BACKGROUND("\033[47m"),
    BLACK_BACKGROUND("\033[40m"),
    GRAY_BACKGROUND("\033[0;100m"),
    CLS("\u001b[2J"),
    HOME("\u001b[H"),
    RESET("\033[0m"),  // Text Reset
    REVERSED("\u001b[7m"),
    COIN("\u25ce");

    final String code;

    AnsiCodes(String code){
      this.code = code;
    }

    /**
     * Returns the <code>String</code> of the background color to be appended corresponding to the
     * specified <code>Color</code>
     *
     * @param color <code>Color</code> that has to be converted in AnsiCodes string
     *
     * @return <code>String</code> of the background color corresponding to the specified <code>Color</code>
     */
    public static String getBackgroundColor(Color color) {
        switch (color) {
            case BLUE:
                return BLUE_BACKGROUND_BRIGHT.code;
            case GREEN:
                return GREEN_BACKGROUND_BRIGHT.code;
            case RED:
                return RED_BACKGROUND_BRIGHT.code;
            case PINK:
                return PURPLE_BACKGROUND_BRIGHT.code;
            case YELLOW:
                return YELLOW_BACKGROUND_BRIGHT.code + BLACK_TEXT.code;
            default:
                return RESET.code;
        }
    }

    /**
     * Returns the <code>String</code> of the text color to be appended corresponding to the specified <code>Color</code>
     *
     * @param color <code>Color</code> that has to be converted in AnsiCodes string
     *
     * @return <code>String</code> of the text color corresponding to the specified <code>Color</code>
     */
    public static String getTextColor(Color color) {
        switch (color) {
            case BLUE:
                return BLUE_TEXT.code;
            case GREEN:
                return GREEN_TEXT.code;
            case RED:
                return RED_TEXT.code;
            case PINK:
                return PURPLE_TEXT.code;
            case YELLOW:
                return YELLOW_TEXT.code;
            default:
                return RESET.code;
        }
    }

    /**
     * Returns the <code>String</code> of the combination of color to represent a team
     * corresponding to the specified <code>Color</code>
     *
     * @param color <code>Color</code> that has to be converted in AnsiCodes string
     *
     * @return <code>String</code> of the combination of color to represent a team
     * corresponding to the specified <code>Color</code>
     */
    public static String getTeamColor(TowerColor color) {
        switch (color){
            case BLACK:
                return BLACK_BACKGROUND.code;
            case WHITE:
                return WHITE_BACKGROUND.code + BLACK_TEXT.code;
            case GREY:
                return GRAY_BACKGROUND.code;
            default:
                return RESET.code;
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
