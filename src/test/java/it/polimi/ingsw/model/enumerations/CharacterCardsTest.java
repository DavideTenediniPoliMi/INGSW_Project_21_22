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

    @Test
    public void testMultiInstantiate(){
        Game g = Game.getInstance();
        g.instantiateCharacterCard(0);
        g.instantiateCharacterCard(0);
        g.instantiateCharacterCard(0);

        g.addPlayer(0, "s", TowerColor.BLACK, CardBack.CB_1, true);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);

        g.buyCharacterCard(0, 0);
        g.buyCharacterCard(0, 1);
        g.buyCharacterCard(0, 2);
    }
}