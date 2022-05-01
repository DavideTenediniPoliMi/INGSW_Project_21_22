package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;

public class BuyCharacterCardCommand implements Command {

    private final int cardIndex;
    private final GameController gameController;

    public BuyCharacterCardCommand(int cardIndex, GameController gameController) {
        this.cardIndex = cardIndex;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws IllegalActionException {
        gameController.getRoundStateController().buyCharacterCard(cardIndex);
    }
}