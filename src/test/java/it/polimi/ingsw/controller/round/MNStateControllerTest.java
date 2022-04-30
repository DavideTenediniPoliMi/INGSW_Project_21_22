package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.board.MNOutOfRangeException;
import it.polimi.ingsw.exceptions.game.ExpertModeDisabledException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.network.parameters.CardParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MNStateControllerTest {
    MNStateController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        game.addPlayer(new Player(0, "lollo", TowerColor.BLACK, CardBack.CB_1, true));
        game.addPlayer(new Player(1, "lello", TowerColor.WHITE, CardBack.CB_2, true));
        game.addPlayer(new Player(2, "lillo", TowerColor.GREY, CardBack.CB_3, true));
        game.placeMNAt(0);

        matchInfo = MatchInfo.getInstance();
        matchInfo.setSelectedNumPlayer(3);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setExpertMode(false);
        matchInfo.setNumPlayersConnected(3);
        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        RoundStateController oldState = new RoundStateController(new IslandController(), new DiningRoomController());
        controller = new MNStateController(oldState);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        MatchInfo.resetInstance();
        controller = null;
        game = null;
        matchInfo = null;
    }

    @Test
    public void testIllegalActions() {
        assertAll(
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.definePlayOrder()),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.playCard(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.collectFromCloud(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToDiningRoom(Color.BLUE)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToIsland(0,Color.BLUE)),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.buyCharacterCard(0)),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.setCardParameters(new CardParameters())),
                () -> assertThrowsExactly(ExpertModeDisabledException.class, () -> controller.activateCard())
        );
    }

    @Test
    public void outOfRangTest() {
        game.playCard(0, Card.CARD_1);
        assertThrowsExactly(MNOutOfRangeException.class, () -> controller.moveMN(2));
    }

    @Test
    public void nullPlayerTest() {
        matchInfo.removePlayer();
        matchInfo.removePlayer();
        assertThrowsExactly(NullPlayerException.class, () -> controller.moveMN(2));
    }

    @Test
    public void moveMNTest() throws MNOutOfRangeException {
        game.playCard(0, Card.CARD_1);
        controller.moveMN(1);
        assertEquals(1, game.getBoard().getMNPosition());
    }
}