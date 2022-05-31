package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.view.View;

public class Client {
    public Client() {
    }

    public void run(View view) {

        view.loadStartingScreen();

        Connection connection = view.handleBinding(this);

        if(connection != null) {
            connection.run();
        }

        view.loadClosingScreen();
    }
}
