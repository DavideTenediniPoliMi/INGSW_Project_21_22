package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class ReturnToBagDecorator extends CharacterCardDecorator {
    private final int NUM_STUDENTS_TO_RETURN = 3;

    private Color selectedColor;

    public ReturnToBagDecorator(CharacterCard card) {
        super(card);
    }

    @Override
    public void setParameters(Parameters params) {
        selectedColor = params.getSelectedColor();
    }

    @Override
    public int activate() {
        card.activate();

        Game game = Game.getInstance();
        Board board = game.getBoard();

        for(Player player: game.getPlayers()) {
            School targetSchool = board.getSchoolByPlayerID(player.getID());

            int numAvailableStudents = targetSchool.getNumStudentsInDiningRoomByColor(selectedColor);
            int numStudentsToReturn = Math.min(NUM_STUDENTS_TO_RETURN, numAvailableStudents);

            StudentGroup temp = new StudentGroup(selectedColor, numStudentsToReturn);

            board.removeFromDiningRoomOf(player.getID(), temp);
            game.putStudentsBack(temp);
        }

        return 0;
    }
}
