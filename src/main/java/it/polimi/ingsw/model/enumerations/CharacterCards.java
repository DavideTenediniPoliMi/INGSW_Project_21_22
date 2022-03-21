package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.characters.*;

import java.lang.reflect.InvocationTargetException;

public enum CharacterCards {
    //TODO COMPLETE PARAMETERS
    MOVE_TO_ISLAND(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1),
    MOVE_TO_DINING_ROOM(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 2),
    POOL_SWAP(StudentGroupDecorator.class, EffectType.STUDENT_GROUP, 1),
    BLOCK_ISLANDS(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 2),
    IGNORE_TOWERS(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3),
    INFLUENCE_ADD_TWO(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 2),
    IGNORE_COLOR(AlterInfluenceDecorator.class, EffectType.ALTER_INFLUENCE, 3),
    TIEBREAKER(TiebreakerDecorator.class, EffectType.TIEBREAKER, 2),
    RESOLVE_ISLAND(ResolveIslandDecorator.class, EffectType.RESOLVE_ISLAND, 3),
    ADD_TWO_STEPS(AddTwoStepsDecorator.class, EffectType.ADD_TWO_STEPS, 1),
    EXCHANGE_STUDENTS(ExchangeStudentsDecorator.class, EffectType.EXCHANGE_STUDENTS, 1),
    RETURN_TO_BAG(ReturnToBagDecorator.class, EffectType.RETURN_TO_BAG, 3);

    private final Class cardClass;
    private final EffectType effectType;
    private final int cost;
    private final Object[] parameters;

    CharacterCards(Class cardClass, EffectType effectType, int cost, Object...parameters){
        this.cardClass = cardClass;
        this.effectType = effectType;
        this.cost = cost;
        this.parameters = parameters;
    }

    public GenericCard instantiate(){
        GenericCard g = new GenericCard(cost, effectType);
        try {
            return (GenericCard) cardClass.getConstructors()[0].newInstance(g, parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Unable to instantiate " + this);
            e.printStackTrace();
            return null;
        }
    }
}
