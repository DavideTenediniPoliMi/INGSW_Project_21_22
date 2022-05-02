package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientConnection implements Observer<String>, Runnable{
    private final Socket serverSocket;
    private final LobbyController lobbyController;
    private final GameController gameController;
    private final VirtualView virtualView;
    private final DataOutputStream out;
    private final DataInputStream in;


    public ClientConnection(Socket socket, LobbyController lobbyController, GameController gameController) throws IOException {
        this.serverSocket = socket;
        this.lobbyController = lobbyController;
        this.gameController = gameController;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        virtualView = new VirtualView();
        virtualView.addObserver(this);

        // PING
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(
                () -> send(""),
                60, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        System.out.println("ClientConnection: starting and waiting for input");
        try {
            int length = in.readInt();

            while (length != -1) {
                System.out.println("received message of length " + length);

                String message = "";
                for (int i = 0; i < length; i++) {
                    message += in.readChar();
                }

                if(length > 0) {
                    virtualView.handleRequest(message);
                }

                length = in.readInt();
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            // TODO disconnect
        }
    }

    @Override
    public void update(String message) {
        send(message);
    }

    public synchronized void send(String message) {
        System.out.println("ClientConnection: sending message " + message + " of size " + message.length());
        try {
            out.writeInt(message.length());

            if(message.equals("")) {
                out.writeChars(message);
            }

            out.flush();
        } catch(IOException e) {
            System.err.println(e.getMessage());
            // TODO disconnect
        }
    }
}
