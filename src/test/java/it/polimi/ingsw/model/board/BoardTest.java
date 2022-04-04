package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    Game g;
    Board b;
    int num;

    @BeforeEach
    public void setUp() {
        g = Game.getInstance();
        b = g.getBoard();
        num = b.getNumCoinsLeft();
    }

    @AfterEach
    public void tearDown() {
        Game.resetInstance();
        g = null;
        b = null;
    }

    @Test
    public void testGetNumCoinsLeft() {
        assertEquals(num, b.getNumCoinsLeft());
    }

    @Test
    public void testPutCoinsBack() {
        num = b.getNumCoinsLeft();
        b.putCoinsBack(1);
        assertEquals(num + 1, b.getNumCoinsLeft());
        num += 1;
    }

    @Test
    public void testTakeCoin() {
        num = b.getNumCoinsLeft();
        b.takeCoin();
        assertEquals(num - 1, b.getNumCoinsLeft());
        num -= 1;
    }
}