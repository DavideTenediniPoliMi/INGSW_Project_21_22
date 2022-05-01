package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.TurnState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class MatchInfoTest {
    MatchInfo matchInfo;
    GameStatus gs;

    @BeforeEach
    void setUp() {
        matchInfo = MatchInfo.getInstance();
        matchInfo.setSelectedNumPlayer(3);
        matchInfo.setExpertMode(true);
        matchInfo.setNumPlayersConnected(3);
        matchInfo.setStateType(TurnState.PLANNING);
        matchInfo.setGameStatus(GameStatus.IN_GAME);
        gs = GameStatus.IN_GAME;
    }

    @AfterEach
    void tearDown() {
        MatchInfo.resetInstance();
        matchInfo = null;
        gs = null;
    }

    @Test
    void getSelectedNumPlayer() {
        assertEquals(3, matchInfo.getSelectedNumPlayer());
    }

    @Test
    void getGameStatus() {
        assertEquals(GameStatus.IN_GAME, matchInfo.getGameStatus());
    }

    @Test
    void isExpertMode() {
        assertTrue(matchInfo.isExpertMode());
    }

    @Test
    void getMaxMovableStudents() {
        assertEquals(4, matchInfo.getMaxMovableStudents());
        matchInfo.setSelectedNumPlayer(2);
        assertEquals(3, matchInfo.getMaxMovableStudents());
        matchInfo.setSelectedNumPlayer(4);
        assertEquals(3, matchInfo.getMaxMovableStudents());
    }

    @Test
    void getMaxTowers() {
        assertEquals(6, matchInfo.getMaxTowers());
        matchInfo.setSelectedNumPlayer(2);
        assertEquals(8, matchInfo.getMaxTowers());
        matchInfo.setSelectedNumPlayer(4);
        assertEquals(8, matchInfo.getMaxTowers());
    }

    @Test
    void getNumPlayersConnected() {
        assertEquals(3, matchInfo.getNumPlayersConnected());
    }

    @Test
    void getInitialNumStudents() {
        assertEquals(9, matchInfo.getInitialNumStudents());
    }

    @Test
    void getStateType() {
        assertEquals(TurnState.PLANNING, matchInfo.getStateType());
    }

    @Test
    void getNumMovedStudents() {
        assertEquals(0, matchInfo.getNumMovedStudents());
    }

    @Test
    void resetNumMovedStudents() {
        matchInfo.studentWasMoved();
        matchInfo.resetNumMovedStudents();
        assertEquals(0, matchInfo.getNumMovedStudents());
    }

    @Test
    void studentWasMoved() {
        matchInfo.studentWasMoved();
        assertEquals(1, matchInfo.getNumMovedStudents());
        matchInfo.studentWasMoved();
        matchInfo.studentWasMoved();
        assertEquals(3, matchInfo.getNumMovedStudents());
    }

    @Test
    void getNumPlayersStillToAct() {
        assertEquals(0, matchInfo.getNumPlayersStillToAct());
        matchInfo.addPlayer(0);
        matchInfo.addPlayer(1);
        assertEquals(2, matchInfo.getNumPlayersStillToAct());
    }

    @Test
    void getCurrentPlayerID() {
        matchInfo.addPlayer(0);
        assertEquals(0, matchInfo.getCurrentPlayerID());
    }

    @Test
    void isGameOver() {
        assertFalse(matchInfo.isGameOver());
        matchInfo.declareWinner(TowerColor.WHITE);
        assertTrue(matchInfo.isGameOver());
    }

    @Test
    void isGameTied() {
        List<TowerColor> teams = new ArrayList<>();
        teams.add(TowerColor.WHITE);
        teams.add(TowerColor.BLACK);

        assertFalse(matchInfo.isGameTied());
        matchInfo.declareTie(teams);
        assertTrue(matchInfo.isGameTied());
    }

    @Test
    void getWinners() {
        List<TowerColor> teams = new ArrayList<>();
        teams.add(TowerColor.WHITE);
        teams.add(TowerColor.BLACK);

        matchInfo.declareTie(teams);

        assertEquals(teams, matchInfo.getWinners());
    }

    @Test
    void queueTest() {
        Queue<Integer> test = new LinkedList<>();
        test.add(0);
        matchInfo.addPlayer(0);
        test.add(1);
        matchInfo.addPlayer(1);
        assertEquals(test, matchInfo.getPlayOrder());
        matchInfo.removePlayer();
        test.remove();
        assertEquals(test, matchInfo.getPlayOrder());
    }
}