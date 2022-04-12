package it.polimi.ingsw.model.enumerations;

/**
 * Class to hold every Assistant Card in the game.
 */
public enum Card {
    CARD_1(1, 1),
    CARD_2(2, 1),
    CARD_3(3, 2),
    CARD_4(4, 2),
    CARD_5(5, 3),
    CARD_6(6, 3),
    CARD_7(7, 4),
    CARD_8(8, 4),
    CARD_9(9, 5),
    CARD_10(10, 5);

    public final int WEIGHT;
    public final int RANGE;
    private boolean used;

    /**
     * Sole constructor to instantiate cards.
     *
     * @param weight    the decisive factor for the playing order
     * @param range     the number of steps Mother Nature can take
     */
    Card(int weight, int range) {
        this.WEIGHT = weight;
        this.RANGE = range;
    }

    /**
     * Sets this card as used. This prevents other players from using the same card in the same turn.
     */
    public void use() {
        this.used = true;
    }

    /**
     * Resets this card <code>used</code> field to <code>false<code/>. It's called
     * at the end of the round in order to allow every card to be picked in the
     * planning phase.
     */
    public void reset() {
        this.used = false;
    }

    /**
     * Returns whether this <code>Card</code> has already been picked in this
     * planning phase.
     *
     * @return <code>true</code> if this card has been used, <code>false<code/> otherwise
     */
    public boolean isUsed() {
        return used;
    }
}
