package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Class to manage the behaviour of 1 <code>CharacterCard</code>, <code>RETURN_TO_BAG</code>.
 */
public class ReturnToBagDecorator extends CharacterCardDecorator {
    private final int NUM_STUDENTS_TO_RETURN = 3;
    private Color selectedColor;

    /**
     * Constructor that instantiates this card.
     *
     * @param card the <code>GenericCard</code> used in the decoration process
     */
    public ReturnToBagDecorator(CharacterCard card) {
        super(card);
    }

    /**
     * Sets necessary parameters for this card to be used. The field <code>selectedColor</code>
     * must NOT be <code>null</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    @Override
    public void setParameters(CardParameters params) {
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

    //NOTIFY SCHOOLS, BAG
}
