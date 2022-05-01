package it.polimi.ingsw.network;

import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientConnection implements Observer<String>, Runnable{
    private final Socket serverSocket;
    private final Server server;
    private final VirtualView virtualView;
    private final OutputStreamWriter out;


    public ClientConnection(Socket serverSocket, Server server) throws IOException {
        this.serverSocket = serverSocket;
        this.server = server;

        out = new OutputStreamWriter(serverSocket.getOutputStream());

        virtualView = new VirtualView();
        virtualView.addObserver(this);
    }

    @Override
    public void run() {

    }

    @Override
    public void update(String message) {
        send(message);
    }

    public synchronized void send(String message) {
        try {
            out.write(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
