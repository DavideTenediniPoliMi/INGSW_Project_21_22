package it.polimi.ingsw.model.enumerations;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Integer> useOrder;

    /**
     * Sole constructor to instantiate cards.
     *
     * @param weight    the decisive factor for the playing order
     * @param range     the number of steps Mother Nature can take
     */
    Card(int weight, int range) {
        this.WEIGHT = weight;
        this.RANGE = range;
        useOrder = new ArrayList<>();
    }

    /**
     * Sets this card as used. This prevents other players from using the same card in the same turn.
     * Also adds the player that used it to <code>useOrder</code>.
     */
    public void use(int playerID) {
        used = true;
        useOrder.add(playerID);
    }

    /**
     * Resets this card <code>used</code> field to <code>false<code/>. It's called
     * at the end of the round in order to allow every card to be picked in the
     * planning phase. Also resets <code>useOrder</code>.
     */
    public void reset() {
        used = false;
        useOrder.clear();
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

    /**
     * Returns the <code>useOrder</code> of this card in this planning phase.
     *
     * @return the <code>useOrder</code> list.
     */
    public List<Integer> getUseOrder() {
        return new ArrayList<>(useOrder);
    }
}