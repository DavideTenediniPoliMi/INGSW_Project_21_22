package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;

public abstract class CharacterCardDecorator {
    protected CharacterCard card;

    public CharacterCardDecorator(CharacterCard card) {
        this.card = card;
    }

    public abstract void activate();

    public int getCost() {
        return card.getCost();
    }

    public boolean isActive() {
        return card.isActive();
    }

    public EffectType getEffectType() {
        return card.getEffectType();
    }

    protected void increaseCost() {
        card.increaseCost();
    }

    protected void setActive() {
        card.setActive();
    }

    public void clearEffect() {
        card.clearEffect();
    }
}
