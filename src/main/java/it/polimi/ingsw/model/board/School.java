package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class School {
    private Player owner;
    private final StudentGroup entrance;
    private final StudentGroup diningRoom;
    private int numTowers;

    public School(){
        this.entrance = new StudentGroup();
        this.diningRoom = new StudentGroup();
        this.numTowers = 0;
    }

    protected void removeFromEntrance(StudentGroup students){

    }

    protected void addToEntrance(StudentGroup students){

    }

    protected void removeFromDiningRoom(StudentGroup students){

    }

    protected void addFromDiningRoom(StudentGroup students){

    }

    protected void setOwner(Player owner){
        this.owner = owner;
    }
    public int getNumTowers(){
        return numTowers;
    }
}
