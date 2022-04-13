package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.characters.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing 8 of the Character cards available in the game. These include:
 * <ul>
 *     <li>MOVE_TO_ISLAND: Move one student to an <code>Island</code>.</li>
 *     <li>MOVE_TO_DINING_ROOM: Move one student to a <code>Player</code>'s dining room.</li>
 *     <li>POOL_SWAP: Swap 3 students between a <code>Player</code>'s entrance and the card.</li>
 *     <li>INFLUENCE_ADD_TWO: Gives +2 points when calculating influence.</li>
 *     <li>IGNORE_TOWERS: Ignores a specified <code>TowerColor</code> when calculating influence.</li>
 *     <li>IGNORE_COLOR: Ignores a specified <code>Color</code> when calculating influence.</li>
 *     <li>EXCHANGE_STUDENTS: Swaps 2 students between a <code>Player</code>'s entrance and dining room</li>
 *     <li>RETURN_TO_BAG: Returns 3 students of the specified <code>Color</code> from each <code>Player</code>'s dining room</li>
 * </ul>
 */
public enum CharacterCards {
    MOVE_TO_ISLAND(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1, true, false),
    MOVE_TO_DINING_ROOM(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 2, false, true),
    POOL_SWAP(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1, false, false),
    INFLUENCE_ADD_TWO(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 2, true, false, false),
    IGNORE_TOWERS(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3, false, true, false),
    IGNORE_COLOR(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3, false, false, true),
    EXCHANGE_STUDENTS(ExchangeStudentsDecorator.class, EffectType.EXCHANGE_STUDENTS, 1),
    RETURN_TO_BAG(ReturnToBagDecorator.class, EffectType.RETURN_TO_BAG, 3);

    private final Class cardClass;
    private final EffectType effectType;
    private final int cost;
    private final Object[] parameters;

    /**
     * Sole constructor for a Character Card. Creates an entry specifying the information needed to instantiate this
     * Character Card.
     *
     * @param cardClass the <code>Class</code> of this Character card.
     * @param effectType the <code>EffectType</code> of this Character card.
     * @param cost the cost to buy this Character card.
     * @param parameters the various parameters to instantiate this Character card.
     */
    CharacterCards(Class cardClass, EffectType effectType, int cost, Object...parameters) {
        this.cardClass = cardClass;
        this.effectType = effectType;
        this.cost = cost;

        GenericCard gc = new GenericCard(this.cost, this.effectType);
        ArrayList<Object> fullParams = new ArrayList<>();
        fullParams.add(gc);
        fullParams.addAll(Arrays.asList(parameters));

        this.parameters = fullParams.toArray();
    }

    /**
     * Returns a new instance of this Character card.
     *
     * @return <code>CharacterCard</code> instance for this card.
     */
    public CharacterCard instantiate() {
        try {
            return (CharacterCard) cardClass.getConstructors()[0].newInstance(parameters);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            System.out.println("Failed to instantiate " + this);
            e.printStackTrace();
            return null;
        }
    }
}
