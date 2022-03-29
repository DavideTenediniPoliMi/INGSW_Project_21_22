package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

public abstract class CharacterCardDecorator extends CharacterCard{
    protected CharacterCard card;

    public CharacterCardDecorator(CharacterCard card) {
        super(card.getCost(), card.getEffectType());
        this.card = card;
    }

    public int getCost() {
        return card.getCost();
    }

    public void increaseCost() {
        card.increaseCost();
    }

    public boolean isActive() {
        return card.isActive();
    }

    public void setActive() {
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
