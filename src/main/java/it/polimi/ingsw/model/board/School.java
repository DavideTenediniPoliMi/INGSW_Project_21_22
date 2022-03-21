package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class School {
    private final Player owner;
    private final StudentGroup entrance = new StudentGroup();
    private final StudentGroup diningRoom = new StudentGroup();
    private int numTowers;

    public School(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public int getNumTowers(){
        return numTowers;
    }

    protected void removeFromEntrance(StudentGroup students){
        StudentGroup temp = new StudentGroup();
        entrance.transferTo(temp, students);
    }

    protected void addToEntrance(StudentGroup students){
        students.transferAllTo(entrance);
    }

    protected void removeFromDiningRoom(StudentGroup students){
        StudentGroup temp = new StudentGroup();
        diningRoom.transferTo(temp, students);
    }

    protected void addFromDiningRoom(StudentGroup students){
        students.transferAllTo(diningRoom);
    }

    protected void takeTowers(int amount) {
        numTowers -= amount;
    }

    protected void giveTowers(int amount) {
        numTowers += amount;
    }
}
