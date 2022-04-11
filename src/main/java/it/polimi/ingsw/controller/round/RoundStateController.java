package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.board.CloudUnavailableException;
import it.polimi.ingsw.exceptions.board.MNOutOfRangeException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.player.CardUsedException;
import it.polimi.ingsw.exceptions.students.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.students.StudentTransferException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;

import java.util.*;

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

    public void defineFirstPlayOrder() {
        Random r = new Random();
        int startingIndex = r.nextInt(MatchInfo.getInstance().getSelectedNumPlayer());

        defineClockWiseOrder(startingIndex);
    }

    public void clearEffects() {
        characterCardController.clearEffects();
    }

    public void definePlayOrder() throws IllegalActionException {
        throw new IllegalActionException("definePlayOrder", MatchInfo.getInstance().getStateType());
    }

    public void playCard(int cardIndex) throws IllegalActionException, CardUsedException {
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

    public void transferStudentToIsland(int islandIndex, Color c) throws IllegalActionException, StudentTransferException, NotEnoughStudentsException {
        throw new IllegalActionException("transferStudentToIsland", MatchInfo.getInstance().getStateType());
    }

    public void transferStudentToDiningRoom(Color c) throws IllegalActionException, StudentTransferException, NotEnoughStudentsException {
        throw new IllegalActionException("transferStudentToDiningRoom", MatchInfo.getInstance().getStateType());
    }
    public void moveMN(int steps) throws IllegalActionException, MNOutOfRangeException {
        throw new IllegalActionException("moveMN", MatchInfo.getInstance().getStateType());
    }

    public void collectFromCloud(int cloudIndex) throws IllegalActionException, CloudUnavailableException {
        throw new IllegalActionException("collectFromCloud", MatchInfo.getInstance().getStateType());
    }

    protected void defineClockWiseOrder(int startingIndex) {
        List<Player> players = Game.getInstance().getPlayers();

        Collections.rotate(players, startingIndex * -1); // times -1 so that it rotates to the left

        for(Player p: players) {
            MatchInfo.getInstance().addPlayer(p.getID());
        }
    }
}
