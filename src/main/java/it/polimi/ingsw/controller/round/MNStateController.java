package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.board.MNOutOfRangeException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnState;

public class MNStateController extends CharacterCardPlayableStateController {
    public MNStateController(RoundStateController oldState) {
        super(oldState, TurnState.MOTHER_NATURE);
    }

    @Override
    public void moveMN(int steps) throws MNOutOfRangeException {
        Game game = Game.getInstance();
        Player player = game.getPlayerByID(MatchInfo.getInstance().getCurrentPlayerID());

        if(player.getID() == -1) {
            throw new NullPlayerException();
        } else if (steps > player.getSelectedCard().RANGE) {
            throw new MNOutOfRangeException(steps, player.getSelectedCard().RANGE);
        }

        islandController.moveMN(steps);
    }
}
