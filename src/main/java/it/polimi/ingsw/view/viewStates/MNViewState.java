package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

public class MNViewState extends ExpertViewState {
    public MNViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        isStudentsPhase = false;

        super.printCLIPrompt();

        appendBuffer("Select the island on which you want to move mother nature: [index of island]");
    }

    @Override
    public String manageCLIInput(String input) {
        appendBuffer(input);
        String error;

        if(!super.manageCLIInput(input).equals("")) {
            error = StringUtils.checkInteger(input, validIslandIndexes);
            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            int islandIndex = game.getBoard().getOriginalIndexOf(Integer.parseInt(input));

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
        return "That was not a valid choice! Try again!";
    }

    @Override
    public void resetInteraction() {
        super.resetInteraction();
    }
}
