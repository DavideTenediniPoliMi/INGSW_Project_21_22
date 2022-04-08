package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;

import java.util.LinkedList;
import java.util.Queue;

public class RoundStateController {
    protected CharacterCardController characterCardController = new CharacterCardController();
    protected IslandController islandController;
    protected DiningRoomController diningRoomController;

    public RoundStateController(IslandController islandController, DiningRoomController diningRoomController) {
        this.islandController = islandController;
        this.diningRoomController = diningRoomController;
    }

    public RoundStateController(RoundStateController oldState, TurnState stateType) {
        this.characterCardController = oldState.characterCardController;
        this.islandController = oldState.islandController;
        this.diningRoomController = oldState.diningRoomController;
    }

    public void init() { // TODO change name
        // TODO decide first player order
    }

    public void definePlayOrder() throws IllegalActionException {
        throw new IllegalActionException("definePlayOrder", MatchInfo.getInstance().getStateType());
    }

    public void playCard(int cardIndex) throws IllegalActionException {
        throw new IllegalActionException("playCard", MatchInfo.getInstance().getStateType());
    }

    public void buyCharacterCard(int cardIndex) throws IllegalActionException {
        throw new IllegalActionException("buyCharacterCard", MatchInfo.getInstance().getStateType());
    }

    public void setCardParameters(Parameters params) throws IllegalActionException {
        throw new IllegalActionException("setCardParameters", MatchInfo.getInstance().getStateType());
    }

    public void activateCard() throws IllegalActionException {
        throw new IllegalActionException("activateCard", MatchInfo.getInstance().getStateType());
    }

    public void transferStudentToIsland(int islandIndex, Color c) throws IllegalActionException {
        throw new IllegalActionException("transferStudentToIsland", MatchInfo.getInstance().getStateType());
    }

    public void transferStudentToDiningRoom(Color c) throws IllegalActionException {
        throw new IllegalActionException("transferStudentToDiningRoom", MatchInfo.getInstance().getStateType());
    }
    public void moveMN(int steps) throws IllegalActionException {
        throw new IllegalActionException("moveMN", MatchInfo.getInstance().getStateType());
    }

    public void collectFromCloud(int cloudIndex) throws IllegalActionException {
        throw new IllegalActionException("collectFromCloud", MatchInfo.getInstance().getStateType());
    }
}
