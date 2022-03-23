package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class Parameters {
    private final StudentGroup fromOrigin = new StudentGroup();
    private final StudentGroup fromDestination = new StudentGroup();
    private TowerColor boostedTeam;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int playerID;
    private int islandIndex;

    public Parameters() {
    }

    public StudentGroup getFromOrigin() {
        return fromOrigin;
    }

    public void setFromOrigin(StudentGroup fromOrigin){
        fromOrigin.transferAllTo(this.fromOrigin);
    }

    public StudentGroup getFromDestination() {
        return fromDestination;
    }

    public void setFromDestination(StudentGroup fromDestination){
        fromDestination.transferAllTo(this.fromDestination);
    }

    public TowerColor getBoostedTeam() {
        return boostedTeam;
    }

    public void setBoostedTeam(TowerColor boostedTeam) {
        this.boostedTeam = boostedTeam;
    }

    public TowerColor getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(TowerColor currentTeam) {
        this.currentTeam = currentTeam;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }
}
