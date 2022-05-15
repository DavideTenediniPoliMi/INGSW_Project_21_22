package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
