package it.polimi.ingsw.network;

import it.polimi.ingsw.network.observer.Observer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public abstract class Connection implements Runnable, Observer<String> {
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;
    protected final ExecutorService executor = Executors.newFixedThreadPool(16);
    private final ScheduledFuture pingTask;
    protected boolean connected;
    private boolean graphic;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;

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

    public synchronized void sendMessage(String message) {
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

    public void disconnect() {
        System.out.println("Connection lost, stopping ping");
        pingTask.cancel(false);

        connected = false;
    }

    public void setGraphic(boolean graphic) {
        this.graphic = graphic;
    }

    public boolean isGraphic() {
        return graphic;
    }
}
