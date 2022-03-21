package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class School {
    private Player owner;
    private int numTowers;
    private final StudentGroup entrance;
    private final StudentGroup diningRoom;

    public School(){
        this.entrance = new StudentGroup();
        this.diningRoom = new StudentGroup();
    }
}
