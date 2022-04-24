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
    private CardParameters p1, p2;

    @BeforeEach
    public void setUp() throws Exception {
        game = Game.getInstance();
        board = game.getBoard();
        Player player = new Player(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);

        game.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
        board.addSchool(player);

        StudentGroup entrance = new StudentGroup(Color.BLUE, 3);
        StudentGroup dining = new StudentGroup(Color.GREEN, 2);
        board.addToEntranceOf(0, entrance);
        board.addToDiningRoomOf(0, dining);

        c = new ExchangeStudentsDecorator(new GenericCard(2, EffectType.EXCHANGE_STUDENTS));

        p1 = new CardParameters();
        p1.setFromOrigin(new StudentGroup(Color.BLUE,1));
        p1.setFromDestination(new StudentGroup(Color.GREEN,1));
        c.setParameters(p1);
    }

    @AfterEach
    public void tearDown() throws Exception {
        game = null;
        board = null;
        c = null;
        p1 = p2 = null;
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
}