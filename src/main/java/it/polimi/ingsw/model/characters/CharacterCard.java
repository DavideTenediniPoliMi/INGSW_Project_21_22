package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;

public abstract class CharacterCard {
    EffectType effectType;

    public CharacterCard(EffectType effectType) {
        this.effectType = effectType;
    }

    public abstract void effect();
}
