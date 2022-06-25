package it.polimi.ingsw.view.cli.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages planning phase view
 */
@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class PlanningViewState extends GameViewState {
    private final Game game = Game.getInstance();
    private final List<Integer> validNumbers = new ArrayList<>();
    private final List<Integer> validIndexes = new ArrayList<>();

    public PlanningViewState(ViewState oldViewState) {
        super(oldViewState);
    }
    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt(boolean shouldPrint) {
        if(!shouldPrint) return;

        setInteractionComplete(false);
        List<Card> playableCards = game.getPlayerByID(playerID).getPlayableCards();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Select the card you want to play:\n");
        int i = 1;
        for(Card c : playableCards) {
            if(!c.isUsed() || playableCards.size() == 1) {
                stringBuilder.append(i + ") " + c.name() + " (weight: " + c.WEIGHT + ", range: " + c.RANGE + ")\n");
                validNumbers.add(i);
                validIndexes.add(c.ordinal());
                i++;
            }
        }

        stringBuilder.setLength(stringBuilder.length()-1);
        appendBuffer(stringBuilder.toString());
    }

    @Override
    public String manageCLIInput (String input) {
        appendBuffer(input);
        String error;

        error = StringUtils.checkInteger(input, validNumbers);
        if(!error.equals("")) {
            appendBuffer(error);
            return error;
        }
        int index = validIndexes.get(Integer.parseInt(input)-1);
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.PLAY_CARD)
                        .setIndex(index)
                        .serialize().toString()
        );
        setInteractionComplete(true);
        return "";
    }

    @Override
    public void resetInteraction() {
        validNumbers.clear();
        validIndexes.clear();
    }
}
