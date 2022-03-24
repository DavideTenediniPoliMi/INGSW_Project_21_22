package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

public class GenericCard extends CharacterCard {
    public GenericCard(int cost, EffectType effectType) {
        super(cost, effectType);
    }

    @Override
    public int activate() {
        setActive();
        return 0;
    }

    @Override
    public void setParameters(Parameters params) {}
}
