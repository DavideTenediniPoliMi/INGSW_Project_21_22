package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;

public class DiningRoomController {

    public DiningRoomController() {

    }

    public void manageDiningRoomOf(int playerID, Color c) {
        checkProfessorOwner(playerID, c);
    }

    private void checkProfessorOwner(int playerID, Color c) {
        Game game = Game.getInstance();
        Board board = game.getBoard();

        int numStuds = board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
        int professorOwnerID = board.getProfessorOwners().getOwnerIDByColor(c);
        int numStudsProfessor = board.getSchoolByPlayerID(professorOwnerID).getNumStudentsInDiningRoomByColor(c);
        if(numStuds > numStudsProfessor)
            game.giveProfessorTo(playerID, c);
    }
}
