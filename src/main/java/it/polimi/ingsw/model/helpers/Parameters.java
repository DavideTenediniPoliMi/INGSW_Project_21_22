package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Class used for generic parameter setting into different Character cards.
 */
public class Parameters {
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
    public Parameters() {
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
    public void setFromOrigin(StudentGroup fromOrigin){
        this.fromOrigin = fromOrigin;
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
    public void setFromDestination(StudentGroup fromDestination){
        this.fromDestination = fromDestination;
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
    public void setBoostedTeam(TowerColor boostedTeam) {
        this.boostedTeam = boostedTeam;
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
    public void setCurrentTeam(TowerColor currentTeam) {
        this.currentTeam = currentTeam;
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
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
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
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
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
    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }
}
