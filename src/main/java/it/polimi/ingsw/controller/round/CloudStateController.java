package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.board.CloudUnavailableException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.enumerations.TurnState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CloudStateController extends RoundStateController {
    public CloudStateController(RoundStateController oldState) {
        super(oldState, TurnState.CLOUD);
    }

    @Override
    public void definePlayOrder() {
        List<Player> players = Game.getInstance().getPlayers();

        Player firstPlayerToAct = players.stream().min(Comparator.comparing(Player::getSelectedCard)).orElse(null);
        defineClockWiseOrder(players.indexOf(firstPlayerToAct));
    }

    @Override
    public void collectFromCloud(int cloudIndex) throws NullPlayerException, CloudUnavailableException {
        Game game = Game.getInstance();
        int currentPlayerID = MatchInfo.getInstance().getCurrentPlayerID();

        if(currentPlayerID == -1) {
            throw new NullPlayerException();
        } else if (!game.getBoard().getClouds().get(cloudIndex).isAvailable()){
            throw new CloudUnavailableException(cloudIndex);
        }

        game.collectFromCloud(cloudIndex, currentPlayerID);
    }
}
