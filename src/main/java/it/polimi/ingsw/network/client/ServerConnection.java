package it.polimi.ingsw.network.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.viewStates.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Connection {
    private final Client client;
    private final CLI cli;
    private boolean inGame;
    private boolean ready;

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
        while(true) {
            cli.handleHandshake();

            String response = receiveMessage();

            JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

            if (!jo.has("error")) {
                new ResponseParameters().deserialize(jo);
                break;
            }

            AnsiConsole.sysOut().println(jo.get("error"));
        }

        lobbySequence();
    }

    /** PER QUANDO IN LOBBY
     * LOOP CHE CONTINUA A LEGGERE E FA LA DESERIALIZE SU UN THREAD SEPARATO
     * ALTRO THREAD CONTINUA A FARE IL LOOP DELLA VIEW FINO A CHE NON SEI READY,
     * A QUEL PUNTO STAMPI OGNI NOTIFY
     *
     * Check se la lobby esiste già o no, quando joini checka finchè non ti arriva un messaggio con il tuo nome tra i player
     */
    private void lobbySequence() {
        if(MatchInfo.getInstance().getSelectedNumPlayer() == 0) {
            //Begin lobby creation sequence
            cli.setViewState(new NoLobbyViewState(cli.getViewState()));
            cli.handleInteraction();
            while(true) {
                String received = receiveMessage();
                JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

                if(!jsonObject.has("error")) { //Lobby was created
                    new ResponseParameters().deserialize(jsonObject);
                    break;
                }
            }
        }

        //Force connection to lobby
        RequestParameters requestParameters = new RequestParameters()
                .setCommandType(CommandType.JOIN);
        update(requestParameters.serialize().toString());
        boolean joined = false;
        while(!joined) {
            String received = receiveMessage();
            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
            if(jsonObject.has("players")) {
                JsonArray players = jsonObject.get("players").getAsJsonArray();
                for(JsonElement player : players) {
                    if(player.getAsJsonObject().get("name").getAsString().equalsIgnoreCase(cli.getName())) {
                        joined = true;
                        cli.setPlayerID(player.getAsJsonObject().get("ID").getAsInt());
                        new ResponseParameters().deserialize(jsonObject);
                        break;
                    }
                }
            }
        }

        // Begin lobby loop
        cli.setViewState(new SelectLobbyViewState(cli.getViewState()));
        cli.handleInteraction();
        String lastResp = "";

        while(!ready) {
            String response = receiveMessage();
            lastResp = response;

            executor.submit( () -> {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                synchronized (cli) {
                    if (jsonObject.has("error")) {
                        cli.resetInteraction(jsonObject.get("error").getAsString());
                        cli.handleInteraction();
                        return;
                    }
                    new ResponseParameters().deserialize(jsonObject);
                    if (Lobby.getLobby().isReady(cli.getPlayerID())) {
                        ready = true;
                        return;
                    }
                    cli.handleInteraction();
                }
            });
        }

        cli.setViewState(new LobbyViewState(cli.getViewState()));
        new ResponseParameters().deserialize(JsonParser.parseString(lastResp).getAsJsonObject());
        cli.displayState();

        while(!inGame) {
            String response = receiveMessage();

            executor.submit( () -> {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                if(jsonObject.has("matchInfo")) {
                    JsonObject matchInfoJson = jsonObject.get("matchInfo").getAsJsonObject();
                    if(matchInfoJson.get("gameStatus").getAsString().equalsIgnoreCase("IN_GAME")) {
                        synchronized (cli) {
                            inGame = true;
                            cli.nextState(jsonObject);
                            new ResponseParameters().deserialize(jsonObject);
                        }
                        return;
                    }
                }
                synchronized (cli) {
                    new ResponseParameters().deserialize(jsonObject);
                    cli.displayState();
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

        cli.handleInteraction();

        while(inGame) {
            String response = receiveMessage();

            executor.submit( () -> {
               JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
               synchronized (cli) {
                   boolean reqInteraction = cli.nextState(jsonObject);

                   if(MatchInfo.getInstance().getCurrentPlayerID() == cli.getPlayerID()) {
                       new ResponseParameters().deserialize(jsonObject);

                       if(reqInteraction) {
                           cli.handleInteraction(); //Handle interaction in new view
                       }
                   } else {
                       cli.displayState();
                   }
               }
            });
        }
    }
}
