package it.polimi.ingsw.network.parameters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.utils.Serializable;

/**
 * Class representing a request message from the client, with every information needed for any action during the game.
 */
public class ActionRequestParameters implements Serializable {
    private CommandType commandType;
    private int index;
    private Color color;
    private CardParameters cardParams;

    /**
     * Gets the <code>CommandType</code> of this message.
     *
     * @return the <code>CommandType</code>.
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Sets the specified <code>CommandType</code> and returns this instance.
     *
     * @param commandType the <code>CommandType</code> of this message.
     * @return this <code>ActionRequestParameters</code>
     */
    public ActionRequestParameters setCommandType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    /**
     * Gets the index of this message.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the specified index and returns this instance.
     *
     * @param index the index for this message
     * @return this <code>ActionRequestParameters</code>
     */
    public ActionRequestParameters setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets the <code>Color</code> of this message.
     *
     * @return the <code>Color</code>
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the specified <code>Color</code> and returns this instance.
     *
     * @param color the <code>Color</code> of this message.
     * @return this <code>ActionRequestParameters</code>
     */
    public ActionRequestParameters setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Gets the <code>CardParameters</code> of this message.
     *
     * @return the <code>CardParameters</code>.
     */
    public CardParameters getCardParams() {
        return cardParams;
    }

    /**
     * Sets the specified <code>CardParameters</code> and returns this instance.
     *
     * @param cardParams the <code>CardParameters</code> of this message.
     * @return this <code>ActionRequestParameters</code>
     */
    public ActionRequestParameters setCardParams(CardParameters cardParams) {
        this.cardParams = cardParams;
        return this;
    }

    @Override
    public JsonObject serialize() {
        return null;
    }

    @Override
    public void deserialize(String json) {

    }
}
