package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.helpers.Parameters;

/**
 * Class representing an abstract decorator for a generic <code>CharacterCard</code>. Used to implement different
 * effects to apply to the generic interface of a <code>CharacterCard</code>.
 */
public abstract class CharacterCardDecorator extends CharacterCard{
    protected CharacterCard card;

    /**
     * Sole constructor for a <code>CharacterCardDecorator</code>, specifies the card this decorator is going to
     * decorate.
     *
     * @param card the <code>CharacterCard</code> to decorate.
     */
    public CharacterCardDecorator(CharacterCard card) {
        super(card.getCost(), card.getEffectType());
        this.card = card;
    }

    /**
     * Returns the cost to buy this decorated <code>CharacterCard</code>.
     *
     * @return The cost of this decorated <code>CharacterCard</code>.
     */
    public int getCost() {
        return card.getCost();
    }

    /**
     * Increases the cost of this decorated <code>CharacterCard</code>.
     */
    public void increaseCost() {
        card.increaseCost();
    }

    /**
     * Returns whether this decorated <code>CharacterCard</code> is active.
     *
     * @return <code>true</code> if this <code>CharacterCard</code> is active.
     */
    public boolean isActive() {
        return card.isActive();
    }

    /**
     * Sets this decorated <code>CharacterCard</code> as active.
     */
    public void setActive() {
        card.setActive();
    }

    /**
     * Returns the <code>EffectType</code> of this decorated <code>CharacterCard</code>.
     *
     * @return <code>EffectType</code> of this <code>CharacterCard</code>.
     */
    public EffectType getEffectType() {
        return card.getEffectType();
    }

    /**
     * Sets this decorated <code>CharacterCard</code> as not active.
     */
    public void clearEffect() {
        card.clearEffect();
    }

    /**
     * Activates the effect of this decorated <code>CharacterCard</code>.
     *
     * @return The result of the activation of this decorated <code>CharacterCard</code>.
     */
    public abstract int activate();

    /**
     * Sets the <code>Parameters</code> for this decorated <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    public abstract void setParameters(Parameters params);

    public void reset() {
        card = null;
    }
}
