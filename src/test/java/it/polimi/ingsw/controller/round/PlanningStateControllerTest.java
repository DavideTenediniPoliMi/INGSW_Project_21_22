package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.controller.subcontrollers.DiningRoomController;
import it.polimi.ingsw.controller.subcontrollers.IslandController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.player.CardUsedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlanningStateControllerTest {
    PlanningStateController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        game.addPlayer(0, "lollo", TowerColor.BLACK, CardBack.CB_1, true);
        game.addPlayer(1, "lello", TowerColor.WHITE, CardBack.CB_2, true);
        game.addPlayer(2, "lillo", TowerColor.GREY, CardBack.CB_3, true);

        matchInfo = MatchInfo.getInstance();
        matchInfo.setSelectedNumPlayer(3);
        matchInfo.setStateType(TurnState.STUDENTS);
        matchInfo.setExpertMode(false);
        matchInfo.setNumPlayersConnected(3);

        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);

        RoundStateController oldState = new RoundStateController(new IslandController(), new DiningRoomController());
        controller = new PlanningStateController(oldState);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        MatchInfo.resetInstance();
        game = null;
        matchInfo = null;
    }

    @Test
    public void testIllegalActions() {
        assertAll(
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.collectFromCloud(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToDiningRoom(Color.BLUE)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.transferStudentToIsland(0,Color.BLUE)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.moveMN(1)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.buyCharacterCard(0)),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.setCardParameters(new Parameters())),
                () -> assertThrowsExactly(IllegalActionException.class, () -> controller.activateCard())
        );
    }

    @Test
    public void testDefinePlayOrder() {
        matchInfo.removePlayer();
        matchInfo.removePlayer();

        game.playCard(0, Card.CARD_1);
        game.playCard(1, Card.CARD_5);
        game.playCard(2, Card.CARD_3);

        controller.definePlayOrder();

        assertEquals(0, matchInfo.getCurrentPlayerID());
        matchInfo.removePlayer();

        assertEquals(2, matchInfo.getCurrentPlayerID());
        matchInfo.removePlayer();

        assertEquals(1, matchInfo.getCurrentPlayerID());
        matchInfo.removePlayer();
    }

    @Test
    public void testPlayCard() {
        try {
            controller.playCard(1); //Plays CARD_2
        } catch (CardUsedException e) {
            e.printStackTrace();
        }
        Player player = Game.getInstance().getPlayerByID(0);
        assertEquals(Card.CARD_2, player.getSelectedCard());
    }

    @Test
    public void testPlayCard_invalidID() {
        assertThrowsExactly(BadParametersException.class, () -> controller.playCard(-1));
        assertThrowsExactly(BadParametersException.class, () -> controller.playCard(999));
    }

    @Test
    public void testPlayCard_alreadyUsed() {
        try {
            controller.playCard(0);
        } catch (CardUsedException e) {
            e.printStackTrace();
        }
        matchInfo.removePlayer();

        assertThrowsExactly(CardUsedException.class, () -> controller.playCard(0));
    }

    @Test
    public void testPlayCard_alreadyUsed_onlyAvailable() {
        for(int i = 0; i < 9; i++){
            game.playCard(1, Card.values()[i]);
        }

        try {
            controller.playCard(9);
        } catch (CardUsedException e) {
            e.printStackTrace();
        }
        matchInfo.removePlayer();

        assertDoesNotThrow( () -> controller.playCard(9) );
        assertEquals(Card.CARD_10, game.getPlayerByID(1).getSelectedCard());
    }
}
