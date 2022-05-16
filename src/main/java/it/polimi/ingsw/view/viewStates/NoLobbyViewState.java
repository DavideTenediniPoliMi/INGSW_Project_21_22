package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;

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
    public String printCLIPrompt() {
        if(!creating) {
            return "Press ENTER to start creating a lobby!";
        }

        if(lobbySize == 0) {
            return "What do you want the lobby size to be ? [2,3,4]";
        }

        return "Do you want the game to be in expert mode? [Y/N]";
    }

    @Override
    public String manageCLIInput(String input) {
        if(!creating) {
            creating = true;
            if(!input.equals("")) {
                return "I said ENTER, but whatever let's keep going!";
            }
            return "";
        }

        if(lobbySize == 0) {
            String error = checkInteger(input, validNumbers);

            if(!error.equals("")) {
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

    public String checkInteger(String input, List<Integer> validInputs) {
        try {
            int temp = Integer.parseInt(input);
            if(!validInputs.contains(temp)) {
                return "That is a number, but not a valid one! Try again!";
            }
            return "";
        } catch(NumberFormatException e) {
            return "That is not a number! Try again!";
        }
    }
}
