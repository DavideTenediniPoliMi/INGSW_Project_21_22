package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.AnsiCodes;
import org.fusesource.jansi.AnsiConsole;

public class Client {
    public Client() {
    }

    public void run(View view) {

        view.loadStartingScreen();

        while(!view.isExiting()) {
            Connection connection = view.handleBinding(this);

            if(connection != null) {
                connection.run();
                AnsiConsole.sysOut().println(AnsiCodes.CLS);
            }
        }

        view.loadClosingScreen();
        System.exit(0);
    }
}
