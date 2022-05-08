package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.model.MatchInfo;

public class SkipTurnCommand implements Command {
    int playerID;
    MatchInfo match;

    public SkipTurnCommand(int playerID) {
        this.playerID = playerID;
        match = MatchInfo.getInstance();
    }

    @Override
    public void execute() throws EriantysException, EriantysRuntimeException {
        while(match.getCurrentPlayerID() == playerID) {
            skipTurn();
        }
    }

    private void skipTurn() { //TODO not best approach
        switch (match.getStateType()) {
            case STUDENTS:
                while(match.getNumMovedStudents() < match.getMaxMovableStudents()) {
                    match.studentWasMoved();
                }
                break;
            case PLANNING:
                break;
            case MOTHER_NATURE:
                break;
            case CLOUD:
                break;
            default:
                break;
        }
    }
}
