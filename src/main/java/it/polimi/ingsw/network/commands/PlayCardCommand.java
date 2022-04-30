package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.player.CardUsedException;

public class PlayCardCommand implements Command {

    private final int cardIndex;
    private final GameController gameController;

    public PlayCardCommand(int cardIndex, GameController gameController) {
        this.cardIndex = cardIndex;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws IllegalActionException, CardUsedException {
        gameController.getRoundStateController().playCard(cardIndex);
    }

}
