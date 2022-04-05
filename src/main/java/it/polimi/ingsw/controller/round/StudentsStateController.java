package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;

public class StudentsStateController extends CharacterCardPlayableStateController{
    private final int NUM_MOVABLE_STUDENTS = 3; //TODO change

    public StudentsStateController(RoundStateController oldState) {
        super(oldState, TurnState.STUDENTS);
        numMovedStudents = 0;
    }

    @Override
    public void transferStudentToIsland(int islandIndex, Color c) {

        if(islandIndex < 0 || islandIndex >= Game.getInstance().getBoard().getNumIslands()) {
            //BAD PARAMETERS EXCEPTION
        }else if(numMovedStudents >= NUM_MOVABLE_STUDENTS) {
            //EXCEPTION
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0){
            Game.getInstance().transferStudentToIsland(islandIndex, c, getCurrentPlayerID());
            numMovedStudents++;

            if(numMovedStudents == NUM_MOVABLE_STUDENTS) {
                //TODO nextPlayer();
                numMovedStudents = 0;
            }
        }else {
            //NOT ENOUGH STUDENTS EXCEPTION
        }
    }

    @Override
    public void transferStudentToDiningRoom(Color c) {
        if(numMovedStudents >= NUM_MOVABLE_STUDENTS) {
            //EXCEPTION
        }

        School playerSchool = Game.getInstance().getBoard().getSchoolByPlayerID(getCurrentPlayerID());

        if(playerSchool.getNumStudentsInEntranceByColor(c) > 0) {
            Game.getInstance().transferStudentToDiningRoom(getCurrentPlayerID(), c);
            numMovedStudents++;

            if(numMovedStudents == NUM_MOVABLE_STUDENTS) {
                //TODO nextPlayer();
                numMovedStudents = 0;
            }
        }else {
            //NOT ENOUGH STUDENTS EXCEPTION
        }
    }
}
