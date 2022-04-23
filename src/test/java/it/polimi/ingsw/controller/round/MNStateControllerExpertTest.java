package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.TurnState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MNStateControllerExpertTest {
    MNStateController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        game.addPlayer(0, "lollo", TowerColor.BLACK, CardBack.CB_1, true);
        game.addPlayer(1, "lello", TowerColor.WHITE, CardBack.CB_2, true);

        matchInfo = MatchInfo.getInstance();
        matchInfo.setSelectedNumPlayer(2);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setExpertMode(true);
        matchInfo.setNumPlayersConnected(2);
        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        RoundStateController oldState = new RoundStateController(new IslandController(), new DiningRoomController());
        controller = new MNStateController(oldState);
    }

    @AfterEach
    void tearDown() {
        Game.getInstance();
        MatchInfo.resetInstance();
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
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToIsland(0,Color.BLUE))
        );
    }


}