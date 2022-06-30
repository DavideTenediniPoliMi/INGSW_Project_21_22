package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.board.MNOutOfRangeException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Class specific to the movement part of the action phase of a round. Allows for Mother Nature movement and
 * <code>CharacterCard</code>-related actions.
 */
public class MNStateController extends CharacterCardPlayableStateController {
    /**
     * Sole constructor for <code>MNStateController</code>.
     *
     * @param oldState the old <code>RoundStateController</code> to transition from.
     */
    public MNStateController(RoundStateController oldState) {
        super(oldState, TurnState.MOTHER_NATURE);
    }

    @Override
    public void moveMN(int destIndex) throws MNOutOfRangeException {
        Game game = Game.getInstance();
        Player player = game.getPlayerByID(MatchInfo.getInstance().getCurrentPlayerID());

        if(player == null) {
            throw new NullPlayerException();
        }
        // Calculate steps
        Board board = Game.getInstance().getBoard();
        int MNIndex = board.getMNPosition();
        int numIslands = board.getNumIslands();
        int steps;
        if(destIndex > MNIndex) {
            steps = destIndex - MNIndex;
        }else {
            steps = numIslands + destIndex - MNIndex;
        }

        if (steps > player.getSelectedCard().RANGE) {
            throw new MNOutOfRangeException(steps, player.getSelectedCard().RANGE);
        }


        islandController.moveMN(steps);
    }

    @Override
    public void skip() {
        Game game = Game.getInstance();
        MatchInfo matchInfo = MatchInfo.getInstance();
        matchInfo.setMNMoved(true);

        // Check if player needs to receive students
        School currentSchool = game.getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());
        int studentAmount = 0;
        for(Color color : Color.values()) {
            studentAmount += currentSchool.getNumStudentsInEntranceByColor(color);
        }

        if(studentAmount < matchInfo.getInitialNumStudents()) {
            // Player has to receive max amt of movable students
            StudentGroup students = game.drawStudents(matchInfo.getMaxMovableStudents());
            game.getBoard().addToEntranceOf(matchInfo.getCurrentPlayerID(), students);
        }
    }
}
