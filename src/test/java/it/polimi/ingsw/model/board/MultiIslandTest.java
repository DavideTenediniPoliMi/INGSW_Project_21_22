package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
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

    @Test
    public void testPrint() {
        SimpleIsland isld = new SimpleIsland();
        isld.conquerIsland(TowerColor.GREY);
        StudentGroup st = new StudentGroup(Color.BLUE, 4);
        st.addByColor(Color.GREEN, 2);
        st.addByColor(Color.RED, 1);
        st.addByColor(Color.YELLOW, 9);

        isld.addStudents(st);

        isld.setMotherNatureTo(true);

        isld.print(false, false, false, false);

        /*AnsiConsole.systemInstall();
        AnsiConsole.sysOut().println(isld.printIsland(null, false, false, false, false));
        AnsiConsole.sysOut().println(isld.printIsland(null, false, true, false, false));
        AnsiConsole.sysOut().println(isld.printIsland(null, true, false, false, false));
        AnsiConsole.sysOut().println(isld.printIsland(null, false, false, true, false));*/
    }

    /*@Test
    public void testPrintMulti() {
        Game game = Game.getInstance();

        game.placeMNAt(0);
        for(int i = 0; i < 12; i++) {
            game.conquerIsland(TowerColor.WHITE);
            game.addInitialStudentToIsland(i, game.drawStudents(5));
            game.moveMN(1);
        }

        game.moveMN(1);
        game.conquerIsland(TowerColor.GREY);
        game.moveMN(1);
        game.conquerIsland(TowerColor.GREY);
        game.mergeIslands(1, 2);
        game.moveMN(1);
        game.conquerIsland(TowerColor.GREY);
        game.mergeIslands(1, 2);

        game.addInitialStudentToIsland(1, new StudentGroup(Color.BLUE, 5));

        game.moveMN(5);
        game.conquerIsland(TowerColor.BLACK);
        game.moveMN(1);
        game.conquerIsland(TowerColor.BLACK);
        game.mergeIslands(6, 7);
        game.addInitialStudentToIsland(6, new StudentGroup(Color.RED, 5));

        AnsiConsole.systemInstall();
        for(Island is : game.getBoard().getIslands()) {
            List<String> strIs = is.print(false, false, false, false);
            for(String ilS : strIs) {
                AnsiConsole.sysOut().println(ilS);
            }
        }
    }*/
}