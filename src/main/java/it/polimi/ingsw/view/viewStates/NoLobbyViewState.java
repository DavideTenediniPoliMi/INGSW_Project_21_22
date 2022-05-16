package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NoLobbyViewState extends LobbyViewState {
    public NoLobbyViewState(ViewState oldViewState) {
        super(oldViewState);
    }
    private boolean creating;
    private int lobbySize;
    private boolean expertMode;
    private final List<Integer> validNumbers = new ArrayList<>(List.of(2,3,4));
    private final List<String> validCharacters = new ArrayList<>(List.of("Y", "y", "N", "n"));

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        if(!creating) {
            appendBuffer("Press ENTER to start creating a lobby!");
            return;
        }

        if(lobbySize == 0) {
            appendBuffer("What do you want the lobby size to be ? [2,3,4]");
            return;
        }

        appendBuffer("Do you want the game to be in expert mode? [Y/N]");
    }

    @Override
    public String manageCLIInput(String input) {
        appendBuffer(input);
        String error;

        if(!creating) {
            creating = true;
            if(!input.equals("")) {
                error = "I said ENTER, but whatever let's keep going!";
                appendBuffer(error);
                return error;
            }
            return "";
        }

        if(lobbySize == 0) {
            error = StringUtils.checkInteger(input, validNumbers);

            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }

            lobbySize = Integer.parseInt(input);
            return "";
        }

        if(validCharacters.contains(input)) {
            if(input.equalsIgnoreCase("Y"))
                expertMode = true;

            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.CREATE_LOBBY)
                            .setExpertMode(expertMode)
                            .setSelectedNumPlayer(lobbySize)
                            .serialize().toString()
            );

            setInteractionComplete(true);
            return "";
        }

        return "That was not a valid choice! Try again!";
    }
}
