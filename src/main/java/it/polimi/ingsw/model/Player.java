package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int ID;
    private String name;
    private School school;
    private int numCoins = 0;
    private List<Card> playableCards = new ArrayList<>();
    private Card selectedCard = null;
    private final TowerColor teamColor;
    private final CardBack cardBack;
    private final boolean towerHolder;

    public Player(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder) {
        this.ID = ID;
        this.name = name;
        this.teamColor = teamColor;
        this.cardBack = cardBack;
        this.towerHolder = towerHolder;
    }
}
