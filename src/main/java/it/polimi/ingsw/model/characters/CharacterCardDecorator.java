package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

public abstract class CharacterCardDecorator {
    protected CharacterCard card;

    public CharacterCardDecorator(CharacterCard card) {
        this.card = card;
    }

    public int getCost() {
        return card.getCost();
    }

    protected void increaseCost() {
        card.increaseCost();
    }

    public boolean isActive() {
        return card.isActive();
    }

    protected void setActive() {
        card.setActive();
    }

    public EffectType getEffectType() {
        return card.getEffectType();
    }

    public void clearEffect() {
        card.clearEffect();
    }

    public abstract int activate();

    public abstract void setParameters(Parameters params);
}
