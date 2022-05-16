package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class SchoolTest {
    Player p1;
    Player p2;
    School s1;
    School s2;

    @BeforeEach
    public void setUp() {
        p1 = new Player(0, "Pippo", TowerColor.GREY, CardBack.CB_1, true);
        p2 = new Player(1, "Pluto", TowerColor.WHITE, CardBack.CB_2, true);

        s1 = new School(p1);
        s2 = new School(p2);

        s1.addTowers(6);

        StudentGroup sg1 = new StudentGroup(Color.GREEN, 3);
        sg1.addByColor(Color.BLUE, 1);

        s2.addToEntrance(sg1);
    }

    @AfterEach
    void tearDown() {
        p1 = p2 = null;
        s1 = s2 = null;
    }

    @Test
    public void getOwnerTest() {
        assertAll(
                () -> assertEquals(p1, s1.getOwner()),
                () -> assertEquals(p2, s2.getOwner())
        );
    }

    @Test
    public void removeTowersTest() {
        s1.removeTowers(2);
        s2.removeTowers(2);
        assertAll(
                () -> assertEquals(0, s2.getNumTowers()),
                () -> assertEquals(4, s1.getNumTowers())
        );
    }

    @Test
    public void numTowersTest() {
        assertAll(
                () -> assertEquals(0, s2.getNumTowers()),
                () -> assertEquals(6, s1.getNumTowers())
        );
    }

    @Test
    public void studentInEntranceTest() {
        assertAll(
                () -> assertEquals(3, s2.getNumStudentsInEntranceByColor(Color.GREEN)),
                () -> assertEquals(1, s2.getNumStudentsInEntranceByColor(Color.BLUE))
        );

        s2.removeFromEntrance(new StudentGroup(Color.BLUE, 1));

        assertEquals(0, s2.getNumStudentsInEntranceByColor(Color.BLUE));
    }

    @Test
    public void studentsInDiningRoomTest() {
        for(Color c: Color.values()) {
            assertEquals(0, s1.getNumStudentsInDiningRoomByColor(c));
        }
        StudentGroup sg = new StudentGroup(Color.BLUE, 2);
        StudentGroup sg1 = new StudentGroup(Color.BLUE, 2);

        s1.addToDiningRoom(sg);
        s2.addToDiningRoom(sg1);

        s2.removeFromDiningRoom(new StudentGroup(Color.BLUE, 1));

        assertAll(
                () -> assertEquals(2, s1.getNumStudentsInDiningRoomByColor(Color.BLUE)),
                () -> assertEquals(1, s2.getNumStudentsInDiningRoomByColor(Color.BLUE))
        );
    }

    /*@Test
    public void printTest() {
        AnsiConsole.systemInstall();
        AnsiConsole.sysOut().println(s1.print());
        s1.print();
        Game game = Game.getInstance();
        game.addPlayer(p2);
        game.playCard(1, Card.CARD_10);
        StudentGroup s = new StudentGroup(Color.RED, 2);
        s.addByColor(Color.BLUE, 3);
        s.addByColor(Color.PINK, 2);
        s2.addToEntrance(s);

        StudentGroup s22 = new StudentGroup();
        s22.addByColor(Color.YELLOW, 7);
        s22.addByColor(Color.GREEN, 4);
        s2.addToDiningRoom(s22);

        game.giveProfessorTo(1, Color.YELLOW);
        game.giveProfessorTo(1, Color.GREEN);

        AnsiConsole.sysOut().println(s2.print());
        s2.print();
    }*/
}