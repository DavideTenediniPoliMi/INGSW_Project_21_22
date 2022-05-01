package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class SetupRequestParameters {
    private CommandType commandType;
    private String name;
    private TowerColor towerColor;
    private CardBack cardBack;
    private boolean ready;

    public SetupRequestParameters setCommandType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    public SetupRequestParameters setName(String name) {
        this.name = name;
        return this;
    }

    public SetupRequestParameters setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
        return this;
    }

    public SetupRequestParameters setCardBack(CardBack cardBack) {
        this.cardBack = cardBack;
        return this;
    }

    public SetupRequestParameters setReady(boolean ready) {
        this.ready = ready;
        return this;
    }

    public CommandType getCommandType() {
        return commandType;
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
