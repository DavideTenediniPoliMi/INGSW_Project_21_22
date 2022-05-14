package it.polimi.ingsw.view.views;

import java.util.ArrayList;
import java.util.List;

public class NoLobbyView extends LobbyView {
    private int lobbySize;
    private boolean expertMode;
    private boolean creating;
    private final List<Integer> validNumbers = new ArrayList<>(List.of(2,3,4));

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

        if(input.equals("Y") || input.equals("y")) {
            expertMode = true;
            return "";
        }

        if(input.equals("N") || input.equals("n")) {
            expertMode = false;
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
