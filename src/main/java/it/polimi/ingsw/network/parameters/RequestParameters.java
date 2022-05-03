package it.polimi.ingsw.network.parameters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.utils.Serializable;

/**
 * Class representing a request message from the client, with every information needed for any action
 */
public class RequestParameters implements Serializable {
    private CommandType commandType;
    private String name;
    private TowerColor towerColor;
    private CardBack cardBack;
    private boolean ready;
    private int index;
    private Color color;
    private CardParameters cardParams;

    /**
     * Sets the specified <code>CommandType</code> and returns this instance.
     *
     * @param commandType the <code>CommandType</code> of this message.
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setCommandType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    /**
     * Sets the specified <code>Player</code> name and returns this instance.
     *
     * @param name the nickname of a <code>Player</code> in this message.
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the specified <code>TowerColor</code> and returns this instance.
     *
     * @param towerColor the <code>TowerColor</code> in this message.
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
        return this;
    }

    /**
     * Sets the specified <code>CardBack</code> and returns this instance.
     *
     * @param cardBack the <code>CardBack</code> in this message.
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setCardBack(CardBack cardBack) {
        this.cardBack = cardBack;
        return this;
    }

    /**
     * Sets the specified ready status for the <code>Player</code> and returns this instance.
     *
     * @param ready the ready status in this message.
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setReady(boolean ready) {
        this.ready = ready;
        return this;
    }

    /**
     * Gets the <code>CommandType</code> of this message.
     *
     * @return the <code>CommandType</code>.
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Gets the name of this message.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the <code>TowerColor</code> of this message.
     *
     * @return the <code>TowerColor</code>.
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * Gets the <code>CardBack</code> of this message.
     *
     * @return the <code>CardBack</code>.
     */
    public CardBack getCardBack() {
        return cardBack;
    }

    /**
     * Gets the ready flag of this message.
     *
     * @return the ready flag.
     */
    public boolean isReady() {
        return ready;
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
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setIndex(int index) {
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
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setColor(Color color) {
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
     * @return this <code>RequestParameters</code>
     */
    public RequestParameters setCardParams(CardParameters cardParams) {
        this.cardParams = cardParams;
        return this;
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(this).getAsJsonObject();

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        if(jsonObject.has("commandType"))
            commandType = CommandType.valueOf(jsonObject.get("commandType").getAsString());
        name = jsonObject.get("name").getAsString();
        if(jsonObject.has("towerColor"))
            towerColor = TowerColor.valueOf(jsonObject.get("towerColor").getAsString());
        if(jsonObject.has("cardBack"))
            cardBack = CardBack.valueOf(jsonObject.get("cardBack").getAsString());
        ready = jsonObject.get("ready").getAsBoolean();
        index = jsonObject.get("index").getAsInt();
        if(jsonObject.has("color"))
            color = Color.valueOf(jsonObject.get("color").getAsString());

        if(jsonObject.has("cardParams")) {
            cardParams = new CardParameters();
            cardParams.deserialize(jsonObject.get("cardParams").getAsJsonObject());
        }
    }
}
