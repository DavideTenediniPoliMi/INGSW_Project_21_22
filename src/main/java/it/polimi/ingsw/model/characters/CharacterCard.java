package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.utils.Serializable;

/**
 * Abstract class representing a generic Character Card.
 */
public abstract class CharacterCard implements Serializable {
    private int cost;
    private boolean active;
    private final EffectType effectType;

    /**
     * Sole constructor for a Character card. Specifies this card's cost and effect type.
     *
     * @param cost the cost to buy this <code>CharacterCard</code>.
     * @param effectType the <code>EffectType</code> of this <code>CharacterCard</code>.
     */
    public CharacterCard(int cost, EffectType effectType) {
        this.cost = cost;
        this.effectType = effectType;
    }

    /**
     * Returns the cost of this <code>CharacterCard</code>.
     *
     * @return Cost for this card
     */
    public int getCost() {
        return cost;
    }

    /**
     * Increments the cost of this <code>CharacterCard</code> by 1.
     */
    public void increaseCost() {
        cost += 1;
    }

    /**
     * Returns whether this <code>CharacterCard</code> is active.
     *
     * @return <code>true</code> if this card is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets this <code>CharacterCard</code> as active.
     */
    public void setActive() {
        active = true;
    }

    /**
     * Returns the <code>EffectType</code> of this <code>CharacterCard</code>.
     *
     * @return <code>EffectType</code> of this <code>CharacterCard</code>.
     */
    public EffectType getEffectType() {
        return effectType;
    }

    /**
     * Sets this <code>CharacterCard</code> as not active.
     */
    public void clearEffect() {
        active = false;
    }

    /**
     * Sets the <code>Parameters</code> for this <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    public abstract void setParameters(CardParameters params);

    /**
     * Activates this <code>CharacterCard</code>'s effect.
     *
     * @return The result of this card's effect.
     */
    public abstract int activate();

    /**
     * Returns <code>ResponseParameters</code> for this <code>CharacterCard</code>.
     *
     * @return <code>ResponseParameters</code> for this <code>CharacterCard</code>.
     */
    public abstract ResponseParameters getResponseParameters();
}
