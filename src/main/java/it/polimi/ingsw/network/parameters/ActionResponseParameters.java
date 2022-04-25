package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.characters.CharacterCard;

import java.util.ArrayList;
import java.util.List;

public class ActionResponseParameters {
    private List<School> schools = new ArrayList<>();
    private List<CharacterCard> characterCards;
    private List<Cloud> clouds;
    private List<Island> islands;
    private boolean bagEmpty;
    private ProfessorTracker professors;
    private Player player;
    private int coinsLeft;
    private boolean sendCards;

    public List<School> getSchools() {
        return schools;
    }

    public ActionResponseParameters setSchools(List<School> schools) {
        this.schools = schools;
        return this;
    }

    public ActionResponseParameters addSchool(School school) {
        schools.add(school);
        return this;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public ActionResponseParameters setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
        return this;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public ActionResponseParameters setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
        return this;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public ActionResponseParameters setIslands(List<Island> islands) {
        this.islands = islands;
        return this;
    }

    public boolean isBagEmpty() {
        return bagEmpty;
    }

    public ActionResponseParameters setBagEmpty(boolean bagEmpty) {
        this.bagEmpty = bagEmpty;
        return this;
    }

    public ProfessorTracker getProfessors() {
        return professors;
    }

    public ActionResponseParameters setProfessors(ProfessorTracker professors) {
        this.professors = professors;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public ActionResponseParameters setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public int getCoinsLeft() {
        return coinsLeft;
    }

    public ActionResponseParameters setCoinsLeft(int coinsLeft) {
        this.coinsLeft = coinsLeft;
        return this;
    }

    public boolean shouldSendCards() {
        return sendCards;
    }

    public ActionResponseParameters setSendCards(boolean sendCards) {
        this.sendCards = sendCards;
        return this;
    }
}
