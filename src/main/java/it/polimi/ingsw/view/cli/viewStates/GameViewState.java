package it.polimi.ingsw.view.cli.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.StringUtils;

public class GameViewState extends ViewState {
    public GameViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return StringUtils.listToString(Game.getInstance().print());
    }

    @Override
    public void printCLIPrompt(boolean shouldPrint) {
        if(!"".equals(getBuffer())) return;

        int activePlayerID = MatchInfo.getInstance().getCurrentPlayerID();
        Player activePlayer = Game.getInstance().getPlayerByID(activePlayerID);
        if(MatchInfo.getInstance().isGamePaused()) {
            appendBuffer("Game is currently paused!");
        } else {
            appendBuffer("It's " + activePlayer.getName() + "'s turn!");
        }
    }

    @Override
    public String manageCLIInput(String input) {
        return null;
    }
}
