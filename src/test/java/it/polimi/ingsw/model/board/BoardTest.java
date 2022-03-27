package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import junit.framework.TestCase;

public class BoardTest extends TestCase {

    Game g;
    Board b;
    int num;

    public void setUp() {
        g = Game.getInstance();
        b = g.getBoard();
        num = b.getNumCoinsLeft();
    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
        b = null;
    }

    public void testGetNumCoinsLeft() {
        assertEquals(num, b.getNumCoinsLeft());
    }

    public void testPutCoinsBack() {
        num = b.getNumCoinsLeft();
        b.putCoinsBack(1);
        assertEquals(num + 1, b.getNumCoinsLeft());
        num += 1;
    }

    public void testTakeCoin() {
        num = b.getNumCoinsLeft();
        b.takeCoin();
        assertEquals(num - 1, b.getNumCoinsLeft());
        num -= 1;
    }
}