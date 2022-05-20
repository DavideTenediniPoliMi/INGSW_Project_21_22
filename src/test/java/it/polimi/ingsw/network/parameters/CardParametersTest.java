package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardParametersTest {
    /*
    CardParameters cardParameters;

    @Test
    void serialize() {
        cardParameters = new CardParameters();
        cardParameters.setFromOrigin(new StudentGroup(Color.BLUE, 2))
                .setFromDestination(new StudentGroup(Color.RED, 2))
                .setCurrentTeam(TowerColor.BLACK)
                .setBoostedTeam(TowerColor.BLACK)
                .setSelectedColor(Color.BLUE)
                .setIslandIndex(0)
                .setPlayerID(0);
        System.out.println(cardParameters.serialize());
    }

    @Test
    void deserialize() {
        cardParameters = new CardParameters();
        cardParameters.setFromOrigin(new StudentGroup(Color.BLUE, 2))
                .setFromDestination(new StudentGroup(Color.RED, 2))
                .setCurrentTeam(TowerColor.BLACK)
                .setBoostedTeam(TowerColor.BLACK)
                .setSelectedColor(Color.BLUE)
                .setIslandIndex(0)
                .setPlayerID(0);
        CardParameters cp1 = new CardParameters().setPlayerID(1);
        System.out.println(cp1.serialize());
        System.out.println(cardParameters.serialize());
        cp1.deserialize(cardParameters.serialize());
        System.out.println(cp1.serialize().toString());
    }

    @Test
    void deserializeNull() {
        cardParameters = new CardParameters();

        CardParameters cp1 = new CardParameters().setPlayerID(1);
        System.out.println(cp1.serialize());
        System.out.println(cardParameters.serialize());
        cp1.deserialize(cardParameters.serialize());
        System.out.println(cp1.serialize().toString());
    }

     */
}