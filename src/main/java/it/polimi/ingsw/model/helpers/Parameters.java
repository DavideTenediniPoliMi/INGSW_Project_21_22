package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class Parameters {
    private final StudentGroup fromOrigin = new StudentGroup();
    private final StudentGroup fromDestination = new StudentGroup();
    private TowerColor teamColor;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int playerIndex;
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

    public TowerColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TowerColor teamColor) {
        this.teamColor = teamColor;
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

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public void setIslandIndex(int islandIndex) {
        this.islandIndex = islandIndex;
    }
}
