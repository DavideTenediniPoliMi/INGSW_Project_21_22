package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player(0, "mario", TowerColor.BLACK, CardBack.CB_1, true);
    }

    @AfterEach
    void tearDown() {
        player = null;
    }

    @Test
    public void testGetter() {
        assertAll(
                () -> assertEquals(CardBack.CB_1, player.getCardBack()),
                () -> assertTrue(player.isTowerHolder())
        );
    }

    @Test
    void setSelectedCard() {
        Card c = Card.CARD_1;

        assertNull(player.getSelectedCard());
        player.setSelectedCard(c);
        assertEquals(Card.CARD_1, player.getSelectedCard());
        assertEquals(9, player.getPlayableCards().size());
    }

    @Test
    void addCoin() {
        int coinsBefore = player.getNumCoins();
        player.addCoin();
        assertEquals(coinsBefore + 1, player.getNumCoins());
    }

    @Test
    void removeCoins() {
        int coinsBefore = player.getNumCoins();
        int amount = 2;

        player.removeCoins(2);
        assertEquals(coinsBefore - amount, player.getNumCoins());
    }

    @Test
    void deserializeTest() {
        Player p = new Player(1, "ciao");
        System.out.println(player.serialize());
        assertDoesNotThrow( () -> p.deserialize(player.serialize()));
        p.setSelectedCard(Card.CARD_1);

        Player p2 = new Player(3, "carlo");
        assertDoesNotThrow( () -> p2.deserialize(p.serialize()));
        System.out.println(p.serialize().toString());
    }

    @Test
    void deserializeNull() {
        player = new Player(0, "nino");
        Player p = new Player(1, "ciao");
        System.out.println(player.serialize());
        assertDoesNotThrow(() -> p.deserialize(player.serialize()));
        System.out.println(p.serialize().toString());
    }
}