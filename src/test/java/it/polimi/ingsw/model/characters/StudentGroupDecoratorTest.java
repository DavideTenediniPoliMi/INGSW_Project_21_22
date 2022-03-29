package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class StudentGroupDecoratorTest extends TestCase {
    Game game;
    Board board;
    StudentGroupDecorator c1, c2, c3;
    Parameters p1, p2, p3;
    StudentGroup sg1, sg2, sg3, sg4;

    @BeforeEach
    public void setUp() throws Exception {
        game = Game.getInstance();
        board = game.getBoard();

        c1 = new StudentGroupDecorator(new GenericCard(3, EffectType.STUDENT_GROUP), true, false);
        c2 = new StudentGroupDecorator(new GenericCard(3, EffectType.STUDENT_GROUP), false, true);
        c3 = new StudentGroupDecorator(new GenericCard(3, EffectType.STUDENT_GROUP), false, false);


        //toIsland
        sg1 = null;
        p1 = new Parameters();

        StudentGroup cardStudents = new StudentGroup();
        for(Color c : Color.values()) {
            int numStudInCardByColor = c1.getStudentsByColor(c);
            cardStudents.addByColor(c, numStudInCardByColor);
            if(numStudInCardByColor > 0) {
                sg1 = new StudentGroup(c, 1);
                p1.setFromOrigin(new StudentGroup(c, 1));
                break;
            }
        }

        p1.setIslandIndex(0);
        c1.setParameters(p1);


        //toDiningRoom
        game.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
        board.addSchool(game.getPlayerByID(0));

        sg2 = null;
        p2 = new Parameters();

        StudentGroup cardStudents2 = new StudentGroup();
        for(Color c : Color.values()) {
            int numStudInCardByColor = c2.getStudentsByColor(c);
            cardStudents2.addByColor(c, numStudInCardByColor);
            if(numStudInCardByColor > 0) {
                sg2 = new StudentGroup(c, 1);
                p2.setFromOrigin(new StudentGroup(c, 1));
                break;
            }
        }

        p2.setPlayerID(0);
        c2.setParameters(p2);


        //toEntrance

        sg3 = null;
        p3 = new Parameters();

        StudentGroup cardStudents3 = new StudentGroup();
        for(Color c : Color.values()) {
            int numStudInCardByColor = c3.getStudentsByColor(c);
            cardStudents3.addByColor(c, numStudInCardByColor);
            if(numStudInCardByColor > 0) {
                sg3 = new StudentGroup(c, 1);
                p3.setFromOrigin(new StudentGroup(c, 1));
                break;
            }
        }

        game.giveStudentsTo(0, 1);
        sg4 = null;

        StudentGroup entranceStudent = new StudentGroup();
        for(Color c : Color.values()) {
            int numStudInEntranceByColor = board.getSchoolByPlayerID(0).getNumStudentsInEntranceByColor(c);
            entranceStudent.addByColor(c, numStudInEntranceByColor);
            if(numStudInEntranceByColor > 0) {
                sg4 = new StudentGroup(c, 1);
                p3.setFromDestination(new StudentGroup(c, 1));
                break;
            }
        }

        p3.setPlayerID(0);
        c3.setParameters(p3);
    }

    @AfterEach
    public void tearDown() throws Exception {
        Game.resetInstance();
        board = null;
        c1 = c2 = null;
        p1 = p2 = null;
    }

    @Test
    public void testSetParameters() {
    }

    @Test
    public void testActivate_isToIsland() {
        int studentsIslandBefore[] = new int[5];
        for(Color c : Color.values()) {
            studentsIslandBefore[c.ordinal()] = board.getIslandAt(p1.getIslandIndex()).getNumStudentsByColor(c);
        }

        c1.activate();

        for(Color c : Color.values()) {
            int numStudentAfter = studentsIslandBefore[c.ordinal()] + sg1.getByColor(c);
            assertEquals(numStudentAfter, board.getIslandAt(p1.getIslandIndex()).getNumStudentsByColor(c));
        }
    }

    @Test
    public void testActivate_isToDining() {
        int studentsDiningBefore[] = new int[5];
        for(Color c : Color.values()) {
            studentsDiningBefore[c.ordinal()] = board.getSchoolByPlayerID(p2.getPlayerID()).getNumStudentsInDiningRoomByColor(c);
        }

        c2.activate();

        for(Color c : Color.values()) {
            int numStudentAfter = studentsDiningBefore[c.ordinal()] + sg2.getByColor(c);
            assertEquals(numStudentAfter, board.getSchoolByPlayerID(p2.getPlayerID()).getNumStudentsInDiningRoomByColor(c));
        }
    }

    @Test
    public void testActivate_isToEntrance() {
        int studentsEntranceBefore[] = new int[5];
        int studentsCardBefore[] = new int[5];
        for(Color c : Color.values()) {
            studentsCardBefore[c.ordinal()] = sg3.getByColor(c);
            studentsEntranceBefore[c.ordinal()] = board.getSchoolByPlayerID(p3.getPlayerID()).getNumStudentsInEntranceByColor(c);
        }

        c3.activate();

        for(Color c : Color.values()) {
            int numStudentCardAfter = studentsCardBefore[c.ordinal()] + sg4.getByColor(c) - sg3.getByColor(c);
            int numStudentEntranceAfter = studentsEntranceBefore[c.ordinal()] + sg3.getByColor(c) - sg4.getByColor(c);
            assertEquals(numStudentCardAfter, studentsEntranceBefore[c.ordinal()]);
            assertEquals(numStudentEntranceAfter, studentsCardBefore[c.ordinal()]);
        }
    }
}