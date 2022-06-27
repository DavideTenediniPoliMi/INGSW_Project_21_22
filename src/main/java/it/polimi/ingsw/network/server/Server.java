package it.polimi.ingsw.network.server;

import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.lobby.GameFullException;
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

/**
 * Class that handles the server lifecycle.
 */
public class Server {
    private static final String BACKUP_FILE = "BackupData.txt";
    private ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(16);
    private LobbyController lobbyController = new LobbyController();
    private GameController gameController = new GameController();
    private final List<VirtualView> virtualViews = new ArrayList<>();

    public Server(int port) throws GameFullException, PlayerAlreadyConnectedException, IOException {
        this();
        serverSocket = new ServerSocket(port);
    }

    /**
     * The constructor checks if a backup file exists. If it does than it loads the information on the server, otherwise
     * creates a brand-new server.
     *
     * @throws PlayerAlreadyConnectedException when there are multiple VirtualView with the same name.
     * @throws GameFullException when there are too many players connected to the game.
     */
    private Server() throws PlayerAlreadyConnectedException, GameFullException {
        File file = new File(BACKUP_FILE);

        if(file.exists()) {
            readDataFromFile(file);
            file.delete();

            gameController.loadSavedState();

            if(MatchInfo.getInstance().getGameStatus().equals(GameStatus.IN_GAME)) {
                for(Player player : Game.getInstance().getPlayers()) {
                    VirtualView virtualView = getVVFor(player.getName());
                    virtualView.deserialize(player.getID());
                    virtualView.disconnect();
                    virtualView.setJoined(true);
                }
            }
        }
    }


    /**
     * Returns a Virtual View for the specified name. If a Virtual View for that player's name already exists than returns it,
     * at the condition that it is not connected to any other socket. If there is not a Virtual View for that player,
     * it creates one and adds it to the list of Virtual Views.
     *
     * @param name the name of the player.
     * @return the VirtualView for the player.
     * @throws PlayerAlreadyConnectedException when the name corresponds to a Virtual View used by another player at the same time.
     * @throws GameFullException when a player tries to create a new Virtual View when the game is already full.
     */
    public synchronized VirtualView getVVFor(String name) throws PlayerAlreadyConnectedException, GameFullException {
        MatchInfo matchInfo = MatchInfo.getInstance();
        if(matchInfo.getNumPlayersConnected() == matchInfo.getSelectedNumPlayer() && matchInfo.getSelectedNumPlayer() != 0) {
            throw new GameFullException();
        }

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

    /**
     * Removes the Virtual View form the list of tracked VirtualViews on the server.
     *
     * @param virtualView the Virtual View to remove.
     */
    public synchronized void removeVV(VirtualView virtualView) {
        virtualViews.remove(virtualView);
    }

    /**
     * Accepts incoming connections and creates a single ClientConnection for each of them.
     */
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

    /**
     * Reads and parses the json objects from the file.
     *
     * @param file the file with the backed up data.
     */
    private void readDataFromFile(File file) {
        MatchInfo matchInfo = MatchInfo.getInstance();
        String line;

        try (Scanner fileReader = new Scanner(file)) {

            line = fileReader.nextLine();
            matchInfo.deserialize(JsonParser.parseString(line).getAsJsonObject());

            line = fileReader.nextLine();

            Game.getInstance().deserialize(JsonParser.parseString(line).getAsJsonObject());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes the Game objects in JSON formats on the back-up file. If the game is in LOBBY status then discards everything.
     */
    public void saveDataToFile() {
        Game game = Game.getInstance();
        MatchInfo matchInfo = MatchInfo.getInstance();
        if(matchInfo.isGameOver())
            return;
        try {
            if(matchInfo.getGameStatus().equals(GameStatus.IN_GAME)) {
                new File(BACKUP_FILE).createNewFile();

                FileWriter fileWriter = new FileWriter(BACKUP_FILE);

                matchInfo.setNumPlayersConnected(0);

                for(Player p : game.getPlayers()) {
                    game.disconnectPlayer(p.getID());
                }

                fileWriter.write(matchInfo.serialize().toString());
                fileWriter.write("\n");
                fileWriter.write(game.serialize().toString());

                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset all the game related structures.
     */
    public void resetGame() {
        Lobby.resetLobby();
        Game.resetInstance();
        MatchInfo.resetInstance();
        lobbyController = new LobbyController();
        gameController = new GameController();
        virtualViews.clear();
    }
}
