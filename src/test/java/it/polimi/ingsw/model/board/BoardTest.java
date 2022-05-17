package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.StringUtils;
import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        board.placeMNAt(0);

        board.createClouds(2);
        board.refillClouds(3);

        Player p = new Player(0, "Pippo", TowerColor.BLACK, CardBack.WIZARD_1, true);
        board.addSchool(p);

        board.addToEntranceOf(0, new StudentGroup(Color.GREEN, 2));
    }

    @AfterEach
    void tearDown() {
        board = null;
    }

    @Test
    public void numIslandTest() {
        assertEquals(12, board.getNumIslands());
    }

    @Test
    public void getIslandAtTest() {
        Island is = board.getIslands().get(0);
        assertEquals(is, board.getIslandAt(0));
    }

    @Test
    public void addStudentsTest() {
        board.addStudentsToIsland(0, new StudentGroup(Color.GREEN, 3));
        Island is = board.getIslandAt(0);

        assertAll(
                () -> assertEquals(3, is.getNumStudentsByColor(Color.GREEN)),
                () -> assertEquals(0, is.getNumStudentsByColor(Color.BLUE)),
                () -> assertEquals(0, is.getNumStudentsByColor(Color.RED)),
                () -> assertEquals(0, is.getNumStudentsByColor(Color.PINK)),
                () -> assertEquals(0, is.getNumStudentsByColor(Color.YELLOW))
        );
    }

    @Test
    public void conquerIslandTest() {
        assertNull(board.getIslandAt(0).getTeamColor());
        board.conquerIsland(TowerColor.WHITE);
        assertEquals(TowerColor.WHITE, board.getIslandAt(0).getTeamColor());
    }

    @Test
    public void mergeIslandTest() {
        board.conquerIsland(TowerColor.WHITE);
        board.moveMN(1);
        board.conquerIsland(TowerColor.WHITE);
        board.mergeIslands(0, 1);
        assertAll(
                () -> assertEquals(11, board.getNumIslands()),
                () -> assertEquals(0, board.getMNPosition())
        );
    }

    @Test
    public void moveMNTest() {
        assertEquals(0, board.getMNPosition());
        board.moveMN(2);
        assertEquals(2, board.getMNPosition());
    }

    @Test
    public void mergeLastFirstTest() {
        board.mergeIslands(11,0);

        assertAll(
                () -> assertEquals(11, board.getNumIslands()),
                () -> assertEquals(10, board.getMNPosition())
        );
    }

    @Test
    public void addCloudsTest() {
        List<Cloud> clouds = board.getClouds();
        assertEquals(2, clouds.size());

        for(Cloud cloud: clouds) {
            int temp = 0;
            StudentGroup stud = cloud.getStudents();

            for(Color c: Color.values()) {
                temp += stud.getByColor(c);
            }

            assertEquals(3, temp);
            assertTrue(cloud.isAvailable());
        }
    }

    @Test
    public void collectFromCloudTest() {
        List<Cloud> clouds = board.getClouds();
        StudentGroup sg = clouds.get(0).getStudents();
        StudentGroup res = board.collectFromCloud(0);

        assertAll(
                () -> assertFalse(clouds.get(0).isAvailable()),
                () -> assertEquals(sg, res)
        );
    }

    @Test
    public void createSchoolTest() {
        assertAll(
                () -> assertEquals(1, board.getSchools().size()),
                () -> assertEquals(board.getSchools().get(0), board.getSchoolByPlayerID(0))
        );
    }

    @Test
    public void profTrackerTest() {
        board.giveProfessorTo(0, Color.GREEN);
        board.giveProfessorTo(3, Color.BLUE);

        ProfessorTracker pt = board.getProfessorOwners();
        assertAll(
                () -> assertEquals(0, pt.getOwnerIDByColor(Color.GREEN)),
                () -> assertEquals(3, pt.getOwnerIDByColor(Color.BLUE))
        );
    }

    @Test
    public void towersTest() {
        School s = board.getSchoolByPlayerID(0);
        board.addTowersTo(0, 3);
        assertEquals(3, s.getNumTowers());
        board.removeTowerFrom(0, 2);
        assertEquals(1, s.getNumTowers());
    }

    @Test
    public void coinsTest() {
        assertEquals(20, board.getNumCoinsLeft());

        board.takeCoin();
        board.takeCoin();
        assertEquals(18, board.getNumCoinsLeft());

        board.putCoinsBack(1);
        assertEquals(19, board.getNumCoinsLeft());

        for (int i = 0; i < 25; i++) {
            board.takeCoin();
        }
        assertEquals(0, board.getNumCoinsLeft());
    }

    @Test
    public void moveStudentsTest() {
        School s = board.getSchoolByPlayerID(0);

        assertEquals(2, s.getNumStudentsInEntranceByColor(Color.GREEN));

        board.removeFromEntranceOf(0, new StudentGroup(Color.GREEN, 1));
        board.addToDiningRoomOf(0, new StudentGroup(Color.GREEN, 1));

        assertAll(
                () -> assertEquals(1, s.getNumStudentsInDiningRoomByColor(Color.GREEN)),
                () -> assertEquals(1, s.getNumStudentsInEntranceByColor(Color.GREEN))
        );

        board.removeFromDiningRoomOf(0, new StudentGroup(Color.GREEN, 1));

        assertEquals(0, s.getNumStudentsInDiningRoomByColor(Color.GREEN));
    }

    @Test
    public void testSetters() {
        board.setNumCoinsLeft(0);
        assertEquals(0, board.getNumCoinsLeft());
        board.setProfessorOwners(new ProfessorTracker());
        assertEquals(-1, board.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
        board.setClouds(new ArrayList<>());
        assertTrue(board.getClouds().isEmpty());
        board.setIslands(new ArrayList<>());
        assertTrue(board.getIslands().isEmpty());
        board.setSchools(new ArrayList<>());
        assertTrue(board.getSchools().isEmpty());
    }

    @Test
    void serializeTest() {
        board.conquerIsland(TowerColor.BLACK);
        board.moveMN(1);
        board.conquerIsland(TowerColor.BLACK);

        board.mergeIslands(0, 1);

        JsonObject jsonObject = board.serialize();

        board = null;

        board = new Board();
        board.deserialize(jsonObject);

        assertEquals(board.getIslandAt(0).getTeamColor(), TowerColor.BLACK);

        jsonObject.remove("clouds");
        jsonObject.remove("schools");
        jsonObject.remove("professorOwners");

        board = null;

        board = new Board();
        board.deserialize(jsonObject);

        assertTrue(board.getClouds().isEmpty());
        assertTrue(board.getSchools().isEmpty());
    }

    /*@Test
    void testPrint() {
        AnsiConsole.systemInstall();

        AnsiConsole.sysOut().println(StringUtils.listToString(board.print()));

        AnsiConsole.systemUninstall();
    }*/
}