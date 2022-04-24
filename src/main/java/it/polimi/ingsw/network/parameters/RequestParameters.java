package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.ActionType;
import it.polimi.ingsw.model.enumerations.Color;

public class RequestParameters {
    private ActionType actionType;
    private int index;
    private Color color;
    private CardParameters cardParams;

    public ActionType getActionType() {
        return actionType;
    }

    public RequestParameters setActionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public RequestParameters setIndex(int index) {
        this.index = index;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public RequestParameters setColor(Color color) {
        this.color = color;
        return this;
    }

    public CardParameters getCardParams() {
        return cardParams;
    }

    public RequestParameters setCardParams(CardParameters cardParams) {
        this.cardParams = cardParams;
        return this;
    }
}
