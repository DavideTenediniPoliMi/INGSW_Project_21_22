package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client();

        client.run(new CLI(null));
    }
}
