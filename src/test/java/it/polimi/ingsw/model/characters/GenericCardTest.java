package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.enumerations.EffectType;
import junit.framework.TestCase;

public class GenericCardTest extends TestCase {
    GenericCard c;

    public void setUp() throws Exception {
        c = new GenericCard(1, EffectType.RETURN_TO_BAG);
    }

    public void tearDown() throws Exception {
        c = null;
    }

    public void testActivate() {
        assertEquals(1, c.getCost());
        assertEquals(EffectType.RETURN_TO_BAG, c.getEffectType());
        c.activate();
        assertTrue(c.isActive());
    }

    public void testSetParameters() {
    }
}