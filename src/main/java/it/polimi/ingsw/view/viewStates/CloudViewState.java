package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CloudViewState extends GameViewState {
    private final List<Integer> validIndexes = new ArrayList<>();

    public CloudViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        setInteractionComplete(false);
        Game game = Game.getInstance();

        for(Cloud cloud : game.getBoard().getClouds()) {
            if(cloud.isAvailable())
                validIndexes.add(game.getIndexOfCloud(cloud));
        }

        appendBuffer("Select the cloud from which you want to take the students: " + validIndexes.toString());
    }

    @Override
    public String manageCLIInput(String input) {
        appendBuffer(input);

        String error = StringUtils.checkInteger(input, validIndexes);

        if(!error.equals("")) {
            appendBuffer(error);
            return error;
        }

        int cloudIndex = Integer.parseInt(input);

        notify(
                new RequestParameters()
                        .setCommandType(CommandType.COLLECT_FROM_CLOUD)
                        .setIndex(cloudIndex)
                        .serialize().toString()
        );
        setInteractionComplete(true);
        return "";
    }

    @Override
    public void resetInteraction() {
        validIndexes.clear();
    }
}
