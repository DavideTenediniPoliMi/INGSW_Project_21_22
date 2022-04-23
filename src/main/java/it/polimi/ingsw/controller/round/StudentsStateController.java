package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.students.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.students.StudentTransferException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;

import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Class specific to the Student transferring part of the action phase. Allows for student transfers and
 * <code>CharacterCard</code>-related actions.
 */
public class StudentsStateController extends CharacterCardPlayableStateController{
    private final int NUM_MOVABLE_STUDENTS = MatchInfo.getInstance().getMaxMovableStudents();

    /**
     * Sole constructor for <code>StudentsStateController</code>.
     *
     * @param oldState the old <code>RoundStateController</code> to transition from.
     */
    public StudentsStateController(RoundStateController oldState) {
        super(oldState, TurnState.STUDENTS);
        MatchInfo.getInstance().resetNumMovedStudents();
    }

    @Override
    public void transferStudentToIsland(int islandIndex, Color c) throws StudentTransferException, NotEnoughStudentsException, BadParametersException {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(islandIndex < 0 || islandIndex >= Game.getInstance().getBoard().getNumIslands()) {
            throw new BadParametersException("islandIndex is " + islandIndex + ", expected between 0 and " + Game.getInstance().getBoard().getNumIslands());
        }else if(matchInfo.getNumMovedStudents() >= NUM_MOVABLE_STUDENTS) {
            throw new StudentTransferException("Player already moved " + NUM_MOVABLE_STUDENTS + " students!");
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0){
            Game.getInstance().transferStudentToIsland(islandIndex, c, matchInfo.getCurrentPlayerID());
            matchInfo.studentWasMoved();
        }else {
            throw new NotEnoughStudentsException(c);
        }
    }

    @Override
    public void transferStudentToDiningRoom(Color c) throws StudentTransferException, NotEnoughStudentsException {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(matchInfo.getNumMovedStudents() >= NUM_MOVABLE_STUDENTS) {
            throw new StudentTransferException("Player already moved " + NUM_MOVABLE_STUDENTS + " students!");
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0) {
            Game.getInstance().transferStudentToDiningRoom(matchInfo.getCurrentPlayerID(), c);

            diningRoomController.manageDiningRoomOf(matchInfo.getCurrentPlayerID(), new StudentGroup(c, 1));

            matchInfo.studentWasMoved();
        }else {
            throw new NotEnoughStudentsException(c);
        }
    }
}
