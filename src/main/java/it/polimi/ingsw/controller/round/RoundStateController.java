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

/**
 * Default class to handle any action during any game phase.
 */
public class RoundStateController {
    protected CharacterCardController characterCardController = new CharacterCardController();
    protected IslandController islandController;
    protected DiningRoomController diningRoomController;

    /**
     * Default constructor for <code>RoundStateController</code>, using the specified instances for <code>IslandController</code>
     * and <code>DiningRoomController</code>.
     *
     * @param islandController the <code>IslandController</code>.
     * @param diningRoomController the <code>DiningRoomController</code>
     */
    public RoundStateController(IslandController islandController, DiningRoomController diningRoomController) {
        this.islandController = islandController;
        this.diningRoomController = diningRoomController;
    }

    /**
     * Constructor called when moving to the specified new <code>TurnState</code>.
     *
     * @param oldState the old <code>RoundStateController</code> instance.
     * @param stateType the <code>TurnState</code> to transition to.
     */
    public RoundStateController(RoundStateController oldState, TurnState stateType) {
        this.characterCardController = oldState.characterCardController;
        this.islandController = oldState.islandController;
        this.diningRoomController = oldState.diningRoomController;
    }

    /**
     * Defines the first play order in the match.
     */
    public void defineFirstPlayOrder() {
        Random r = new Random();
        int startingIndex = r.nextInt(MatchInfo.getInstance().getSelectedNumPlayer());

        defineClockWiseOrder(startingIndex);
    }

    /**
     * Clears any active <code>CharacterCard</code>'s effect.
     * @see CharacterCardPlayableStateController
     */
    public void clearEffects() {
        characterCardController.clearEffects();
    }

    /**
     * Defines this round's new order of play.
     *
     * @throws IllegalActionException If called during the wrong state.
     */
    public void definePlayOrder() throws IllegalActionException {
        throw new IllegalActionException("definePlayOrder", MatchInfo.getInstance().getStateType());
    }

    /**
     * Plays the specified assistant card for the current <code>Player</code>.
     *
     * @param cardIndex the index of the assistant card to play.
     * @throws IllegalActionException If called during the wrong state.
     * @throws CardUsedException If the specified <code>Card</code> was already used by another <code>Player</code>.
     * @see PlanningStateController
     */
    public void playCard(int cardIndex) throws IllegalActionException, CardUsedException {
        throw new IllegalActionException("playCard", MatchInfo.getInstance().getStateType());
    }

    /**
     * Buys the specified <code>CharacterCard</code>. for the current <code>Player</code>.
     *
     * @param cardIndex the index of the <code>CharacterCard</code> to buy.
     * @throws IllegalActionException If called during the wrong state.
     * @see CharacterCardPlayableStateController
     */
    public void buyCharacterCard(int cardIndex) throws IllegalActionException {
        throw new IllegalActionException("buyCharacterCard", MatchInfo.getInstance().getStateType());
    }

    /**
     * Sets the specified <code>Parameters</code> for the currently active <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to set.
     * @throws IllegalActionException If called during the wrong state.
     * @see CharacterCardPlayableStateController
     */
    public void setCardParameters(Parameters params) throws IllegalActionException {
        throw new IllegalActionException("setCardParameters", MatchInfo.getInstance().getStateType());
    }

    /**
     * Activates the effect of the active <code>CharacterCard</code>.
     *
     * @throws IllegalActionException If called during the wrong state.
     * @see CharacterCardPlayableStateController
     */
    public void activateCard() throws IllegalActionException {
        throw new IllegalActionException("activateCard", MatchInfo.getInstance().getStateType());
    }

    /**
     * Transfers a student of the specified <code>Color</code> from the current <code>Player</code>'s entrance to the
     * specified <code>Island</code>.
     *
     * @param islandIndex the index of the recipient <code>Island</code>.
     * @param c the <code>Color</code> of the student to transfer.
     * @throws IllegalActionException If called during the wrong state.
     * @throws StudentTransferException If the current <code>Player</code> already moved the maximum amount of students.
     * @throws NotEnoughStudentsException If the current <code>Player</code> does not have enough students of the
     * specified <code>Color</code>.
     * @see StudentsStateController
     */
    public void transferStudentToIsland(int islandIndex, Color c) throws IllegalActionException, StudentTransferException, NotEnoughStudentsException {
        throw new IllegalActionException("transferStudentToIsland", MatchInfo.getInstance().getStateType());
    }

    /**
     * Transfers a student of the specified <code>Color</code> from the current <code>Player</code>'s entrance to its
     * dining room.
     *
     * @param c the <code>Color</code> of the student to transfer.
     * @throws IllegalActionException If called during the wrong state.
     * @throws StudentTransferException If the current <code>Player</code> already moved the maximum amount of students.
     * @throws NotEnoughStudentsException If the current <code>Player</code> does not have enough students of the
     * specified <code>Color</code>.
     * @see StudentsStateController
     */
    public void transferStudentToDiningRoom(Color c) throws IllegalActionException, StudentTransferException, NotEnoughStudentsException {
        throw new IllegalActionException("transferStudentToDiningRoom", MatchInfo.getInstance().getStateType());
    }

    /**
     * Moves Mother Nature the specified amount of steps.
     *
     * @param steps the amount of steps to move.
     * @throws IllegalActionException If called during the wrong state.
     * @throws MNOutOfRangeException If the amount of steps specified exceeds the current <code>Player</code> assistant
     * card's range.
     * @see MNStateController
     */
    public void moveMN(int steps) throws IllegalActionException, MNOutOfRangeException {
        throw new IllegalActionException("moveMN", MatchInfo.getInstance().getStateType());
    }

    /**
     * Collects the students from the specified <code>Cloud</code> for the current <code>Player</code>.
     *
     * @param cloudIndex the index of the <code>Cloud</code>.
     * @throws IllegalActionException If called during the wrong state.
     * @throws CloudUnavailableException If another <code>Player</code> already collected the students from the
     * specified <code>Cloud</code>.
     * @see CloudStateController
     */
    public void collectFromCloud(int cloudIndex) throws IllegalActionException, CloudUnavailableException {
        throw new IllegalActionException("collectFromCloud", MatchInfo.getInstance().getStateType());
    }

    /**
     * Defines the new order of play as clockwise, starting from the specified index.
     *
     * @param startingIndex the index of the first <code>Player</code> to play.
     */
    protected void defineClockWiseOrder(int startingIndex) {
        List<Player> players = Game.getInstance().getPlayers();

        Collections.rotate(players, startingIndex * -1); // times -1 so that it rotates to the left

        for(Player p: players) {
            MatchInfo.getInstance().addPlayer(p.getID());
        }
    }
}
