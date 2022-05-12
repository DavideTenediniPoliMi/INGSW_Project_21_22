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
                    ("│ " + AnsiCodes.YELLOW_BACKGROUND_BRIGHT + " 6 " + AnsiCodes.RESET + " " + AnsiCodes.PURPLE_BACKGROUND_BRIGHT + " 3 " + AnsiCodes.RESET + " │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("└─────────┘");
            AnsiConsole.systemInstall();
        }
}
