package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeStudentsDecoratorTest {
    private Game game;
    private Board board;
    private ExchangeStudentsDecorator c;
    private CardParameters p1;

    @BeforeEach
    public void setUp() throws Exception {
        game = Game.getInstance();
        board = game.getBoard();
        Player player = new Player(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);

        game.addPlayer(new Player(0, "mario", TowerColor.BLACK, CardBack.CB_1, true));
        board.addSchool(player);

        StudentGroup entrance = new StudentGroup(Color.BLUE, 3);
        StudentGroup dining = new StudentGroup(Color.GREEN, 2);
        board.addToEntranceOf(0, entrance);
        board.addToDiningRoomOf(0, dining);

        c = new ExchangeStudentsDecorator(new GenericCard("EXCHANGE_STUDENTS", 2, EffectType.EXCHANGE_STUDENTS));

        p1 = new CardParameters();
        p1.setPlayerID(0);
        p1.setFromOrigin(new StudentGroup(Color.BLUE,1));
        p1.setFromDestination(new StudentGroup(Color.GREEN,1));
        c.setParameters(p1);
    }

    @AfterEach
    void tearDown() {
        game = null;
        board = null;
        c = null;
        p1 = null;
        Game.resetInstance();
    }

    @Test
    public void testSetParameters() {
    }

    @Test
    public void testActivate() {
        School s = board.getSchoolByPlayerID(0);
        StudentGroup entrance = new StudentGroup();
        StudentGroup dining = new StudentGroup();
        for (Color c : Color.values()){
            entrance.addByColor(c, s.getNumStudentsInEntranceByColor(c));
            dining.addByColor(c, s.getNumStudentsInDiningRoomByColor(c));
        }

        c.activate();

        for(Color c : Color.values()) {
            int numStudentsFromEntrance = p1.getFromOrigin().getByColor(c);
            int numStudentsFromDining = p1.getFromDestination().getByColor(c);
            if (numStudentsFromEntrance != 0) {
                assertEquals(entrance.getByColor(c) - numStudentsFromEntrance, s.getNumStudentsInEntranceByColor(c));
                assertEquals(dining.getByColor(c) + numStudentsFromEntrance, s.getNumStudentsInDiningRoomByColor(c));
                assertEquals(dining.getByColor(c) - numStudentsFromDining, s.getNumStudentsInDiningRoomByColor(c));
                assertEquals(entrance.getByColor(c) + numStudentsFromDining, s.getNumStudentsInEntranceByColor(c));
            }
        }
    }

    @Test
    void serializeTest() {
        System.out.println(c.serialize());
    }

    @Test
    void deserializeTest() {
        System.out.println(c.serialize());
        ExchangeStudentsDecorator c1 = new ExchangeStudentsDecorator(new GenericCard("EXCHANGE_STUDENTS", 3,EffectType.EXCHANGE_STUDENTS));
        c1.deserialize(c.serialize());
        System.out.println(c1.serialize());
    }
}