package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class Parameters {
    private final StudentGroup origin = new StudentGroup();
    private final StudentGroup destination = new StudentGroup();
    private TowerColor teamColor;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int playerIndex;
    private int islandIndex;

    public Parameters() {
    }

    public StudentGroup getOrigin() {
        return origin;
    }

    public void setOrigin(StudentGroup origin){
        origin.transferAllTo(this.origin);
    }

    public StudentGroup getDestination() {
        return destination;
    }

    public void setDestination(){
        destination.transferAllTo(this.destination);
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
