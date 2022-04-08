package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;

import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;

public class StudentsStateController extends CharacterCardPlayableStateController{
    private final int NUM_MOVABLE_STUDENTS = 3; //TODO change

    public StudentsStateController(RoundStateController oldState) {
        super(oldState, TurnState.STUDENTS);
        MatchInfo.getInstance().resetNumMovedStudents();
    }

    @Override
    public void transferStudentToIsland(int islandIndex, Color c) {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(islandIndex < 0 || islandIndex >= Game.getInstance().getBoard().getNumIslands()) {
            throw new BadParametersException("islandIndex is " + islandIndex + ", expected between 0 and " + Game.getInstance().getBoard().getNumIslands());
        }else if(matchInfo.getNumMovedStudents() >= NUM_MOVABLE_STUDENTS) {
            //EXCEPTION
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0){
            Game.getInstance().transferStudentToIsland(islandIndex, c, matchInfo.getCurrentPlayerID());
            matchInfo.studentWasMoved();

            if(matchInfo.getNumMovedStudents() == NUM_MOVABLE_STUDENTS) {
                //TODO nextPlayer();
                matchInfo.resetNumMovedStudents();
            }
        }else {
            //NOT ENOUGH STUDENTS EXCEPTION
        }
    }

    @Override
    public void transferStudentToDiningRoom(Color c) {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(matchInfo.getNumMovedStudents() >= NUM_MOVABLE_STUDENTS) {
            //EXCEPTION
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0) {
            Game.getInstance().transferStudentToDiningRoom(matchInfo.getCurrentPlayerID(), c);
            matchInfo.studentWasMoved();

            if(matchInfo.getNumMovedStudents() == NUM_MOVABLE_STUDENTS) {
                //TODO nextPlayer();
                matchInfo.resetNumMovedStudents();
            }
        }else {
            //NOT ENOUGH STUDENTS EXCEPTION
        }
    }
}
