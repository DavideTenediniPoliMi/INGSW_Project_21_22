package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.view.cli.AnsiCodes;
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
    private final Scanner scanner = new Scanner(System.in);

    public Client() {}

    public void run() {
        AnsiConsole.systemInstall();
        AnsiConsole.sysOut().print(AnsiCodes.CLS + "" + AnsiCodes.HOME);
        AnsiConsole.sysOut().println("Welcome to Eriantys!");

        do {
            AnsiConsole.sysOut().println("Insert the IP address of the server : (Type X to close the game)");
            String ip = scanner.nextLine();

            if(ip.equals("X")) break;

            AnsiConsole.sysOut().println("Insert the port :");
            int port = scanner.nextInt();
            scanner.nextLine();

            try {
                ServerConnection serverConnection = new ServerConnection(new Socket(ip, port), this);
                serverConnection.run();
            } catch(IOException e) {
                AnsiConsole.sysOut().println("The combination IP/Port didn't work! Try again!");
            } catch(Exception e) {
                AnsiConsole.sysOut().println("Something went wrong! Try again!");
            }
        } while(true);
        AnsiConsole.systemUninstall();
    }
}
