package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

public abstract class CharacterCard {
    private int cost;
    private boolean active;
    private final EffectType effectType;

    public CharacterCard(int cost, EffectType effectType) {
        this.cost = cost;
        this.effectType = effectType;
    }

    public int getCost() {
        return cost;
    }

    public void increaseCost() {
        cost += 1;
    }

    public boolean isActive() {
        return active;
    }

    protected void setActive() {
        active = true;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public void clearEffect() {
        active = false;
    }

    public abstract void setParameters(Parameters params);

    public abstract int activate();
}
