package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientConnection implements Observer<String>, Runnable{
    private final Socket serverSocket;
    private final LobbyController lobbyController;
    private final GameController gameController;
    private final VirtualView virtualView;
    private final DataOutputStream out;
    private final DataInputStream in;
    private ScheduledFuture pingTask;


    public ClientConnection(Socket socket, LobbyController lobbyController, GameController gameController) throws IOException {
        this.serverSocket = socket;
        this.lobbyController = lobbyController;
        this.gameController = gameController;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        virtualView = new VirtualView(lobbyController, gameController);
        virtualView.addObserver(this);

        // PING
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);

        pingTask = executor.scheduleAtFixedRate(
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
            System.out.println("ClientConnection: client disconnected, stopping ping");
            pingTask.cancel(false);
            // TODO disconnect
        }
    }

    @Override
    public void update(String message) {
        send(message);
    }

    public synchronized void send(String message) {
        System.out.println("ClientConnection: sending message of size " + message.length() + ":\n" + message);
        try {
            out.writeInt(message.length());

            if(message.equals("")) {
                out.writeChars(message);
            }

            out.flush();
        } catch(IOException e) {
            System.out.println("ClientConnection: client disconnected, stopping ping");
            pingTask.cancel(false);
            // TODO disconnect
        }
    }
}
