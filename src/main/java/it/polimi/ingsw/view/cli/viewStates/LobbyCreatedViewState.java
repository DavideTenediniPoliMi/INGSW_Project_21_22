package it.polimi.ingsw.view.cli.viewStates;

import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.view.cli.AnsiCodes;

/**
 * Class that manages the view of the lobby once it is created
 */
public class LobbyCreatedViewState extends LobbyViewState {
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
        MatchInfo matchInfo = MatchInfo.getInstance();
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
