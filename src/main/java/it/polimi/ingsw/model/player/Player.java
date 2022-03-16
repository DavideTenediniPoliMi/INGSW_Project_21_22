package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class Player {
    int ID;
    String name;
    School school;
    int numCoins = 0;
    List<Card> playableCards = new ArrayList<>();
    Card selectedCard = null;
    TowerColor teamColor;
    CardBack cardBack;

    public Player(int ID, String name, TowerColor teamColor, CardBack cardBack) {
        this.ID = ID;
        this.name = name;
        this.teamColor = teamColor;
        this.cardBack = cardBack;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public School getSchool() {
        return school;
    }

    public int getNumCoins() {
        return numCoins;
    }

    public void addCoins(int amount) {
        numCoins += amount;
    }

    public void removeCoins(int amount) {
        numCoins -= amount;
    }

    public List<Card> getPlayableCards() {
        return new ArrayList<>(playableCards);
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public TowerColor getTeamColor() {
        return teamColor;
    }

    public CardBack getCardBack() {
        return cardBack;
    }

    public void pickCard() {}

    public void playTurn (int numTowers) {}

    public void moveStudents (Board board) {}

    public void moveMN (Board board) {}

    public void pickCloud (Board board) {}
}
