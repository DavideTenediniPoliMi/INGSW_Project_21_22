package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;

/**
 * Command class to request the activation of a <code>CharacterCard</code>.
 */
public class ActivateCharacterCardCommand implements Command {
    private final GameController gameController;

    public ActivateCharacterCardCommand(GameController gameController) {
        this.gameController = gameController;
    }
    @Override
    public void execute() throws IllegalActionException {
        gameController.getRoundStateController().activateCard();
    }
}
