package it.polimi.ingsw.network.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.viewStates.*;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Connection {
    private final Client client;
    private final CLI cli;
    private boolean inGame;
    private boolean ready;
    private boolean joined;
    private MessageQueue<String> packetQueue;
    private MessageConsumer jsonConsumer;

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

            String received = receiveMessage();
            if(received.equals("")) continue;

            JsonObject jo = JsonParser.parseString(received).getAsJsonObject();

            if (!jo.has("error")) {
                new ResponseParameters().deserialize(jo);
                break;
            }

            cli.resetInteraction(jo.get("error").getAsString());
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
        String lastResp = "";

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.IN_GAME)) { //Handle reconnection
            while(connected) {
                String received = receiveMessage();
                if(received.equals("")) continue;

                lastResp = received;

                JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();

                if(!jsonObject.has("error") && jsonObject.has("game")) { //Received game package
                    ready = true;
                    inGame = true;
                    gameSequence(lastResp);
                    return;
                }
            }
        } else if(MatchInfo.getInstance().getSelectedNumPlayer() == 0) {
            //Begin lobby creation sequence
            cli.setViewState(new NoLobbyViewState(cli.getViewState()));
            cli.handleInteraction();
            while(connected) {
                String received = receiveMessage();
                if(received.equals("")) continue;

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
        while(!joined && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
            bindPlayerID(jsonObject);
        }

        // Begin lobby loop
        cli.setViewState(new SelectLobbyViewState(cli.getViewState()));
        executor.submit(cli::handleInteraction);

        while(!ready && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;
            lastResp = received;

            if(isThisPlayerReady(received))
                break;

            executor.submit( () -> {
                JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
                synchronized (Lobby.getLobby()) {
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
                }
                if(cli.getViewState().isInteractionComplete())
                    cli.handleInteraction();
            });
        }

        cli.setViewState(new LobbyViewState(cli.getViewState()));
        new ResponseParameters().deserialize(JsonParser.parseString(lastResp).getAsJsonObject());
        cli.displayState();

        while(!inGame && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;
            lastResp = received;

            JsonObject jsonObject = JsonParser.parseString(received).getAsJsonObject();
            if(jsonObject.has("matchInfo")) {
                JsonObject matchInfoJson = jsonObject.get("matchInfo").getAsJsonObject();
                if(matchInfoJson.get("gameStatus").getAsString().equalsIgnoreCase("IN_GAME")) {
                    synchronized (cli) {
                        inGame = true;
                    }
                    break;
                }
            }
            executor.submit( () -> {
                synchronized (cli) {
                    new ResponseParameters().deserialize(jsonObject);
                    cli.displayState();
                }
            });
        }
        gameSequence(lastResp);
    }

    /** PER QUANDO IN GAME
     * LOOP CHE LEGGE, QUANDO LEGGE QUALCOSA FA LA DESERIALZE A MENO CHE NON SIA UN ERRORE
     * NEXT_VIEW_STATE A MENO CHE NON SIA UN ERRORE
     * SE NEXT_VIEW_STATE RITORNA TRUE VAI A FARE UN INTERAZIONE DELLA NUOVA VIEW
     * ALTRIMENTI CONTINUI A LEGGERE
     */
    private void gameSequence(String lastResponse) {
        JsonObject initJsonObject = JsonParser.parseString(lastResponse).getAsJsonObject();
        if(!joined)
            bindPlayerID(initJsonObject);
        new ResponseParameters().deserialize(initJsonObject);

        cli.resetTurnState(MatchInfo.getInstance().serialize());
        //cli.nextState(initJsonObject);

        if(MatchInfo.getInstance().getCurrentPlayerID() == cli.getPlayerID()) {
            cli.handleInteractionAsFirst();
        } else {
            cli.setViewState(new GameViewState(cli.getViewState()));
            cli.displayState();
        }

        packetQueue = new MessageQueue<>();
        jsonConsumer = new MessageConsumer(packetQueue, cli);
        executor.submit( () -> jsonConsumer.run());

        while(inGame && connected) {
            String received = receiveMessage();
            if(received.equals("")) continue;

            packetQueue.add(received);
            packetQueue.notifyAllForEmpty();
        }
        jsonConsumer.stop();
    }

    private boolean isThisPlayerReady(String received) {
        JsonObject jsonObject = (JsonObject) JsonParser.parseString(received);
        if(jsonObject.has("players")) {
            JsonArray jsonPlayers = jsonObject.get("players").getAsJsonArray();

            for(JsonElement jsonPlayer : jsonPlayers) {
                JsonObject playerObj = jsonPlayer.getAsJsonObject();
                if(playerObj.get("ID").getAsInt() == cli.getPlayerID())
                    return playerObj.get("ready").getAsBoolean();
            }
        }
        return false;
    }

    private void bindPlayerID(JsonObject jsonObject) {
        JsonObject gameObject;

        if(jsonObject.has("game"))
            gameObject = jsonObject.get("game").getAsJsonObject();
        else
            gameObject = jsonObject;

        if(gameObject.has("players")) {
            JsonArray players = gameObject.get("players").getAsJsonArray();
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
}
