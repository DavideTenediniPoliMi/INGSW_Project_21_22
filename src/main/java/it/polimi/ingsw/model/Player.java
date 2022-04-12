package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding every information about a Player
 */
public class Player {
    private final int ID;
    private final String name;
    private final TowerColor teamColor;
    private final CardBack cardBack;
    private final boolean towerHolder;
    private final List<Card> playableCards = new ArrayList<>();
    private Card selectedCard;
    private int numCoins;

    /**
     * Sole constructor to instantiate a new <code>Player</code>
     * @param ID the ID of the player
     * @param name the name of the player
     * @param teamColor the team of the player
     * @param cardBack the card back chosen by the player
     * @param towerHolder the flag to identify if a player has towers in its school
     */
    public Player(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder) {
        this.ID = ID;
        this.name = name;
        this.teamColor = teamColor;
        this.cardBack = cardBack;
        this.towerHolder = towerHolder;

        this.playableCards.addAll(List.of(Card.values()));
    }

    /**
     * Returns the ID of this <code>Player</code>
     * @return ID of this Player
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the name of this <code>Player</code>
     * @return name of this Player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the team of this <code>Player</code>
     * @return team of this Player
     */
    public TowerColor getTeamColor() {
        return teamColor;
    }

    /**
     * Returns the <code>CardBack</code> picked by this <code>Player</code>
     * @return CardBack of this Player
     */
    public CardBack getCardBack() {
        return cardBack;
    }

    /**
     * Returns whether this <code>Player</code> holds towers in its <code>School</code>
     * @return <code>true</code> if this Player holds towers for its team
     */
    public boolean isTowerHolder() {
        return towerHolder;
    }

    /**
     * Returns the assistant cards this <code>Player</code> can still use.
     * @return list of cards available for use.
     */
    public List<Card> getPlayableCards(){
        return new ArrayList<>(playableCards);
    }

    /**
     * Returns the assistant card selected by this <code>Player</code>.
     * @return Assistant card selected for this turn.
     */
    public Card getSelectedCard() {
        return selectedCard;
    }

    /**
     * Sets the selected card to the one specified.
     * @param card the assistant card picked.
     */
    protected void setSelectedCard(Card card) {
        selectedCard = card;
        playableCards.remove(card);
    }

    /**
     * Returns the amount of coins of this <code>Player</code>. 0 if game is not in expert mode
     * @return Amount of coins
     */
    public int getNumCoins() {
        return numCoins;
    }

    /**
     * Adds one coin to this <code>Player</code>
     */
    protected void addCoin(){
        numCoins += 1;
    }

    /**
     * Removes the specified amount of coins from this <code>Player</code>
     * @param amount the amount of coins to remove
     */
    protected void removeCoins(int amount){
        numCoins -= amount;
    }
}
