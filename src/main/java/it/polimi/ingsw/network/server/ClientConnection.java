package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientConnection extends Connection implements Observer<String> {
    private final Server server;
    private VirtualView virtualView;
    private boolean bound;

    public ClientConnection(Socket socket, Server server) throws IOException {
        super(socket);
        this.server = server;

        bound = false;
    }

    @Override
    public void run() {
        System.out.println("ClientConnection: starting and waiting for input");

        System.out.println("Waiting for handshake");
        waitForHandshake();

        while(connected) {
            String received = readMessage();
            executor.submit(() -> virtualView.handleRequest(received));
        }

        System.out.println("Disconnected");
    }

    @Override
    public void update(String message) {
        send(message);
    }

    private void waitForHandshake() {
        while(connected && !bound) {
            handleHandshake(readMessage());
        }
    }

    public void handleHandshake(String message) { // TODO Proper error messages
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();

        if(!jsonObject.has("commandType") || !jsonObject.has("name")) {
            send("Invalid params");
            return;
        }

        if (!jsonObject.get("commandType").getAsString().equals("HANDSHAKE")) {
            send("Wrong command. Handshake expected");
            return;
        }

        try {
            virtualView = server.getVVFor(jsonObject.get("name").getAsString());
            virtualView.addObserver(this);
            send(MatchInfo.getInstance().serialize().toString());
            bound = true;
        } catch (PlayerAlreadyConnectedException exc) {
            send(exc.toString());
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        virtualView.disconnect();

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.LOBBY)) {
            server.removeVV(virtualView);
        }
    }
}
