package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.exceptions.lobby.GameFullException;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;

/**
 * Class representing the connection to the Client on the Server.
 */
public class ClientConnection extends Connection {
    private final Server server;
    private VirtualView virtualView;
    private boolean bound;

    public ClientConnection(Socket socket, Server server) throws IOException {
        super(socket);

        this.server = server;
    }

    /**
     * After handling the handshake it keeps reading messages and dispatching them to the Virtual View.
     */
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

    /**
     * Keeps reading and handling messages until a valid handshake has been performed.
     */
    private void waitForHandshake() {
        while(connected && !bound) {
            String received = receiveMessage();

            if(received.equals("")) continue;

            handleHandshake(received);
        }
    }

    /**
     * Attempts to bind a Virtual View to the name received.
     *
     * @param message the last message received
     */
    public void handleHandshake(String message) {
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
            //If VirtualView is new and game has already started send error
            if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.IN_GAME) && !virtualView.hasJoined()) {
                sendMessage(new ResponseParameters().setError("Game has already started!").serialize().toString());
                server.removeVV(virtualView);
                return;
            }

            sendMessage(new ResponseParameters().setSendMatchInfo(true).serialize().toString());
            bound = true;
        } catch (PlayerAlreadyConnectedException | GameFullException exc) {
            sendMessage(new ResponseParameters().setError(exc.getMessage()).serialize().toString());
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        virtualView.disconnect();

        if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.LOBBY))
            server.removeVV(virtualView);
        else if(MatchInfo.getInstance().isGameOver())
            resetGame();
    }

    /**
     * Resets the server after all payers have disconnected.
     */
    public void resetGame() {
        GameStatus status = MatchInfo.getInstance().getGameStatus();
        if(status.equals(GameStatus.RESETTING) || status.equals(GameStatus.LOBBY)) //Game is resetting or has already been reset
            return;
        server.resetGame();
    }
}
