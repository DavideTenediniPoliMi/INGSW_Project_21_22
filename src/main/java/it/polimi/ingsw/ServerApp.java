package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.lobby.GameFullException;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

/**
 * Class representing the Server application.
 */
public class ServerApp {
    private static final int DEFAULT_PORT = 12345;

    public static void main(String[] args) throws IOException, InterruptedException, PlayerAlreadyConnectedException, GameFullException {
        Server server;
        int port = DEFAULT_PORT;
        for(int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-p") && i+1 < args.length) {
                try{
                    port = Integer.parseInt(args[1]);
                    if(port < 0 || port > 65535) {
                        System.out.println("Invalid port: " + args[1] + "\nexiting...");
                        System.exit(1);
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port: " + args[1] + "\nexiting...");
                    System.exit(1);
                }
            }
        }
        server = new Server(port);
        //Runtime.getRuntime().addShutdownHook(new Thread ( () -> server.saveDataToFile()));
        server.run();
    }
}
