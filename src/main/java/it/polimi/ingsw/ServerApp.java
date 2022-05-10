package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread ( () -> server.saveDataToFile()));
        server.run();
        Thread.sleep(10000);
    }
}
