package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientConnection implements Observer<String>, Runnable{
    private final Socket serverSocket;
    private final Server server;
    private VirtualView virtualView;
    private final DataOutputStream out;
    private final DataInputStream in;
    private ScheduledFuture pingTask;
    private final ExecutorService executor = Executors.newFixedThreadPool(16);


    public ClientConnection(Socket socket, Server server) throws IOException {
        this.serverSocket = socket;
        this.server = server;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());


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
                System.out.println(message);

                String finalMessage = message;

                if(length > 0) {
                    if(virtualView == null) {
                        handleHandshake(message);
                    } else {
                        executor.submit(() -> virtualView.handleRequest(finalMessage));
                    }
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

            if(!message.equals("")) {
                out.writeChars(message);
            }

            out.flush();
        } catch(IOException e) {
            System.out.println("ClientConnection: client disconnected, stopping ping");
            pingTask.cancel(false);
            // TODO disconnect
        }
    }

    public void handleHandshake(String message) { // TODO Proper error messages
        JsonObject jo = JsonParser.parseString(message).getAsJsonObject();

        if(!jo.has("commandType") || !jo.has("name")) {
            send("Invalid params");
            return;
        }

        if (!jo.get("commandType").getAsString().equals("HANDSHAKE")) {
            send("Wrong command. Handshake expected");
            return;
        }

        try {
            virtualView = server.getVVFor(jo.get("name").getAsString());
            send(MatchInfo.getInstance().serialize().toString());
        } catch (Exception e) {
            send("Another player with the same name is connected");
        }
    }
}
