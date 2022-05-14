package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.CharacterCardActivationException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.player.NotEnoughCoinsException;

/**
 * Command class to request the purchase of a <code>CharacterCard</code>.
 */
public class BuyCharacterCardCommand implements Command {

    private final int cardIndex;
    private final GameController gameController;

    public BuyCharacterCardCommand(int cardIndex, GameController gameController) {
        this.cardIndex = cardIndex;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws IllegalActionException, NotEnoughCoinsException, CharacterCardActivationException {
        gameController.getRoundStateController().buyCharacterCard(cardIndex);
    }
}
