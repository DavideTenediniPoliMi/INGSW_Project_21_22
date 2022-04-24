package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.helpers.StudentBag;

import java.util.List;

public class ResponseParameters {
    private List<School> schools;
    private List<CharacterCard> characterCards;
    private List<Cloud> clouds;
    private List<Island> islands;
    private StudentBag bag;
    private ProfessorTracker professors;
    private Player player;
    private int coinsLeft;
    private boolean sendCards;

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public void addSchool(School school) {
        schools.add(school);
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }

    public StudentBag getBag() {
        return bag;
    }

    public void setBag(StudentBag bag) {
        this.bag = bag;
    }

    public ProfessorTracker getProfessors() {
        return professors;
    }

    public void setProfessors(ProfessorTracker professors) {
        this.professors = professors;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCoinsLeft() {
        return coinsLeft;
    }

    public void setCoinsLeft(int coinsLeft) {
        this.coinsLeft = coinsLeft;
    }

    public boolean shouldSendCards() {
        return sendCards;
    }

    public void setSendCards(boolean sendCards) {
        this.sendCards = sendCards;
    }
}
