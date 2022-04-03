package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.Parameters;

import java.util.Queue;

public class RoundStateController {
    protected Queue<Integer> playOrder;
    protected CharacterCardController characterCardController = new CharacterCardController();
    protected IslandController islandController;
    protected DiningRoomController diningRoomController;
    protected int numMovedStudents;

    public RoundStateController(IslandController islandController, DiningRoomController diningRoomController) {
        this.islandController = islandController;
        this.diningRoomController = diningRoomController;
    }

    public RoundStateController(RoundStateController oldState) {
        this.playOrder = oldState.playOrder;
        this.characterCardController = oldState.characterCardController;
        this.islandController = oldState.islandController;
        this.diningRoomController = oldState.diningRoomController;
        this.numMovedStudents = oldState.numMovedStudents;
    }

    public void init() { // TODO change name
        // TODO decide first player order
    }

    public void definePlayOrder() {
        // TODO
    }

    public int getCurrentPlayerID() {
        return (playOrder.peek() != null) ? playOrder.peek() : -1;
    }

    public void playCard(int cardIndex) {
        // EXCEPTION
    }

    public void buyCharacterCard(int cardIndex) {
        // EXCEPTION
    }

    public void setCardParameters(Parameters params) {
        // EXCEPTION
    }

    public void activateCard() {
        // EXCEPTION
    }

    public void transferStudentToIsland(int islandIndex, Color c) {
        // EXCEPTION
    }

    public void transferStudentToDiningRoom(int playerID, Color c) {
        // EXCEPTION
    }
    public void moveMN() {
        // EXCEPTION
    }

    public void collectFromClouds() {
        // EXCEPTION
    }
}
