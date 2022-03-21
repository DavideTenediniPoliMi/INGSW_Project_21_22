package it.polimi.ingsw.model.board;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Island> islands = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private ProfessorTracker professorOwners;
    private int numCoinsLeft;

    public Board() {
    }
}