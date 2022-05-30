package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.cli.AnsiCodes;
import it.polimi.ingsw.view.cli.CLI;
import org.fusesource.jansi.AnsiConsole;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    public Client() {}

    public void run() {

        // THIS SHOULD BE IN A LOOP AND IN A IF TO SEE IF IT SHOULD BE EITHER CLI OR GUI
        AnsiConsole.systemInstall();

        AnsiConsole.sysOut().print(AnsiCodes.CLS + "" + AnsiCodes.HOME);
        AnsiConsole.sysOut().println("Welcome to Eriantys!");

        while(!CLI.exit) {
            Connection connection = CLI.handleBinding(this);

            if(connection != null)
                connection.run();
        }

        AnsiConsole.sysOut().println("Thanks for playing Eriantys!");

        AnsiConsole.systemUninstall();
    }
}
