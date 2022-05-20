package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.enumerations.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestParametersTest {
    /*
    RequestParameters requestParameters;

    @Test
    void serialize() {
        requestParameters = new RequestParameters();
        requestParameters.setCommandType(CommandType.JOIN)
                .setColor(Color.BLUE)
                .setName("ciao")
                .setCardParams(new CardParameters().setSelectedColor(Color.GREEN)
                        .setFromDestination(new StudentGroup(Color.RED,1)));
        System.out.println(requestParameters.serialize());
    }

    @Test
    void deserialize() {
        requestParameters = new RequestParameters();
        requestParameters.setCommandType(CommandType.JOIN)
                .setColor(Color.BLUE)
                .setName("ciao");

        RequestParameters rp1 = new RequestParameters().setName("albero");
        System.out.println(rp1.serialize());
        System.out.println(requestParameters.serialize());
        rp1.deserialize(requestParameters.serialize());
        System.out.println(rp1.serialize().toString());
    }

    @Test
    void deserializeNull() {
        requestParameters = new RequestParameters();;

        RequestParameters rp1 = new RequestParameters().setName("albero").setCommandType(CommandType.JOIN);
        System.out.println(rp1.serialize());
        System.out.println(requestParameters.serialize());

        rp1.deserialize(requestParameters.serialize());
        System.out.println(rp1.serialize().toString());
    }
    */
}