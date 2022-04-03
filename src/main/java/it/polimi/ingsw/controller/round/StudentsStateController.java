package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.Parameters;

public class StudentsStateController extends CharacterCardPlayableStateController{
    public StudentsStateController(RoundStateController oldState) {
        super(oldState);
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
