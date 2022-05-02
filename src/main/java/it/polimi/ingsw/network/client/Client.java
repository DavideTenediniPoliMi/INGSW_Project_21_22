package it.polimi.ingsw.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String ip;
    private final int port;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Client: Connection established");
        DataInputStream socketIn = new DataInputStream(socket.getInputStream());

        try {
            int num = socketIn.readInt();

            while (num != -1) {
                String message = "";
                for (int i = 0; i < num; i++) {
                    message += socketIn.readChar();
                }

                System.out.println("Client: Message of length " + num + " received :\n" + message);

                num = socketIn.readChar();
            }
        } catch(IOException e){
            System.out.println("Client: server disconnected");
        } finally {
            socketIn.close();
            socket.close();
        }
    }
}
