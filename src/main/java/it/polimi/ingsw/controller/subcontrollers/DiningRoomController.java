package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Subcontroller handling all actions related to check for professors on student transfer events.
 */
public class DiningRoomController {

    /**
     * Sole constructor
     */
    public DiningRoomController() {

    }

    /**
     * Calls for professor owner checks with the specified parameters.
     *
     * @param playerID the ID of the current <code>Player</code>.
     * @param studentGroup the students part of a transfer.
     */
    public void manageDiningRoomOf(int playerID, StudentGroup studentGroup) {
        checkProfessorOwner(playerID, studentGroup);
    }

    /**
     * Checks whether a <code>Player</code> needs to receive a professor after transferring students in its dining room.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param studentGroup the students part of a transfer.
     */
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
