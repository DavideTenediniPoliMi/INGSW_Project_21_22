package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.MultiIsland;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.board.SimpleIsland;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.enumerations.CommandType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseParametersTest {
    /*
    ResponseParameters responseParameters;

    @Test
    void serialize() {
        responseParameters = new ResponseParameters();
        List<School> schools = new ArrayList<>();
        schools.add(new School(new Player(0, "gigi")));
        List<CharacterCard> characterCards = new ArrayList<>();
        characterCards.add(CharacterCards.RETURN_TO_BAG.instantiate());
        responseParameters.setPlayer(new Player(0, "gigi"))
                        .setBagEmpty(true)
                        .setSchools(schools)
                        .setCharacterCards(characterCards);
        System.out.println(responseParameters.serialize());
    }

    @Test
    void deserialize() {
        responseParameters = new ResponseParameters();
        List<School> schools = new ArrayList<>();
        schools.add(new School(new Player(0, "gigi")));
        List<CharacterCard> characterCards = new ArrayList<>();
        characterCards.add(CharacterCards.RETURN_TO_BAG.instantiate());
        List<Island> islands = new ArrayList<>();
        islands.add(new MultiIsland(new SimpleIsland(), new SimpleIsland()));
        islands.add(new SimpleIsland());

        responseParameters.setPlayer(new Player(0, "gigi"))
                .setBagEmpty(false)
                .setSchools(schools)
                .setCharacterCards(characterCards)
                .setIslands(islands);

        ResponseParameters rp1 = new ResponseParameters()
                .setPlayer(new Player(1, "marti"));
        System.out.println(rp1.serialize());
        System.out.println(responseParameters.serialize());
        rp1.deserialize(responseParameters.serialize());
        System.out.println(rp1.serialize().toString());
    }

    @Test
    void deserializeNull() {
        responseParameters = new ResponseParameters();

        ResponseParameters rp1 = new ResponseParameters().setPlayer(new Player(1, "marti"));
        System.out.println(rp1.serialize());
        System.out.println(responseParameters.serialize());
        rp1.deserialize(responseParameters.serialize());
        System.out.println(rp1.serialize().toString());
    }
    */
}