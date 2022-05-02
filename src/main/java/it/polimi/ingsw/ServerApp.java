package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
