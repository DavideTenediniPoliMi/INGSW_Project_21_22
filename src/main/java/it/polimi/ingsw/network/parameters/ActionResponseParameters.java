package it.polimi.ingsw.network.parameters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a response from the server after any action has been played during a game.
 */
public class ActionResponseParameters implements Serializable {
    private List<School> schools = new ArrayList<>();
    private List<CharacterCard> characterCards;
    private List<Cloud> clouds;
    private List<Island> islands;
    private boolean bagEmpty;
    private ProfessorTracker professors;
    private Player player;
    private int coinsLeft;
    private boolean sendCards;
    private boolean sendMatchInfo;

    /**
     * Gets the list of schools.
     *
     * @return the list of schools of this <code>ActionResponseParameters</code>.
     */
    public List<School> getSchools() {
        return schools;
    }

    /**
     * Sets the specified list of schools and returns this instance.
     *
     * @param schools the list of schools of this message.
     * @return this <code>ActionResponseParameters</code>
     */
    public ActionResponseParameters setSchools(List<School> schools) {
        this.schools = schools;
        return this;
    }

    /**
     * Sets the specified <code>School</code> and returns this instance.
     *
     * @param school the <code>School</code> of this message.
     * @return this <code>ActionResponseParameters</code>
     */
    public ActionResponseParameters addSchool(School school) {
        schools.add(school);
        return this;
    }

    /**
     * Gets the list of Character cards.
     *
     * @return the list of Character cards of this <code>ActionResponseParameters</code>.
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Sets the specified list of Character cards and returns this instance.
     *
     * @param characterCards the list of Character cards of this message.
     * @return this <code>ActionResponseParameters</code>
     */
    public ActionResponseParameters setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
        return this;
    }

    /**
     * Gets the list of clouds.
     *
     * @return the list of clouds of this <code>ActionResponseParameters</code>.
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Sets the specified list of clouds and returns this instance.
     *
     * @param clouds the list of clouds of this message.
     * @return this <code>ActionResponseParameters</code>
     */
    public ActionResponseParameters setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
        return this;
    }

    /**
     * Gets the list of schools.
     *
     * @return the list of schools of this <code>ActionResponseParameters</code>.
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * Sets the specified list of islands and returns this instance.
     *
     * @param islands the list of islands of this message.
     * @return this <code>ActionResponseParameters</code>
     */
    public ActionResponseParameters setIslands(List<Island> islands) {
        this.islands = islands;
        return this;
    }

    /**
     * Gets the flag specifying if the <code>StudentBag</code> is empty.
     *
     * @return if the <code>StudentBag</code> is set as empty in this <code>ActionResponseParameters</code>.
     */
    public boolean isBagEmpty() {
        return bagEmpty;
    }

    /**
     * Sets if the <code>StudentBag</code> is empty and returns this instance.
     *
     * @param bagEmpty the flag regarding the <code>StudentBag</code>.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setBagEmpty(boolean bagEmpty) {
        this.bagEmpty = bagEmpty;
        return this;
    }

    /**
     * Gets the <code>ProfessorTracker</code>.
     *
     * @return the <code>ProfessorTracker</code> of this <code>ActionResponseParameters</code>.
     */
    public ProfessorTracker getProfessors() {
        return professors;
    }

    /**
     * Sets the specified <code>ProfessorTracker</code> and returns this instance.
     *
     * @param professors the <code>ProfessorTracker</code> to set.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setProfessors(ProfessorTracker professors) {
        this.professors = professors;
        return this;
    }

    /**
     * Gets the <code>Player</code>.
     *
     * @return the <code>Player</code> of this <code>ActionResponseParameters</code>.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the specified <code>Player</code> and returns this instance.
     *
     * @param player the <code>Player</code> to set.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the amount of coins left in the <code>Board</code>.
     *
     * @return the amount of coins set in this <code>ActionResponseParameters</code>.
     */
    public int getCoinsLeft() {
        return coinsLeft;
    }

    /**
     * Sets the specified amount of coins left in the <code>Board</code> and returns this instance.
     *
     * @param coinsLeft the amount of coins.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setCoinsLeft(int coinsLeft) {
        this.coinsLeft = coinsLeft;
        return this;
    }

    /**
     * Gets the flag specifying if this message should send information about the assistant cards.
     *
     * @return whether this <code>ActionResponseParameters</code> should send information about the assistant cards.
     */
    public boolean shouldSendCards() {
        return sendCards;
    }

    /**
     * Sets whether this message should send information about the assistant cards and returns this instance.
     *
     * @param sendCards the flag specifying if the VirtualView should send info about Cards.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setSendCards(boolean sendCards) {
        this.sendCards = sendCards;
        return this;
    }

    /**
     * Gets the flag specifying if this message should send information about the Match.
     *
     * @return whether this <code>ActionResponseParameters</code> should send information about the Match.
     */
    public boolean shouldSendMatchInfo() {
        return sendMatchInfo;
    }

    /**
     * Sets whether this message should send information about this Match and returns this instance.
     *
     * @param sendMatchInfo the flag specifying if the VirtualView should send info about this Match.
     * @return this <code>ActionResponseParameters</code>.
     */
    public ActionResponseParameters setSendMatchInfo(boolean sendMatchInfo) {
        this.sendMatchInfo = sendMatchInfo;
        return this;
    }

    @Override
    public JsonObject serialize() {
        return null;
    }

    @Override
    public void deserialize(String json) {

    }
}
