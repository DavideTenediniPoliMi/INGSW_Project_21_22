package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    private final String ip;
    private final int port;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Client(String ip, int port) throws IOException{
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        //TEST PARAMS
        RequestParameters params = new RequestParameters().setCommandType(CommandType.HANDSHAKE).setName("Davide");
        RequestParameters params2 = new RequestParameters().setCommandType(CommandType.CREATE_LOBBY)
                .setExpertMode(false).setSelectedNumPlayer(2);
        RequestParameters params3 = new RequestParameters().setCommandType(CommandType.JOIN);

        executor.scheduleAtFixedRate(
                () -> send(params.serialize().toString()),
                0, 100000, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(
                () -> send(params2.serialize().toString()),
                5, 100000, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(
                () -> send(params3.serialize().toString()),
                10, 100000, TimeUnit.SECONDS);
    }

    public void run() throws IOException {
        System.out.println("Client: Connection established");

        try {
            int num = in.readInt();

            while (num != -1) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < num; i++) {
                    message.append(in.readChar());
                }

                System.out.println("Client: Message of length " + num + " received :\n" + message);

                num = in.readChar();
            }
        } catch(IOException e){
            System.out.println("Client: server disconnected");
        } finally {
            in.close();
            socket.close();
        }
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
            System.out.println("Client: server disconnected");
            // TODO disconnect
        }
    }
}
