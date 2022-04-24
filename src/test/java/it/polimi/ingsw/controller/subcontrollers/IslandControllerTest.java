package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandControllerTest {
    Game game;
    Board board;
    IslandController islandController;

    @BeforeEach
    void setUp() {
        islandController = new IslandController();
        game = Game.getInstance();
        board = game.getBoard();
        game.addPlayer(new Player(0, "luca", TowerColor.GREY, CardBack.CB_1, true));
        game.addPlayer(new Player(1, "paolo", TowerColor.BLACK, CardBack.CB_2, true));
        board.addSchool(game.getPlayerByID(0));
        board.addSchool(game.getPlayerByID(1));
        game.placeMNAt(0);

        game.conquerIsland(TowerColor.GREY);
        board.addStudentsToIsland(0, new StudentGroup(Color.BLUE, 3));
        game.giveProfessorTo(0, Color.BLUE);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        game = null;
        islandController = null;
    }

    @Test
    void moveMN_islandOwned() {
        Island island = board.getIslandAt(0);
        TowerColor oldTeam = island.getTeamColor();

        islandController.moveMN(12);

        TowerColor newTeam = island.getTeamColor();

        assertEquals(newTeam, oldTeam);
    }

    @Test
    void moveMN_newOwner() {
        board.addStudentsToIsland(0, new StudentGroup(Color.GREEN, 5));
        game.giveProfessorTo(1, Color.GREEN);

        islandController.moveMN(12);

        TowerColor newTeam = board.getIslandAt(0).getTeamColor();

        assertEquals(TowerColor.BLACK, newTeam);
    }

    @Test
    void moveMN_mergeIslands() {
        board.addStudentsToIsland(2, new StudentGroup(Color.BLUE, 2));
        islandController.moveMN(2);
        board.addStudentsToIsland(1, new StudentGroup(Color.BLUE, 2));

        game.placeMNAt(0);
        islandController.moveMN(1);

        assertAll(
                () -> assertEquals(TowerColor.GREY, board.getIslandAt(0).getTeamColor()),
                () -> assertEquals(10, board.getNumIslands())
        );
    }
}