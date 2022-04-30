package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class GenericCardTest {
    GenericCard c;

    @BeforeEach
    public void setUp() throws Exception {
        c = new GenericCard(1, EffectType.RETURN_TO_BAG);
    }

    @AfterEach
    void tearDown() {
        c = null;
    }

    @Test
    public void testActivate() {
        assertEquals(1, c.getCost());
        assertEquals(EffectType.RETURN_TO_BAG, c.getEffectType());
        c.activate();
        c.setParameters(null);
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
        assertEquals(2, c.getCost());
    }

    @Test
    void testActionResponseParameters() {
        assertNull(c.getResponseParameters());
    }
}