package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.ArrayList;
import java.util.List;

public class LobbyCreatedViewState extends LobbyViewState {
    private final List<String> validCharacters = new ArrayList<>(List.of("Y", "y", "N", "n"));
    private MatchInfo matchInfo;
    private boolean printed;
    public LobbyCreatedViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return "";
    }

    @Override
    public void printCLIPrompt(boolean shouldPrint) {
        setInteractionComplete(false);
        matchInfo = MatchInfo.getInstance();
        appendBuffer("Another lobby was created [Players: "
                + "1/"+ matchInfo.getSelectedNumPlayer() + ", Expert: "
                + (matchInfo.isExpertMode() ?
                AnsiCodes.GREEN_BACKGROUND_BRIGHT + " V " : AnsiCodes.RED_BACKGROUND_BRIGHT + " X ")
                + AnsiCodes.RESET + " ]");
        appendBuffer("Press ENTER to join.");
    }

    @Override
    public String manageCLIInput(String input) {
        appendBuffer(input);
        String error = "";

        if (!input.equals("")) {
            error = "I said ENTER, but whatever let's keep going!";
            appendBuffer(error);
        }
        setInteractionComplete(true);
        return error;
    }
}
