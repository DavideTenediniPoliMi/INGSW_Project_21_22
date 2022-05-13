package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    /*Server server;
    Lobby lobby;
    Game game;
    GameController gameController;
    MatchInfo matchInfo;


    @BeforeEach
    void setUp() throws IOException {
        server = new Server();
        gameController = new GameController();
        matchInfo = MatchInfo.getInstance();
        lobby = Lobby.getLobby();
    }

    @AfterEach
    void tearDown() {
        Lobby.resetLobby();
        Game.resetInstance();
        MatchInfo.resetInstance();
        server = null;
        matchInfo = null;
        gameController = null;
        lobby = null;
        game = null;
    }

    /*@Test
    void test(){
        //Game.resetInstance();
        lobby.addPlayer(0, "pippo");
        lobby.addPlayer(1, "topo");
        matchInfo.setUpGame(2, false);
        gameController.createGame();

        game = Game.getInstance();

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


    }*/
}