package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.network.server.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket = new ServerSocket(PORT);
    private final ExecutorService executor = Executors.newFixedThreadPool(32);
    private final LobbyController lobbyController = new LobbyController();
    private final GameController gameController = new GameController();

    public Server() throws IOException {
    }

    public void run(){
        System.out.println("Server: running");
        while(true){
            try {
                System.out.println("Server: waiting for connections");
                Socket newSocket = serverSocket.accept();
                System.out.println("Server: connection received");

                ClientConnection socketConnection = new ClientConnection(newSocket, lobbyController, gameController);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}
