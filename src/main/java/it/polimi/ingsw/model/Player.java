package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private final int ID;
    private final String name;
    private int numCoins = 0;
    private final List<Card> playableCards = new ArrayList<>();
    private Card selectedCard;
    private final TowerColor teamColor;
    private final CardBack cardBack;
    private final boolean towerHolder;

    public Player(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder) {
        this.ID = ID;
        this.name = name;
        this.teamColor = teamColor;
        this.cardBack = cardBack;
        this.towerHolder = towerHolder;
        this.playableCards.addAll(List.of(Card.values()));
    }

    protected void addCoin(){
        this.numCoins += 1;
    }

    protected void removeCoins(int amt){
        this.numCoins -= amt;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getNumCoins() {
        return numCoins;
    }

    public Card getSelectedCard() {
        return selectedCard;
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

    protected void setSelectedCard(Card card) {
        selectedCard = card;
        playableCards.remove(card);
    }
}
