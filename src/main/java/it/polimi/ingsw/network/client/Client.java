package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.view.cli.AnsiCodes;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
import org.fusesource.jansi.AnsiConsole;

/**
 * Class that handles GUI and CLI lifecycle.
 */
public class Client {
    public Client() {
    }

    public void run(CLI view) {

        view.loadStartingScreen();

        while(!view.isExiting()) {
            Connection connection = view.handleBinding();

            if(connection != null) {
                connection.run();
                AnsiConsole.sysOut().println(AnsiCodes.CLS);
            }
        }

        view.loadClosingScreen();
        System.exit(0);
    }

    public void run() {
        GUI.main(null);
    }
}
