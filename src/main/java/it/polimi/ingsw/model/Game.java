package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private final List<Player> players = new ArrayList<>();
    private int numPlayersStillToAct;

    public Game(int numPlayers, boolean expertModeActive) {
        this.numPlayersStillToAct = numPlayers;
        // TODO
    }

    // TODO
    public void startGame() {}

    public void nextRound() {}

    private void nextTurn () {}

    public void endGame() {}

    private void decidePlayingOrder() {}
}
