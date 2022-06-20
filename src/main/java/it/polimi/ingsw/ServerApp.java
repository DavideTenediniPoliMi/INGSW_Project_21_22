package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.lobby.GameFullException;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) throws IOException, InterruptedException, PlayerAlreadyConnectedException, GameFullException {
        Server server = new Server();
        //Runtime.getRuntime().addShutdownHook(new Thread ( () -> server.saveDataToFile()));
        server.run();
    }
}
