package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.characters.*;

import java.lang.reflect.InvocationTargetException;

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

    CharacterCards(Class cardClass, EffectType effectType, int cost, Object...parameters) {
        this.cardClass = cardClass;
        this.effectType = effectType;
        this.cost = cost;
        this.parameters = parameters;
    }

    public CharacterCard instantiate() {
        GenericCard g = new GenericCard(cost, effectType);

        try {
            if(parameters == null) {
                return (CharacterCard) cardClass.getConstructors()[0].newInstance(g);
            } else {
                return (CharacterCard) cardClass.getConstructors()[0].newInstance(g, parameters);
            }
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to instantiate " + this);
            e.printStackTrace();
            return null;
        }
    }
}
