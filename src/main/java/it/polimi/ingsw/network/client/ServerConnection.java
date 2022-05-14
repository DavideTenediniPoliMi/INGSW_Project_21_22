package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Connection;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Connection {
    private final Client client;

    public ServerConnection(Socket socket, Client client) throws IOException {
        super(socket);

        this.client = client;
    }

    @Override
    public void run() {

    }
}
