package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;

import java.util.LinkedList;
import java.util.Queue;

public class RoundStateController {
    protected TurnState stateType;
    protected Queue<Integer> playOrder;
    protected CharacterCardController characterCardController = new CharacterCardController();
    protected IslandController islandController;
    protected DiningRoomController diningRoomController;
    protected int numMovedStudents;

    public RoundStateController(IslandController islandController, DiningRoomController diningRoomController) {
        this.islandController = islandController;
        this.diningRoomController = diningRoomController;
        playOrder = new LinkedList<>();
    }

    public RoundStateController(RoundStateController oldState, TurnState stateType) {
        this.playOrder = oldState.playOrder;
        this.characterCardController = oldState.characterCardController;
        this.islandController = oldState.islandController;
        this.diningRoomController = oldState.diningRoomController;
        this.numMovedStudents = oldState.numMovedStudents;
        this.stateType = stateType;
    }

    public TurnState getStateType() {
        return stateType;
    }

    public int getNumPlayersStillToAct() {
        return playOrder.size();
    }

    public int getNumMovedStudents() {
        return numMovedStudents;
    }


    public void init() { // TODO change name
        // TODO decide first player order
    }

    public int getCurrentPlayerID() {
        return (playOrder.peek() != null) ? playOrder.peek() : -1;
    }

    public void definePlayOrder() throws IllegalActionException {
        throw new IllegalActionException("definePlayOrder", stateType);
    }

    public void playCard(int cardIndex) throws IllegalActionException {
        throw new IllegalActionException("playCard", stateType);
    }

    public void buyCharacterCard(int cardIndex) throws IllegalActionException {
        throw new IllegalActionException("buyCharacterCard", stateType);
    }

    public void setCardParameters(Parameters params) throws IllegalActionException {
        throw new IllegalActionException("setCardParameters", stateType);
    }

    public void activateCard() throws IllegalActionException {
        throw new IllegalActionException("activateCard", stateType);
    }

    public void transferStudentToIsland(int islandIndex, Color c) throws IllegalActionException {
        throw new IllegalActionException("transferStudentToIsland", stateType);
    }

    public void transferStudentToDiningRoom(Color c) throws IllegalActionException {
        throw new IllegalActionException("transferStudentToDiningRoom", stateType);
    }
    public void moveMN(int steps) throws IllegalActionException {
        throw new IllegalActionException("moveMN", stateType);
    }

    public void collectFromCloud(int cloudIndex) throws IllegalActionException {
        throw new IllegalActionException("collectFromCloud", stateType);
    }
}
