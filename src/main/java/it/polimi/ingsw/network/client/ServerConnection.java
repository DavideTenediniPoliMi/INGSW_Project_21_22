package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.viewStates.HandshakeViewState;
import it.polimi.ingsw.view.viewStates.ViewState;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection extends Connection {
    private final Client client;
    private final CLI cli;

    public ServerConnection(Socket socket, Client client) throws IOException {
        super(socket);

        this.client = client;
        ViewState viewState = new ViewState();
        viewState.addObserver(this);

        cli = new CLI(new HandshakeViewState(viewState));
    }

    @Override
    public void run() {
        // THIS FIRST INTERACTION IS THE HANDSHAKE (name)
        cli.handleInteraction();

        String response = receiveMessage();

        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

        if (!jo.has("error")) {
            new ResponseParameters().deserialize(jo);
        }

        AnsiConsole.sysOut().println(jo.get("error"));
    }

    /** PER QUANDO IN GAME
     * LOOP CHE LEGGE, QUANDO LEGGE QUALCOSA FA LA DESERIALZE A MENO CHE NON SIA UN ERRORE
     * NEXT_VIEW_STATE A MENO CHE NON SIA UN ERRORE
     * SE C'è
     * SE NEXT_VIEW_STATE RITORNA TRUE VAI A FARE UN INTERAZIONE DELLA NUOVA VIEW
     * ALTRIMENTI CONTINUI A LEGGERE
     */

    /** PER QUANDO IN LOBBY
     * LOOP CHE CONTINUA A LEGGERE E FA LA DESERIALIZE SU UN THREAD SEPARATO
     * ALTRO THREAD CONTINUA A FARE IL LOOP DELLA VIEW FINO A CHE NON SEI READY,
     * A QUEL PUNTO STAMPI OGNI NOTIFY
     */
}
