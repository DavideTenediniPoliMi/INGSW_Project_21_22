package it.polimi.ingsw.model.characters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;

/**
 * Class representing a generic <code>CharacterCard</code>.
 */
public class GenericCard extends CharacterCard {
    public GenericCard(String name, int cost, EffectType effectType) {
        super(name, cost, effectType);
    }

    /**
     * Activates this <code>CharacterCard</code>'s effect and returns the default value.
     *
     * @return Default value for effect activation (0).
     */
    @Override
    public int activate() {
        setActive();
        return 0;
    }
    /**
     * Returns <code>null</code>.
     *
     * @return <code>null</code>
     */
    @Override
    public ResponseParameters getResponseParameters() {
        return null;
    }

    @Override
    public void setParameters(CardParameters params) {}

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();

        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        super.name = jsonObject.get("name").getAsString();
        super.cost = jsonObject.get("cost").getAsInt();
        super.active = jsonObject.get("active").getAsBoolean();
    }
}
