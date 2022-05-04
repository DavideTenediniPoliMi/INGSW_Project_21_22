package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.lobby.*;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.commands.LobbyJoinCommand;
import it.polimi.ingsw.network.commands.LobbyReadyStatusCommand;
import it.polimi.ingsw.network.commands.LobbySelectCBCommand;
import it.polimi.ingsw.network.commands.LobbySelectTeamCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyControllerTest {
    LobbyController controller;
    MatchInfo info;
    Lobby lobby;

    @BeforeEach
    void setUp() throws Exception{
        info = MatchInfo.getInstance();
        lobby = Lobby.getLobby();
        controller = new LobbyController();

        info.setUpGame(2,true);

        controller.requestCommand(new LobbyJoinCommand(0, "LOLLO", controller));
        controller.requestCommand(new LobbySelectCBCommand(0, CardBack.CB_1, controller));
        controller.requestCommand(new LobbySelectTeamCommand(0, TowerColor.BLACK, controller));
        controller.requestCommand(new LobbyReadyStatusCommand(0, true, controller));
    }

    @AfterEach
    void tearDown() {
        Lobby.resetLobby();
        MatchInfo.resetInstance();
    }

    @Test
    public void testJoinGame() {
        assertTrue(lobby.hasJoined(0));
    }

    @Test
    public void testSelectCB() {
        assertEquals(CardBack.CB_1, lobby.getPlayerByID(0).getCardBack());
    }

    @Test
    public void testSelectTeam() {
        assertEquals(TowerColor.BLACK, lobby.getPlayerByID(0).getTeamColor());
    }
    @Test
    public void testReady() {
        assertTrue(lobby.isReady(0));
    }

    @Test
    public void testJoinFull() throws Exception{
        controller.requestCommand(new LobbyJoinCommand(1, "LELLO", controller));
        assertThrowsExactly(GameFullException.class, () -> controller.requestCommand(new LobbyJoinCommand(2, "LULLO", controller)));
    }

    @Test
    public void testJoinDuplicateID() {
        assertThrowsExactly(DuplicateIDException.class, () -> controller.requestCommand(new LobbyJoinCommand(0, "LELLO", controller)));
    }

    @Test
    public void testJoinDuplicateName() {
        assertThrowsExactly(NameTakenException.class, () -> controller.requestCommand(new LobbyJoinCommand(1, "LOLLO", controller)));
    }

    @Test
    public void testInvalidPlayer() {
        assertAll(
                () -> assertThrowsExactly(NoSuchPlayerException.class, () -> controller.requestCommand(new LobbySelectCBCommand(1, CardBack.CB_1, controller))),
                () -> assertThrowsExactly(NoSuchPlayerException.class, () -> controller.requestCommand(new LobbySelectTeamCommand(1, TowerColor.BLACK, controller))),
                () -> assertThrowsExactly(NoSuchPlayerException.class, () -> controller.requestCommand(new LobbyReadyStatusCommand(1, true, controller))),
                () -> assertThrowsExactly(NoSuchPlayerException.class, () -> controller.requestCommand(new LobbySelectTeamCommand(1, TowerColor.BLACK, controller))),
                () -> assertThrowsExactly(NoSuchPlayerException.class, () -> controller.removePlayer(1))
        );
    }

    @Test
    public void testDuplicateCB() throws Exception{
        controller.requestCommand(new LobbyJoinCommand(1, "Lello", controller));
        assertThrowsExactly(CardBackTakenException.class, () -> controller.requestCommand(new LobbySelectCBCommand(1, CardBack.CB_1, controller)));
    }

    @Test
    public void testDuplicateTeam() throws Exception{
        controller.requestCommand(new LobbyJoinCommand(1, "Lello", controller));
        assertThrowsExactly(TeamFullException.class, () -> controller.requestCommand(new LobbySelectTeamCommand(1, TowerColor.BLACK, controller)));
    }

    @Test
    public void testRemove() throws Exception{
        controller.removePlayer(0);
        assertFalse(lobby.hasJoined(0));
    }

    @Test
    public void testJoinSameColor() throws Exception{
        info.setUpGame(4,true);
        controller.requestCommand(new LobbyJoinCommand(1, "lello", controller));
        controller.requestCommand(new LobbySelectTeamCommand(1, TowerColor.BLACK, controller));
    }

    @Test
    public void testFailedRequest() {
        info.setGameStatus(GameStatus.IN_GAME);
        assertThrowsExactly(GameStartedException.class, () -> controller.requestCommand(new LobbyJoinCommand(3, "lillo", controller)));
    }
}