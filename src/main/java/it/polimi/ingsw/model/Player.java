package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int ID;
    private final String name;
    private final TowerColor teamColor;
    private final CardBack cardBack;
    private final boolean towerHolder;
    private final List<Card> playableCards = new ArrayList<>();
    private Card selectedCard;
    private int numCoins;

    public Player(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder) {
        this.ID = ID;
        this.name = name;
        this.teamColor = teamColor;
        this.cardBack = cardBack;
        this.towerHolder = towerHolder;

        this.playableCards.addAll(List.of(Card.values()));
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public TowerColor getTeamColor() {
        return teamColor;
    }

    public CardBack getCardBack() {
        return cardBack;
    }

    public boolean isTowerHolder() {
        return towerHolder;
    }

    public List<Card> getPlayableCards(){
        return playableCards;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    protected void setSelectedCard(Card card) {
        selectedCard = card;
        playableCards.remove(card);
    }

    public int getNumCoins() {
        return numCoins;
    }

    protected void addCoin(){
        numCoins += 1;
    }

    protected void removeCoins(int amount){
        numCoins -= amount;
    }
}
