package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class PlanningViewState extends GameViewState {
    private final Game game = Game.getInstance();
    private final MatchInfo matchInfo = MatchInfo.getInstance();
    private final List<Integer> validNumbers = new ArrayList<>();

    public PlanningViewState(ViewState oldViewState) {
        super(oldViewState);
    }
    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        setInteractionComplete(false);
        List<Card> playableCards = game.getPlayerByID(playerID).getPlayableCards();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Select the card you want to play:\n");
        int i = 1;
        for(Card c : playableCards) {
            stringBuilder.append(i + ") " + c.name() + " (weight: " + c.WEIGHT + ", range: " + c.RANGE + ")\n");
            validNumbers.add(i);
            i++;
        }

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
        int index = Integer.parseInt(input);
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.PLAY_CARD)
                        .setIndex(index-1)
                        .serialize().toString()
        );
        setInteractionComplete(true);
        return "";
    }
}
