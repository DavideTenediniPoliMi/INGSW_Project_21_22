package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TurnState;

public class StudentsStateController extends CharacterCardPlayableStateController{
    public StudentsStateController(RoundStateController oldState) {
        super(oldState, TurnState.STUDENTS);
    }

    @Override
    public void transferStudentToIsland(int islandIndex, Color c) {
        // TODO
    }

    @Override
    public void transferStudentToDiningRoom(int playerID, Color c) {
        // TODO
    }
}
