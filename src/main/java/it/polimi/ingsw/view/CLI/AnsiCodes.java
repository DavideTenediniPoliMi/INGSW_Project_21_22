package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.enumerations.Color;

public enum AnsiCodes {
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),// BLUE
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"),// GREEN
    PURPLE_BACKGROUND_BRIGHT("\033[0;105m"), // PURPLE
    RED_BACKGROUND_BRIGHT("\033[0;101m"),// RED
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),// YELLOW
    CLS("\u001b[2J"),
    HOME("\u001b[H"),
    RESET("\033[0m");  // Text Reset

    final String code;

    AnsiCodes(String code){
      this.code = code;
    }

    public static String getColor(Color color) {
        return values()[color.ordinal()].toString();
    }

    @Override
    public String toString() {
        return code;
    }
}
