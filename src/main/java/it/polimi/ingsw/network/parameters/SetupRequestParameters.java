package it.polimi.ingsw.network.parameters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.utils.Serializable;

/**
 * Class representing a request message from the client, with every information needed for any action during the pre-game
 * <code>Lobby</code>.
 */
public class SetupRequestParameters implements Serializable {
    private CommandType commandType;
    private String name;
    private TowerColor towerColor;
    private CardBack cardBack;
    private boolean ready;

    /**
     * Sets the specified <code>CommandType</code> and returns this instance.
     *
     * @param commandType the <code>CommandType</code> of this message.
     * @return this <code>SetupRequestParameters</code>
     */
    public SetupRequestParameters setCommandType(CommandType commandType) {
        this.commandType = commandType;
        return this;
    }

    /**
     * Sets the specified <code>Player</code> name and returns this instance.
     *
     * @param name the nickname of a <code>Player</code> in this message.
     * @return this <code>SetupRequestParameters</code>
     */
    public SetupRequestParameters setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the specified <code>TowerColor</code> and returns this instance.
     *
     * @param towerColor the <code>TowerColor</code> in this message.
     * @return this <code>SetupRequestParameters</code>
     */
    public SetupRequestParameters setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
        return this;
    }

    /**
     * Sets the specified <code>CardBack</code> and returns this instance.
     *
     * @param cardBack the <code>CardBack</code> in this message.
     * @return this <code>SetupRequestParameters</code>
     */
    public SetupRequestParameters setCardBack(CardBack cardBack) {
        this.cardBack = cardBack;
        return this;
    }

    /**
     * Sets the specified ready status for the <code>Player</code> and returns this instance.
     *
     * @param ready the ready status in this message.
     * @return this <code>SetupRequestParameters</code>
     */
    public SetupRequestParameters setReady(boolean ready) {
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

    @Override
    public JsonObject serialize() {
        return null;
    }

    @Override
    public void deserialize(String json) {

    }
}
