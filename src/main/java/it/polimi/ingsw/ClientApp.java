package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
        //GUI.main(args);
    }
}
