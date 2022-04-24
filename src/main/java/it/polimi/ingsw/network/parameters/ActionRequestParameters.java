package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.ActionType;
import it.polimi.ingsw.model.enumerations.Color;

public class ActionRequestParameters {
    private ActionType actionType;
    private int index;
    private Color color;
    private CardParameters cardParams;

    public ActionType getActionType() {
        return actionType;
    }

    public ActionRequestParameters setActionType(ActionType actionType) {
        this.actionType = actionType;
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
