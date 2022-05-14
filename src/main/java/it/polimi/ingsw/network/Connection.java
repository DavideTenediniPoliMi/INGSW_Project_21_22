package it.polimi.ingsw.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public abstract class Connection implements Runnable{
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;
    protected final ExecutorService executor = Executors.newFixedThreadPool(16);
    private final ScheduledFuture pingTask;
    protected boolean connected;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        // PING
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        executor.setRemoveOnCancelPolicy(true);

        pingTask = executor.scheduleAtFixedRate(
                () -> send(""),
                60, 5, TimeUnit.SECONDS);

        connected = true;
    }

    public synchronized void send(String message) {
        System.out.println("Sending message of size " + message.length() + ":\n" + message);
        try {
            out.writeInt(message.length());

            if(!message.equals("")) {
                out.writeChars(message);
            }

            out.flush();
        } catch(IOException e) {
            disconnect();
        }
    }

    public String readMessage() {
        String finalMessage = "";
        try {
            int length = in.readInt();

            if (length != -1) {
                System.out.println("received message of length " + length);

                StringBuilder message = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    message.append(in.readChar());
                }
                System.out.println(message);

                finalMessage = message.toString();
            }
        } catch (IOException e){
            disconnect();
        }
        return finalMessage;
    }

    public void disconnect() {
        System.out.println("Connection lost, stopping ping");
        pingTask.cancel(false);
        connected = false;
    }
}
