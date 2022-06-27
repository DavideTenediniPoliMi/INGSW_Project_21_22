package it.polimi.ingsw.network;

import it.polimi.ingsw.network.observer.Observer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Class that represents a connection between Client and Server.
 */
public abstract class Connection implements Runnable, Observer<String> {
    private final DataOutputStream out;
    private final DataInputStream in;
    protected final ExecutorService executor = Executors.newFixedThreadPool(16);
    private final ScheduledFuture pingTask;
    protected boolean connected;

    public Connection(Socket socket) throws IOException {
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        // PING
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);

        pingTask = executor.scheduleAtFixedRate(
                () -> sendMessage(""),
                60, 5, TimeUnit.SECONDS);

        connected = true;
    }

    /**
     * Sends the message on the socket.
     *
     * @param message the message to be sent.
     */
    public synchronized void sendMessage(String message) {
        try {
            System.out.println("SENT : " + message);
            out.writeInt(message.length());

            if(!message.equals("")) {
                out.writeChars(message);
            }

            out.flush();
        } catch(IOException e) {
            disconnect();
        }
    }

    /**
     * Keeps reading from the socket until it reads a non-ping message, then returns it.
     *
     * @return the message read.
     */
    public String receiveMessage() {
        try {
            int length;

            do {
                length = in.readInt();
            } while(length == 0);

            if (length > 0) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    message.append(in.readChar());
                }

                System.out.println("RECEIVED : " + message);
                return message.toString();
            }
        } catch (IOException e){
            disconnect();
        }

        return "";
    }

    @Override
    public void update(String message) {
        sendMessage(message);
    }

    /**
     * To be called when the connection fails. Stops the ping routine and applies any cleanup effects.
     */
    public void disconnect() {
        System.out.println("Connection lost, stopping ping");
        pingTask.cancel(false);

        connected = false;
    }
}
