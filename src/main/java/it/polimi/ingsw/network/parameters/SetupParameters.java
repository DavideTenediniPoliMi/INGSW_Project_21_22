package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.SetupType;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class SetupParameters {
    private SetupType setupType;
    private String name;
    private TowerColor towerColor;
    private CardBack cardBack;
    private boolean ready;

    public SetupParameters setSetupType(SetupType setupType) {
        this.setupType = setupType;
        return this;
    }

    public SetupParameters setName(String name) {
        this.name = name;
        return this;
    }

    public SetupParameters setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
        return this;
    }

    public SetupParameters setCardBack(CardBack cardBack) {
        this.cardBack = cardBack;
        return this;
    }

    public SetupParameters setReady(boolean ready) {
        this.ready = ready;
        return this;
    }

    public SetupType getSetupType() {
        return setupType;
    }

    public String getName() {
        return name;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public CardBack getCardBack() {
        return cardBack;
    }

    public boolean isReady() {
        return ready;
    }
}
