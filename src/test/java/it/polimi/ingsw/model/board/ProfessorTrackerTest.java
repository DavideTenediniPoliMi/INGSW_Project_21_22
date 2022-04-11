package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessorTrackerTest {
    ProfessorTracker profOwner;

    @BeforeEach
    public void setUp() {
        profOwner = new ProfessorTracker();
    }

    @AfterEach
    public void tearDown() {
        profOwner = null;
    }

    @Test
    public void testInitialization() {
        for(Color c: Color.values()) {
            assertEquals(-1, profOwner.getOwnerIDByColor(c));
        }
    }

    @Test
    public void testGetSetOwnerIDByColor() {
        profOwner.setOwnerIDByColor(2, Color.GREEN);
        assertEquals(2, profOwner.getOwnerIDByColor(Color.GREEN));
    }
}