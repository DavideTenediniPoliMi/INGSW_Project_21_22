package it.polimi.ingsw.view.CLI;

import org.fusesource.jansi.AnsiConsole;

public class CLI {


        public static void main(String[] args){
            AnsiConsole.systemInstall();
            AnsiConsole.sysOut().println(AnsiCodes.CLS);
            AnsiConsole.sysOut().println
                    (AnsiCodes.HOME + "┌─────────┐");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│ " + AnsiCodes.BROWN_BACKGROUND + " 6 " + AnsiCodes.RESET + " " + AnsiCodes.GREEN_BACKGROUND_BRIGHT + AnsiCodes.BLACK_TEXT + " 3 " + AnsiCodes.RESET + " │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("└─────────┘");
            AnsiConsole.systemInstall();
        }
}
