package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.helpers.ProfessorTracker;

import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Island> islands = new ArrayList<>();
    List<Cloud> clouds = new ArrayList<>();
    List<CharacterCard> characterCards = new ArrayList<>();
    ProfessorTracker professorOwners;

    public Board(int numPlayers, boolean isExpertModeActive) {
        // TODO
    }

    public List<Island> getIslands() {
        return new ArrayList<>(islands);
    }

    public List<Cloud> getClouds() {
        return new ArrayList<>(clouds);
    }

    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }

    public ProfessorTracker getProfessorOwners() {
        return professorOwners;
    }

    public void fillIslands() {}
}