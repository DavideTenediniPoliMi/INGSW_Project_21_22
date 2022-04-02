package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class DiningRoomController {

    public DiningRoomController() {

    }

    public void manageDiningRoomOf(int playerID, StudentGroup studentGroup) {
        checkProfessorOwner(playerID, studentGroup);
    }

    private void checkProfessorOwner(int playerID, StudentGroup studentGroup) {
        Game game = Game.getInstance();
        Board board = game.getBoard();

        for(Color c : Color.values()) {
            if(studentGroup.getByColor(c) > 0) {
                int professorOwnerID = board.getProfessorOwners().getOwnerIDByColor(c);
                if (professorOwnerID == -1) {
                    game.giveProfessorTo(playerID, c);
                    return;
                }

                int numStuds = board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
                int numStudsProfessor = board.getSchoolByPlayerID(professorOwnerID).getNumStudentsInDiningRoomByColor(c);
                if (numStuds > numStudsProfessor)
                    game.giveProfessorTo(playerID, c);
            }
        }
    }
}
