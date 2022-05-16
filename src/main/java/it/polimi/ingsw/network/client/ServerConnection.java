package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.viewStates.GameViewState;
import it.polimi.ingsw.view.viewStates.HandshakeViewState;
import it.polimi.ingsw.view.viewStates.ViewState;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection extends Connection {
    private final Client client;
    private final CLI cli;
    private boolean inGame;

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
        boolean valid = false;

        while(!valid) {
            cli.handleInteraction();

            String response = receiveMessage();

            JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

            if (!jo.has("error")) {
                new ResponseParameters().deserialize(jo);
                valid = true;
            }

            AnsiConsole.sysOut().println(jo.get("error"));
        }

        lobbySequence();
    }

    /** PER QUANDO IN LOBBY
     * LOOP CHE CONTINUA A LEGGERE E FA LA DESERIALIZE SU UN THREAD SEPARATO
     * ALTRO THREAD CONTINUA A FARE IL LOOP DELLA VIEW FINO A CHE NON SEI READY,
     * A QUEL PUNTO STAMPI OGNI NOTIFY
     */
    private void lobbySequence() {
        while(!inGame) {
            String response = receiveMessage();

            executor.submit( () -> {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                if(jsonObject.has("matchInfo")) {
                    JsonObject matchInfoJson = jsonObject.get("matchInfo").getAsJsonObject();
                    if(matchInfoJson.get("gameStatus").getAsString().equalsIgnoreCase("IN_GAME")) {
                        synchronized (cli) {
                            inGame = true;
                            new ResponseParameters().deserialize(jsonObject);
                        }
                        return;
                    }
                }
                synchronized (cli) {
                    new ResponseParameters().deserialize(jsonObject);
                    cli.handleInteraction();
                }
            });
        }
        gameSequence();
    }

    /** PER QUANDO IN GAME
     * LOOP CHE LEGGE, QUANDO LEGGE QUALCOSA FA LA DESERIALZE A MENO CHE NON SIA UN ERRORE
     * NEXT_VIEW_STATE A MENO CHE NON SIA UN ERRORE
     * SE NEXT_VIEW_STATE RITORNA TRUE VAI A FARE UN INTERAZIONE DELLA NUOVA VIEW
     * ALTRIMENTI CONTINUI A LEGGERE
     */
    private void gameSequence() {
        cli.setViewState(new GameViewState(cli.getViewState()));

        while(inGame) {
            String response = receiveMessage();

            executor.submit( () -> {
               JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
               synchronized (cli) {
                   boolean reqInteraction = cli.nextState(jsonObject);

                   if(reqInteraction) {
                       cli.handleInteraction(); //Handle interaction in new view
                   }
               }
            });
        }
    }
}
