package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Class used for generic parameter setting into different Character cards.
 */
public class CardParameters {
    private StudentGroup fromOrigin ;
    private StudentGroup fromDestination;
    private TowerColor boostedTeam;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int playerID;
    private int islandIndex;

    /**
     * Sole constructor
     */
    public CardParameters() {
    }

    /**
     * Returns the origin <code>StudentGroup</code>
     *
     * @return Origin <code>StudentGroup</code> for a transfer/swap
     */
    public StudentGroup getFromOrigin() {
        return fromOrigin;
    }

    /**
     * Sets the origin <code>StudentGroup</code> for a transfer/swap.
     *
     * @param fromOrigin the origin <code>StudentGroup</code>
     */
    public CardParameters setFromOrigin(StudentGroup fromOrigin){
        this.fromOrigin = fromOrigin;
        return this;
    }

    /**
     * Returns the destination <code>StudentGroup</code>
     *
     * @return Destination <code>StudentGroup</code> for a transfer/swap
     */
    public StudentGroup getFromDestination() {
        return fromDestination;
    }

    /**
     * Sets the destination <code>StudentGroup</code> for a transfer/swap.
     *
     * @param fromDestination the destination <code>StudentGroup</code>
     */
    public CardParameters setFromDestination(StudentGroup fromDestination){
        this.fromDestination = fromDestination;
        return this;
    }

    /**
     * Gets the team color to be boosted by the "+2" card.
     *
     * @return Boosted <code>TowerColor</code>.
     */
    public TowerColor getBoostedTeam() {
        return boostedTeam;
    }

    /**
     * Sets the team color to be boosted by the "+2" card.
     *
     * @param boostedTeam the team to boost.
     */
    public CardParameters setBoostedTeam(TowerColor boostedTeam) {
        this.boostedTeam = boostedTeam;
        return this;
    }

    /**
     * Gets the current team for influence calc.
     *
     * @return <code>TowerColor</code> for current team
     */
    public TowerColor getCurrentTeam() {
        return currentTeam;
    }

    /**
     * Sets the current team during influence calc.
     *
     * @param currentTeam the new current team
     */
    public CardParameters setCurrentTeam(TowerColor currentTeam) {
        this.currentTeam = currentTeam;
        return this;
    }

    /**
     * Gets selected color for "IgnoreColor" card
     *
     * @return Selected <code>Color</code>
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * Sets selected color for "IgnoreColor" card.
     *
     * @param selectedColor the selected <code>Color</code> for the card.
     */
    public CardParameters setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    /**
     * Gets ID of the <code>Player</code> participating in students swap/transfer.
     *
     * @return Player ID.
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Sets ID of the <code>Player</code> participating in students swap/transfer.
     *
     * @param playerID the ID of the player.
     */
    public CardParameters setPlayerID(int playerID) {
        this.playerID = playerID;
        return this;
    }

    /**
     * Gets the index of the <code>Island</code> participating in students swap/transfer.
     *
     * @return Index of the <code>Island</code>.
     */
    public int getIslandIndex() {
        return islandIndex;
    }

    /**
     * Sets the index of the <code>Island</code> participating in students swap/transfer.
     *
     * @param islandIndex the index of the <code>Island</code>
     */
    public CardParameters setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
        return this;
    }
}