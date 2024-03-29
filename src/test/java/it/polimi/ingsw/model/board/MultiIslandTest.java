package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.StringUtils;
import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiIslandTest {
    Island is;
    @BeforeEach
    public void setUp(){
        Island is1 = new SimpleIsland();
        Island is2 = new SimpleIsland();

        is1.conquerIsland(TowerColor.BLACK);
        is2.conquerIsland(TowerColor.BLACK);

        is1.addStudents(new StudentGroup(Color.BLUE, 2));
        is2.addStudents(new StudentGroup(Color.GREEN, 3));

        is = new MultiIsland(is1, is2);
    }

    @AfterEach
    void tearDown(){
        is = null;
    }

    @Test
    public void motherNatureTest() {
        assertTrue(is.isMotherNatureOnIsland());
        is.setMotherNatureTo(false);
        assertFalse(is.isMotherNatureOnIsland());
    }

    @Test
    public void numStudentTest() {
        assertAll(
                () -> assertEquals(2, is.getNumStudentsByColor(Color.BLUE)),
                () -> assertEquals(3, is.getNumStudentsByColor(Color.GREEN))
        );
    }

    @Test
    public void numIslandTest() {
        assertEquals(2, is.getNumIslands());

        Island is1 = new SimpleIsland();
        is1.conquerIsland(TowerColor.BLACK);
        Island is2 = new MultiIsland(is, is1);

        assertEquals(3, is2.getNumIslands());
    }

    @Test
    public void towerColorTest() {
        assertEquals(TowerColor.BLACK, is.getTeamColor());
        is.conquerIsland(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, is.getTeamColor());
    }

    @Test
    public void serializeTest() {
        Island i1 = new SimpleIsland();
        i1.addStudents(new StudentGroup(Color.BLUE, 3));

        Island i2 = new SimpleIsland();
        i1.addStudents(new StudentGroup(Color.BLUE, 2));
        i1.addStudents(new StudentGroup(Color.GREEN, 1));

        Island i3 = new SimpleIsland();
        i1.addStudents(new StudentGroup(Color.RED, 4));

        Island i4 = new SimpleIsland();
        i1.addStudents(new StudentGroup(Color.YELLOW, 1));

        i1.conquerIsland(TowerColor.BLACK);
        i2.conquerIsland(TowerColor.BLACK);
        i3.conquerIsland(TowerColor.BLACK);
        i4.conquerIsland(TowerColor.BLACK);

        Island m1 = new MultiIsland(i1, i2);
        Island m2 = new MultiIsland(i3, i4);

        Island m3 = new MultiIsland(m1, m2);

        JsonObject jsonObject = m3.serialize();

        Island m4 = new MultiIsland(new SimpleIsland(), new SimpleIsland());
        m4.deserialize(jsonObject);

        assertEquals(m3.getNumIslands(), m4.getNumIslands());
        assertEquals(m3.getTeamColor(), m4.getTeamColor());

        for(Color color : Color.values()) {
            assertEquals(m3.getNumStudentsByColor(color), m4.getNumStudentsByColor(color));
        }

        JsonObject jsonObject2 = i1.serialize();
        Island noStudents = new SimpleIsland();
        noStudents.deserialize(jsonObject2);

        for(Color color : Color.values()) {
            assertEquals(noStudents.getNumStudentsByColor(color), i1.getNumStudentsByColor(color));
        }
    }
    /*

    @Test
    public void testPrintGame4() {
        tearDown();
        AnsiConsole.systemInstall();
        GameController gameController = new GameController();
        Game game = Game.getInstance();
        Board board = game.getBoard();
        Lobby lobby = Lobby.getLobby();
        MatchInfo matchInfo = MatchInfo.getInstance();

        lobby.addPlayer(0, "a");
        lobby.addPlayer(1, "b");
        lobby.selectTeam(0, TowerColor.BLACK);
        lobby.selectTeam(1, TowerColor.WHITE);
        lobby.selectCardBack(0, CardBack.WIZARD_1);
        lobby.selectCardBack(1, CardBack.WIZARD_2);
        lobby.setReadyStatus(0, true);
        lobby.setReadyStatus(1, true);

        lobby.addPlayer(2, "c");
        lobby.selectTeam(2, TowerColor.BLACK);
        lobby.selectCardBack(2, CardBack.WIZARD_3);
        lobby.setReadyStatus(2, true);
        lobby.addPlayer(3, "d");
        lobby.selectTeam(3, TowerColor.WHITE);
        lobby.selectCardBack(3, CardBack.WIZARD_4);
        lobby.setReadyStatus(3, true);

        matchInfo.setUpGame(4,true);

        matchInfo.setNumPlayersConnected(4);

        gameController.tryCreatingGame();
        AnsiConsole.sysOut().println(StringUtils.listToString(game.print()));

        game.mergeIslands(11, 0);

        AnsiConsole.sysOut().println(StringUtils.listToString(game.print()));

        game.mergeIslands(10, 0);

        AnsiConsole.sysOut().println(StringUtils.listToString(game.print()));

        game.mergeIslands(8, 9);

        AnsiConsole.sysOut().println(StringUtils.listToString(game.print()));

        AnsiConsole.systemUninstall();
    }*/
}