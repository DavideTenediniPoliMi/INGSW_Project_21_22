package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a generic Character Card.
 */
public abstract class CharacterCard implements Serializable, Printable<List<String>> {
    protected String name;
    protected int cost;
    protected boolean active;
    protected EffectType effectType;

    /**
     * Sole constructor for a Character card. Specifies this card's cost and effect type.
     *
     * @param cost the cost to buy this <code>CharacterCard</code>.
     * @param effectType the <code>EffectType</code> of this <code>CharacterCard</code>.
     */
    public CharacterCard(String name, int cost, EffectType effectType) {
        this.name = name;
        this.cost = cost;
        this.effectType = effectType;
    }

    /**
     * Returns the name of this <code>CharacterCard</code>
     *
     * @return name of this card
     */
    public String getName() {
        return name;
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

    @Override
    public List<String> print(boolean... params) {
        StringBuilder cardBuilder = new StringBuilder();
        List<String> cardString = new ArrayList<>();

        cardBuilder.append("┌───────────┐");
        cardString.add(cardBuilder.toString());
        cardBuilder.setLength(0);

        cardBuilder.append("│").append(Game.getInstance().getIndexOfCharacterCard(this))
                    .append(" ")
                    .append(active ? AnsiCodes.GREEN_BACKGROUND_BRIGHT : AnsiCodes.RED_BACKGROUND_BRIGHT)
                    .append(" A ").append(AnsiCodes.RESET)
                    .append(" ".repeat(2))
                    .append(cost).append(" ").append("©")
                    .append(" │");
        cardString.add(cardBuilder.toString());
        cardBuilder.setLength(0);

        String[] printableName = CharacterCards.valueOf(name).getPrintableName();
        cardBuilder.append("│ ").append(printableName[0]).append(" ".repeat(9 - printableName[0].length())).append(" │");
        cardString.add(cardBuilder.toString());
        cardBuilder.setLength(0);

        cardBuilder.append("│ ").append(printableName[1]).append(" ".repeat(9 - printableName[1].length())).append(" │");
        cardString.add(cardBuilder.toString());
        cardBuilder.setLength(0);

        cardBuilder.append("└───────────┘");
        cardString.add(cardBuilder.toString());
        cardBuilder.setLength(0);

        return cardString;
    }
}
