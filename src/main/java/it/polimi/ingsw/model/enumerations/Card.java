package it.polimi.ingsw.model.enumerations;

import com.google.gson.*;
import it.polimi.ingsw.utils.Serializable;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold every Assistant Card in the game.
 */
public enum Card implements Serializable {
    LION(1, 1, "/images/Assistente (1)"),
    OSTRICH(2, 1, "/images/Assistente (2)"),
    CAT(3, 2, "/images/Assistente (3)"),
    EAGLE(4, 2, "/images/Assistente (4)"),
    FOX(5, 3, "/images/Assistente (5)"),
    VIPER(6, 3, "/images/Assistente (6)"),
    OCTOPUS(7, 4, "/images/Assistente (7)"),
    DOG(8, 4, "/images/Assistente (8)"),
    ELEPHANT(9, 5, "/images/Assistente (9)"),
    TORTOISE(10, 5, "/images/Assistente (10)"),
    CARD_AFK(99, 0, "/images/cardback_bw_afk");

    public final int WEIGHT;
    public final int RANGE;
    private boolean used;
    private final List<Integer> useOrder;
    private final String path;

    /**
     * Sole constructor to instantiate cards.
     *
     * @param weight    the decisive factor for the playing order
     * @param range     the number of steps Mother Nature can take
     */
    Card(int weight, int range, String path) {
        this.WEIGHT = weight;
        this.RANGE = range;
        this.path = path;
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

    public Image getImage() {
        return new Image(path + ".png");
    }

    public Image getImageHalf() {
        return new Image(path + "_half.png");
    }

    public Image getImageBW() {
        return new Image(path + "_bw.png");
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