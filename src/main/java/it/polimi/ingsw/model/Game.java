package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private final List<Player> players = new ArrayList<>();
    //add cards
    private int numPlayersStillToAct;

    public Game() {
    }
}
