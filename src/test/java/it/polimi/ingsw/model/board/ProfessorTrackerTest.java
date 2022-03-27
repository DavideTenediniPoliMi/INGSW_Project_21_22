package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import junit.framework.TestCase;

public class ProfessorTrackerTest extends TestCase {

    Game g;
    Board b;

    public void setUp() {
       g = Game.getInstance();
       b = g.getBoard();

       g.addPlayer(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
       g.addPlayer(1, "luigi", TowerColor.WHITE, CardBack.CB_2, true);

       for(Player p : g.getPlayers()){
           b.addSchool(p);
       }

       b.giveProfessorTo(0, Color.GREEN);
    }

    public void tearDown() {
        Game.resetInstance();
        g = null;
        b = null;
    }

    public void testGetOwnerIDByColor() {
        assertEquals(0, b.getProfessorOwners().getOwnerIDByColor(Color.GREEN));
    }

    public void testSetOwnerIDByColor() {
        b.giveProfessorTo(1, Color.BLUE);
        assertEquals(1, b.getProfessorOwners().getOwnerIDByColor(Color.BLUE));
    }
}