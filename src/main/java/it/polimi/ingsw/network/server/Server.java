package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket = new ServerSocket(PORT);
    private final ExecutorService executor = Executors.newFixedThreadPool(16);
    private final LobbyController lobbyController = new LobbyController();
    private final GameController gameController = new GameController();
    private final List<VirtualView> virtualViews = new ArrayList<>();

    public Server() throws IOException {
        // TEST
        MatchInfo.getInstance().setSelectedNumPlayer(2);
        MatchInfo.getInstance().setExpertMode(false);
    }

    public synchronized VirtualView getVVFor(String name) throws Exception {
        Optional<VirtualView> result = virtualViews.stream()
                .filter((virtualView) -> (virtualView.getName().equals(name)))
                .findAny();

        if(result.isPresent()) {
            if(!result.get().isConnected()) {
                return result.get();
            }
            throw new Exception(); // TODO make exception for "Player with that name is already connected"
        }
        return new VirtualView(name, lobbyController, gameController);
    }

    public void run(){
        System.out.println("Server: running");
        while(true){
            try {
                System.out.println("Server: waiting for connections");
                Socket newSocket = serverSocket.accept();
                System.out.println("Server: connection received");

                ClientConnection socketConnection = new ClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}
