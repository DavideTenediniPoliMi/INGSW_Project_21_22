package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.characters.AlterInfluenceDecorator;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.ReturnToBagDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardsTest {

    @Test
    public void testInstantiate() {
        CharacterCard aid = CharacterCards.INFLUENCE_ADD_TWO.instantiate();
        assertNotNull(aid);
        assertEquals(aid.getClass(), AlterInfluenceDecorator.class);

        CharacterCard rtb = CharacterCards.RETURN_TO_BAG.instantiate();
        assertNotNull(rtb);
        assertEquals(rtb.getClass(), ReturnToBagDecorator.class);
    }
}