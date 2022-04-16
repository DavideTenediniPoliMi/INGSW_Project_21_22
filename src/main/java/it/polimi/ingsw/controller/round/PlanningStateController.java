package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.player.CardUsedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.TurnState;

import java.util.Comparator;
import java.util.List;

/**
 * Class specific to the Planning phase of the game. Defines the order of play for a new round and allows playing
 * Assistant cards.
 */
public class PlanningStateController extends RoundStateController {
    /**
     * Sole constructor for <code>PlanningStateController</code>.
     *
     * @param oldState the old <code>RoundStateController</code> to transition from.
     */
    public PlanningStateController(RoundStateController oldState) {
        super(oldState, TurnState.PLANNING);
        Game.getInstance().refillClouds(MatchInfo.getInstance().getMaxMovableStudents());
    }

    /**
     * Defines the new order of play based on the Assistant cards played.
     */
    @Override
    public void definePlayOrder() {
        List<Player> players = Game.getInstance().getPlayers();
        players.sort(Comparator.comparing(Player::getSelectedCard)
                .thenComparing(player -> player.getSelectedCard().getUseOrder().indexOf(player.getID())));

        for(Player p: players) {
            MatchInfo.getInstance().addPlayer(p.getID());
        }
    }

    @Override
    public void playCard(int cardIndex) throws CardUsedException {
        if(cardIndex < 0 || cardIndex > 9) {
            throw new BadParametersException("cardIndex is " + cardIndex + ", expected between 0 and 9");
        }

        Player p = Game.getInstance().getPlayerByID(MatchInfo.getInstance().getCurrentPlayerID());
        Card card = Card.values()[cardIndex];

        if(p.getPlayableCards().contains(card) && (!card.isUsed() || p.getPlayableCards().size() == 1)) {
            Game.getInstance().playCard(p.getID(), card);
        }else {
            throw new CardUsedException(card);
        }
    }
}
