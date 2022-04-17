package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

/**
 * Class representing a generic <code>CharacterCard</code>.
 */
public class GenericCard extends CharacterCard {
    public GenericCard(int cost, EffectType effectType) {
        super(cost, effectType);
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

    @Override
    public void reset() {
    }

    @Override
    public void setParameters(Parameters params) {}
}
