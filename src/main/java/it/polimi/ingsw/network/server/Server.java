package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.PlayerAlreadyConnectedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.view.VirtualView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private static final String BACKUP_FILE = "BackupData.txt";
    private final ServerSocket serverSocket = new ServerSocket(PORT);
    private final ExecutorService executor = Executors.newFixedThreadPool(16);
    private final LobbyController lobbyController = new LobbyController();
    private final GameController gameController = new GameController();
    private final List<VirtualView> virtualViews = new ArrayList<>();

    public Server() throws IOException, PlayerAlreadyConnectedException {
        File file = new File(BACKUP_FILE);

        if(file.exists()) {
            readDataFromFile(file);
            file.delete();

            for(Player player : Game.getInstance().getPlayers()) {
                VirtualView virtualView = getVVFor(player.getName());
                virtualView.deserialize(player.getID());
            }
        }
    }

    public synchronized VirtualView getVVFor(String name) throws PlayerAlreadyConnectedException {
        Optional<VirtualView> result = virtualViews.stream()
                .filter((virtualView) -> (virtualView.getName().equals(name)))
                .findAny();

        if(result.isPresent()) {
            if(!result.get().isConnected()) {
                return result.get();
            }
            throw new PlayerAlreadyConnectedException(name);
        }
        VirtualView vv = new VirtualView(name, lobbyController, gameController);
        virtualViews.add(vv);
        return vv;
    }

    public synchronized void removeVV(VirtualView virtualView) {
        virtualViews.remove(virtualView);
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

    private void readDataFromFile(File file) {
        MatchInfo matchInfo = MatchInfo.getInstance();
        String line;

        try (Scanner fileReader = new Scanner(file)) {

            line = fileReader.nextLine();
            matchInfo.deserialize(JsonParser.parseString(line).getAsJsonObject());

            line = fileReader.nextLine();
            if (matchInfo.getGameStatus().equals(GameStatus.LOBBY))
                Lobby.getLobby().deserialize(JsonParser.parseString(line).getAsJsonObject());
            else
                Game.getInstance().deserialize(JsonParser.parseString(line).getAsJsonObject());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveDataToFile() {
        Lobby lobby = Lobby.getLobby();
        Game game = Game.getInstance();
        MatchInfo matchInfo = MatchInfo.getInstance();
        if(matchInfo.isGameOver())
            return;
        try {
            new File(BACKUP_FILE).createNewFile();

            FileWriter fileWriter = new FileWriter(BACKUP_FILE);

            fileWriter.write(matchInfo.serialize().toString());
            fileWriter.write("\n");
            if(matchInfo.getGameStatus().equals(GameStatus.LOBBY))
                fileWriter.write(lobby.serialize().toString());
            else if(matchInfo.getGameStatus().equals(GameStatus.IN_GAME))
                fileWriter.write(game.serialize().toString());

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
