package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TurnState;

import java.util.LinkedList;
import java.util.Queue;

public class MatchInfo {
    private static MatchInfo instance;
    private int selectedNumPlayer;
    private boolean expertMode;
    private int numPlayersConnected;
    private TurnState stateType;
    private final Queue<Integer> playOrder;
    private int numMovedStudents;

    private MatchInfo() {
        playOrder = new LinkedList<>();
    }

    public static MatchInfo getInstance() {
        if(instance == null) {
            instance = new MatchInfo();
        }

        return instance;
    }

    public int getSelectedNumPlayer() {
        return selectedNumPlayer;
    }

    public void setSelectedNumPlayer(int selectedNumPlayer) {
        this.selectedNumPlayer = selectedNumPlayer;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    public void setNumPlayersConnected(int numPlayersConnected) {
        this.numPlayersConnected = numPlayersConnected;
    }

    public int getMaxMovableStudents() { return (selectedNumPlayer % 2 == 0) ? 3 : 4 ;}

    public int getMaxTowers() { return (selectedNumPlayer % 2 == 0) ? 8 : 6 ;}

    public TurnState getStateType() {
        return stateType;
    }

    public void setStateType(TurnState stateType) {
        this.stateType = stateType;
    }

    public Queue<Integer> getPlayOrder() {
        return new LinkedList<>(playOrder);
    }

    public void removePlayer() {
        playOrder.remove();
    }

    public void addPlayer(int playerID) {
        playOrder.add(playerID);
    }

    public int getNumMovedStudents() {
        return numMovedStudents;
    }

    public void resetNumMovedStudents() {
        numMovedStudents = 0;
    }

    public void studentWasMoved() {
        numMovedStudents++;
    }

    public int getNumPlayersStillToAct() {
        return playOrder.size();
    }

    public int getCurrentPlayerID() {
        return (playOrder.peek() != null) ? playOrder.peek() : -1;
    }
}
