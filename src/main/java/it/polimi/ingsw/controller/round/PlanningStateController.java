package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnState;

import java.util.Comparator;
import java.util.List;

public class PlanningStateController extends RoundStateController {
    public PlanningStateController(RoundStateController oldState) {
        super(oldState, TurnState.PLANNING);
    }

    @Override
    public void definePlayOrder() {
        List<Player> players = Game.getInstance().getPlayers();
        players.sort(Comparator.comparing(Player::getSelectedCard));

        for(Player p: players) {
            playOrder.add(p.getID());
        }
    }

    @Override
    public void playCard(int cardIndex) {
        // TODO
    }
}
