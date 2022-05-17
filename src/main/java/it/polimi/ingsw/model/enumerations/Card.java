package it.polimi.ingsw.model.enumerations;

import com.google.gson.*;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold every Assistant Card in the game.
 */
public enum Card implements Serializable {
    LION(1, 1),
    OSTRICH(2, 1),
    CAT(3, 2),
    EAGLE(4, 2),
    FOX(5, 3),
    VIPER(6, 3),
    OCTOPUS(7, 4),
    DOG(8, 4),
    ELEPHANT(9, 5),
    TORTOISE(10, 5),
    CARD_AFK(99, 0);

    public final int WEIGHT;
    public final int RANGE;
    private boolean used;
    private final List<Integer> useOrder;

    /**
     * Sole constructor to instantiate cards.
     *
     * @param weight    the decisive factor for the playing order
     * @param range     the number of steps Mother Nature can take
     */
    Card(int weight, int range) {
        this.WEIGHT = weight;
        this.RANGE = range;
        useOrder = new ArrayList<>();
    }

    /**
     * Sets this card as used. This prevents other players from using the same card in the same turn.
     * Also adds the player that used it to <code>useOrder</code>.
     */
    public void use(int playerID) {
        useOrder.add(playerID);
        if(equals(CARD_AFK))
            return;
        used = true;
    }

    /**
     * Resets this card <code>used</code> field to <code>false<code/>. It's called
     * at the end of the round in order to allow every card to be picked in the
     * planning phase. Also resets <code>useOrder</code>.
     */
    public void reset() {
        used = false;
        useOrder.clear();
    }

    /**
     * Returns whether this <code>Card</code> has already been picked in this
     * planning phase.
     *
     * @return <code>true</code> if this card has been used, <code>false<code/> otherwise
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Returns the <code>useOrder</code> of this card in this planning phase.
     *
     * @return the <code>useOrder</code> list.
     */
    public List<Integer> getUseOrder() {
        return new ArrayList<>(useOrder);
    }

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("used", new JsonPrimitive(used));
        JsonArray jsonUseOrder = new JsonArray();
        for(Integer i : useOrder) {
            jsonUseOrder.add(new JsonPrimitive(i));
        }
        jsonObject.add("useOrder", jsonUseOrder);

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        used = jsonObject.get("used").getAsBoolean();
        useOrder.clear();
        JsonArray jsonUseOrder = jsonObject.get("useOrder").getAsJsonArray();
        for(JsonElement player : jsonUseOrder) {
            useOrder.add(player.getAsInt());
        }
    }

    public static void deserializeAll(JsonObject jsonObject) {
        for(Card card : values()) {
            if(card.equals(CARD_AFK))
                continue;
            JsonObject cardObj = jsonObject.get(card.name()).getAsJsonObject();
            card.deserialize(cardObj);
        }
    }

    public static JsonObject serializeAll() {
        JsonObject jsonObject = new JsonObject();
        for(Card card : Card.values()) {
            if(card.equals(CARD_AFK))
                continue;
            jsonObject.add(card.name(), card.serialize());
        }

        return jsonObject;
    }
}