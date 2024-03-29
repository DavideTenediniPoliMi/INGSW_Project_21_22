package it.polimi.ingsw.view.cli.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

/**
 * Class that manages MN phase view
 */
public class MNViewState extends ExpertViewState {
    public MNViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt(boolean shouldPrint) {
        if(!shouldPrint) return;

        isStudentsPhase = false;

        if(matchInfo.isExpertMode() && !choiceSelected && menuChoice != 2) {
            super.printCLIPrompt(true);
            return;
        }

        if(isMovingMN || !matchInfo.isExpertMode())
            appendBuffer("Select the island on which you want to move mother nature: [index of island]");
    }

    @Override
    public String manageCLIInput(String input) {
        String error;

        if(matchInfo.isExpertMode() && !choiceSelected && menuChoice != 2)
            return super.manageCLIInput(input);

        appendBuffer(input);
        if(isMovingMN || !matchInfo.isExpertMode()) {
            error = StringUtils.checkInteger(input, validIslandIndexes);
            if (!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            int islandAbsoluteIndex = game.getBoard().getOriginalIndexOf(Integer.parseInt(input));
            int islandIndex = game.getBoard().getIndexOfMultiIslandContainingIslandOfIndex(islandAbsoluteIndex);

            int MNPosition = game.getBoard().getMNPosition();
            int steps = game.getPlayerByID(matchInfo.getCurrentPlayerID()).getSelectedCard().RANGE;
            if(islandIndex > MNPosition) {
                if(islandIndex - MNPosition > steps) {
                    error = "The island you have selected is too far!";
                    appendBuffer(error);
                    return error;
                }
            } else {
                if(game.getBoard().getNumIslands() + islandIndex - MNPosition > steps) {
                    error = "The island you have selected is too far!";
                    appendBuffer(error);
                    return error;
                }
            }

            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.MOVE_MN)
                            .setIndex(islandIndex)
                            .serialize().toString()
            );
            isMovingMN = false;
            setInteractionComplete(true);
            return "";
        }
        return "";
    }

    @Override
    public void resetInteraction() {
        super.resetInteraction();
    }
}
