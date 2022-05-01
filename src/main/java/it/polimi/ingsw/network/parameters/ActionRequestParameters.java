package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.model.enumerations.Color;

public class ActionRequestParameters {
    private CommandType commandType;
    private int index;
    private Color color;
    private CardParameters cardParams;

    public CommandType getActionType() {
        return commandType;
    }

    public ActionRequestParameters setActionType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public ActionRequestParameters setIndex(int index) {
        this.index = index;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public ActionRequestParameters setColor(Color color) {
        this.color = color;
        return this;
    }

    public CardParameters getCardParams() {
        return cardParams;
    }

    public ActionRequestParameters setCardParams(CardParameters cardParams) {
        this.cardParams = cardParams;
        return this;
    }
}
