package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.parameters.CardParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardDecoratorTest {
    CharacterCard c;

    @BeforeEach
    public void setUp() {
        c = CharacterCards.RETURN_TO_BAG.instantiate();
    }

    @AfterEach
    public void tearDown() {
        c = null;
    }

    @Test
    public void testActivate() {
        assertEquals(3, c.getCost());
        assertEquals(EffectType.RETURN_TO_BAG, c.getEffectType());
        c.setActive();
        CardParameters p = new CardParameters();
        p.setSelectedColor(Color.RED);
        c.setParameters(p);
        assertTrue(c.isActive());
    }

    @Test
    public void testClearEffect() {
        c.clearEffect();
        assertFalse(c.isActive());
    }

    @Test
    public void testIncrementCost() {
        c.increaseCost();
        assertEquals(4, c.getCost());
    }
}
