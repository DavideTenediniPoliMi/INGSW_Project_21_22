package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Card;
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

    /*
    * Assuming the request is already verified at this point (Sender is currentPlayer)
    * */
    @Override
    public void playCard(int cardIndex) {
        /*
        * Checks
        *   - Check if player has already played?
        *   - Check if player CAN play this card
        *   - Check if card was already played this round
        * */
        if(cardIndex < 0 || cardIndex > 9) {
            throw new BadParametersException("cardIndex is " + cardIndex + ", expected between 0 and 9");
        }

        Player p = Game.getInstance().getPlayerByID(getCurrentPlayerID());
        Card card = Card.values()[cardIndex];

        if(p.getPlayableCards().contains(card) && !card.isUsed()) {
            Game.getInstance().playCard(p.getID(), card);
            //TODO nextPlayer();
        }else {
            //CARD USED EXCEPTION
        }
    }
}
