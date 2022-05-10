package it.polimi.ingsw.view.CLI;

import org.fusesource.jansi.AnsiConsole;

public class CLI {
        public static final String ANSI_CLS = "\u001b[2J";
        public static final String ANSI_HOME = "\u001b[H";
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String RESET = "\033[0m";  // Text Reset


        public static void main(String[] args){
            AnsiConsole.systemInstall();
            AnsiConsole.sysOut().println(ANSI_CLS);
            AnsiConsole.sysOut().println
                    (ANSI_HOME + "┌─────────┐");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│ " + RED_BACKGROUND_BRIGHT + " 6 " + RESET + " " + GREEN_BACKGROUND_BRIGHT + " 3 " + RESET + " │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("│         │");
            AnsiConsole.sysOut().println
                    ("└─────────┘");
            AnsiConsole.systemInstall();
        }
}
