package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client();
        // IF CLI THEN USE client.run(new CLI(null))
        // WHICH IS THE OVERLOADED METHOD
        client.run();
    }
}
