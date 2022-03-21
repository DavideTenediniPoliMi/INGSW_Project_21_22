package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;

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

    public boolean isActive() {
        return active;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    protected void increaseCost() {
        cost += 1;
    }

    protected void setActive() {
        active = true;
    }

    public void clearEffect() {
        active = false;
    }

    public abstract void activate();
}
