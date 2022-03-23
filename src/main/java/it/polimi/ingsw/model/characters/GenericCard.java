package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;

public class GenericCard extends CharacterCard{
    public GenericCard(int cost, EffectType effectType) {
        super(cost, effectType);
    }

    public int activate() {
        setActive();
        increaseCost();
        return 0;
    }
}
