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
    void setSelectedCard() {
        Card c = Card.CARD_1;

        assertNull(player.getSelectedCard());
        player.setSelectedCard(c);
        assertEquals(Card.CARD_1, player.getSelectedCard());
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
}