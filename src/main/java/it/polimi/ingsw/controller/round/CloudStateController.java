package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
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
        int firstPlayerToActIndex = players.indexOf(firstPlayerToAct);

        Collections.rotate(players, firstPlayerToActIndex * -1); // times -1 so that it rotates to the left
    }

    @Override
    public void collectFromCloud(int cloudIndex) {
        Game game = Game.getInstance();
        int currentPlayerID = getCurrentPlayerID();

        if(currentPlayerID == -1) {
            throw new NullPlayerException();
        } else if (!game.getBoard().getClouds().get(cloudIndex).isAvailable()){
            //EXCEPTION
        }

        game.collectFromCloud(cloudIndex, currentPlayerID);
    }
}
