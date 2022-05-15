package it.polimi.ingsw.view.views;

import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.utils.Printable;

public abstract class View implements Printable<String> {
    private ServerConnection serverConnection;
    public abstract String printCLIPrompt();
    public abstract String manageCLIInput(String input);
}
