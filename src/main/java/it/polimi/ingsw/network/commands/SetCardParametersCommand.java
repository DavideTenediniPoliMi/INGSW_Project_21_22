package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.parameters.CardParameters;

public class SetCardParametersCommand implements Command {

    private final int cardIndex;
    private final CardParameters cardParams;
    private final GameController gameController;

    public SetCardParametersCommand(int cardIndex, CardParameters cardParams, GameController gameController) {
        this.cardIndex = cardIndex;
        this.cardParams = cardParams;
        this.gameController = gameController;
    }

    @Override
    public void execute() throws IllegalActionException {
        //TODO move this in CharacterCardController or sth
        Game game = Game.getInstance();
        if(game.getCharacterCards().indexOf(game.getActiveCharacterCard()) != cardIndex){
            throw new BadParametersException("Given ID does not match the active CharacterCard's ID");
        }

        gameController.getRoundStateController().setCardParameters(cardParams);
    }
}
