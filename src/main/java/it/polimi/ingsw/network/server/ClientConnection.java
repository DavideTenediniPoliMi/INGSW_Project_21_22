package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;

public class ClientConnection extends Connection {
    private final Server server;
    private VirtualView virtualView;
    private boolean bound;

    public ClientConnection(Socket socket, Server server) throws IOException {
        super(socket);

        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("Waiting for handshake");
        waitForHandshake();

        while(connected) {
            String received = receiveMessage();

            if(received.equals("")) continue;

            executor.submit(() -> virtualView.handleRequest(received));
        }

        System.out.println("Disconnected");
    }

    private void waitForHandshake() {
        while(connected && !bound) {
            String received = receiveMessage();

            if(received.equals("")) continue;

            handleHandshake(received);
        }
    }

    public void handleHandshake(String message) { // TODO Proper error messages
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();

        if(!jsonObject.has("commandType") || !jsonObject.has("name")) {
            sendMessage("Invalid params");
            return;
        }

        if (!jsonObject.get("commandType").getAsString().equals("HANDSHAKE")) {
            sendMessage("Wrong command. Handshake expected");
            return;
        }

        try {
            virtualView = server.getVVFor(jsonObject.get("name").getAsString());
            virtualView.addObserver(this);
            sendMessage(new ResponseParameters().setSendMatchInfo(true).serialize().toString());
            bound = true;
        } catch (PlayerAlreadyConnectedException exc) {
            sendMessage(new ResponseParameters().setError(exc.getMessage()).serialize().toString());
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
