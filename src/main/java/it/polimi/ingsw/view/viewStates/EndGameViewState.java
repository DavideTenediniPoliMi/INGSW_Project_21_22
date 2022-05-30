package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;

public class EndGameViewState extends GameViewState {
    public EndGameViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public void printCLIPrompt(boolean shouldPrint) {
        MatchInfo matchInfo = MatchInfo.getInstance();

        String message = "The game is OVER and ";

        if(matchInfo.isGameTied()) {
            message += "it ended in a TIE between the following players :";
        } else {
            message += "it was WON by :";
        }

        appendBuffer(message);

        for(Player player: Game.getInstance().getPlayers()) {
            if(MatchInfo.getInstance().getWinners().contains(player.getTeamColor())) {
                appendBuffer(player.getName() + " (TEAM " + player.getTeamColor().print() + ")");
            }
        }

        appendBuffer("\n");
        appendBuffer("Press ENTER to go back to the starting screen!");
    }

    @Override
    public String manageCLIInput(String input) {
        setInteractionComplete(true);
        return "";
    }

    @Override
    public void resetInteraction() {
        super.resetInteraction();
    }
}
