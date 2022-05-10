package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    Lobby lobby;

    @BeforeEach
    void setUp() {
        lobby = Lobby.getLobby();

        lobby.addPlayer(0, "foo");
    }

    @AfterEach
    void tearDown() {
        Lobby.resetLobby();
        lobby = null;
    }

    @Test
    void addPlayer() {
        lobby.addPlayer(1, "bar");

        assertNotNull(lobby.getPlayerByID(1));
    }

    @Test
    void removePlayer() {
        lobby.removePlayer(0);

        assertNull(lobby.getPlayerByID(0));
    }

    @Test
    void hasJoined() {
        assertTrue(lobby.hasJoined(0));
        assertFalse(lobby.hasJoined(1));
    }

    @Test
    void selectTeam_first() {
        lobby.selectTeam(0, TowerColor.BLACK);

        assertTrue(lobby.getPlayerByID(0).isTowerHolder());
        assertEquals(TowerColor.BLACK, lobby.getPlayerByID(0).getTeamColor());
    }

    @Test
    void selectTeam_second() {
        lobby.selectTeam(0, TowerColor.BLACK);

        lobby.addPlayer(1, "bar");
        lobby.selectTeam(1, TowerColor.GREY);

        lobby.addPlayer(2, "baz");
        lobby.selectTeam(2, TowerColor.BLACK);

        assertFalse(lobby.getPlayerByID(2).isTowerHolder());
        assertEquals(TowerColor.BLACK, lobby.getPlayerByID(2).getTeamColor());
    }

    @Test
    void selectCardBack() {
        lobby.selectCardBack(0, CardBack.CB_1);

        assertEquals(CardBack.CB_1, lobby.getPlayerByID(0).getCardBack());
    }

    @Test
    void setReadyStatus() {
        lobby.setReadyStatus(0, true);
        assertTrue(lobby.isReady(0));
    }

    @Test
    void isNameTaken() {
        assertTrue(lobby.isNameTaken("foo"));
    }

    @Test
    void serialize() {
        System.out.println(lobby.serialize());
    }

    @Test
    void deserialize() {
        lobby.setReadyStatus(1, true);
        JsonObject lobbyJson = lobby.serialize();
        System.out.println(lobbyJson.toString());

        Lobby.resetLobby();
        Lobby lobby1 = Lobby.getLobby();
        lobby1.setReadyStatus(1, true);
        System.out.println(lobby1.serialize());

        lobby1.deserialize(lobbyJson);

        System.out.println(lobby1.serialize());
    }

    @Test
    void deserializeEmpty() {
        JsonObject jsonObject = new JsonObject();

        lobby.deserialize(jsonObject);
        assertTrue(lobby.getPlayers().isEmpty());
    }
}