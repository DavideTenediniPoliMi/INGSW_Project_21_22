package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.characters.*;
import javafx.scene.image.Image;

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
    MOVE_TO_ISLAND(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1, "/images/CarteTOT_front.jpg", new String[]{"MOVE TO", "ISLAND"}, true, false),
    MOVE_TO_DINING_ROOM(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 2, "/images/CarteTOT_front10.jpg", new String[]{"MOVE TO", "DINING"}, false, true),
    POOL_SWAP(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1, "/images/CarteTOT_front6.jpg", new String[]{"POOL", "SWAP"}, false, false),
    INFLUENCE_ADD_TWO(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 2, "/images/CarteTOT_front7.jpg", new String[]{"INFLUENCE", "ADD TWO"}, true, false, false),
    IGNORE_TOWERS(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3, "/images/CarteTOT_front5.jpg", new String[]{"IGNORE", "TOWERS"}, false, true, false),
    IGNORE_COLOR(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3, "/images/CarteTOT_front8.jpg", new String[]{"IGNORE", "COLOR"}, false, false, true),
    EXCHANGE_STUDENTS(ExchangeStudentsDecorator.class, EffectType.EXCHANGE_STUDENTS, 1, "/images/CarteTOT_front9.jpg", new String[]{"EXCHANGE", "STUDENTS"}),
    RETURN_TO_BAG(ReturnToBagDecorator.class, EffectType.RETURN_TO_BAG, 3, "/images/CarteTOT_front11.jpg", new String[]{"RETURN", "TO BAG"});

    private final Class cardClass;
    private final EffectType effectType;
    private final int cost;
    private final String path;
    private final String[] printableName;
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
    CharacterCards(Class cardClass, EffectType effectType, int cost, String path, String[] printableName, Object...parameters) {
        this.cardClass = cardClass;
        this.effectType = effectType;
        this.cost = cost;
        this.path = path;
        this.printableName = printableName;
        this.parameters = parameters;
    }

    /**
     * Returns a new instance of this Character card.
     *
     * @return <code>CharacterCard</code> instance for this card.
     */
    public CharacterCard instantiate(boolean... shouldDraw) {
        boolean paramShouldDraw = true;
        if(shouldDraw.length != 0) {
            paramShouldDraw = shouldDraw[0];
        }
        GenericCard gc = new GenericCard(this.name(), this.cost, this.effectType);
        ArrayList<Object> fullParams = new ArrayList<>();
        fullParams.add(gc);
        fullParams.addAll(Arrays.asList(parameters));
        if(effectType.equals(EffectType.STUDENT_GROUP)) {
            fullParams.add(paramShouldDraw);
        }

        try {
            return (CharacterCard) cardClass.getConstructors()[0].newInstance(fullParams.toArray());
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the printable name for this Character Card, formatted for the CLI.
     *
     * @return the printable name.
     */
    public String[] getPrintableName() {
        return printableName.clone();
    }

    /**
     * Returns an image corresponding to the character card
     *
     * @return an <code>Image</code> corresponding to the character card
     */
    public Image getImage() {
        return new Image(path);
    }
}
