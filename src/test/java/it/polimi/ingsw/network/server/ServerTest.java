package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.GameStatus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void test() throws IOException {
        Server server = new Server();
        GameController gameController = new GameController();
        MatchInfo matchInfo = MatchInfo.getInstance();
        Game game = Game.getInstance();
        Lobby lobby = Lobby.getLobby();

        lobby.addPlayer(0, "pippo");
        lobby.addPlayer(1, "topo");
        matchInfo.setUpGame(2, false);
        gameController.createGame();

        System.out.println(matchInfo.serialize());
        System.out.println(game.serialize());
        System.out.println(lobby.serialize());

        server.saveDataToFile();

        Game.resetInstance();
        MatchInfo.resetInstance();

        /*
        game = Game.getInstance();
        matchInfo = MatchInfo.getInstance();

        File file = new File("BackupData.txt");
        server.readDataFromFile(file);
        file.delete();

        System.out.println(matchInfo.serialize());
        System.out.println(game.serialize());
        System.out.println(lobby.serialize());

        Game.resetInstance();
        Lobby.resetLobby();
        MatchInfo.resetInstance();

         */
    }
}