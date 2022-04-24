package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.ActionResponseParameters;

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
    /**
     * Returns <code>null</code>.
     *
     * @return <code>null</code>
     */
    @Override
    public ActionResponseParameters getResponseParameters() {
        return null;
    }

    @Override
    public void setParameters(CardParameters params) {}
}
