package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

public abstract class CharacterCard {
    private int cost;
    private boolean active;
    private final EffectType effectType;
    private Parameters parameters;

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

    public void setParameters(Parameters params){
        this.parameters = params;
    }

    public abstract int activate();
}
