package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class DiningRoomExpertController extends DiningRoomController{

    public DiningRoomExpertController() {

    }

    @Override
    public void manageDiningRoomOf(int playerID, StudentGroup studentGroup) {
        super.manageDiningRoomOf(playerID, studentGroup);
        checkCoin(playerID, studentGroup);
    }

    private void checkCoin(int playerID, StudentGroup studentGroup) {
        Game game = Game.getInstance();
        Board board = game.getBoard();

        for(Color c: Color.values()) {
            int numStuds = board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
            int numStudsAdded = studentGroup.getByColor(c);
            //gives coin only if numStudents is multiple of three or with the addition passes over a multiple of three
            if((numStuds % 3 == 0 && numStudsAdded > 0) || (numStuds % 3 == 1 && numStuds-numStudsAdded % 3 == 2))
                game.giveCoinToPlayer(playerID);
        }
    }
}
