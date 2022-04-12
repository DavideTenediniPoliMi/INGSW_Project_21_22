package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class School {
    private final Player owner;
    private int numTowers;
    private final StudentGroup entrance = new StudentGroup();
    private final StudentGroup diningRoom = new StudentGroup();

    public School(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public int getNumTowers(){
        return numTowers;
    }

    protected void removeTowers(int amount) {
        numTowers = Math.max(numTowers - amount, 0);
    }

    protected void addTowers(int amount) {
        numTowers += amount;
    }

    public int getNumStudentsInDiningRoomByColor(Color c) {
        return diningRoom.getByColor(c);
    }

    public int getNumStudentsInEntranceByColor(Color c) { return entrance.getByColor(c); }

    protected void removeFromEntrance(StudentGroup students) {
        StudentGroup temp = new StudentGroup();
        entrance.transferTo(temp, students);
    }

    protected void addToEntrance(StudentGroup students) {
        students.transferAllTo(entrance);
    }

    protected void removeFromDiningRoom(StudentGroup students) {
        StudentGroup temp = new StudentGroup();
        diningRoom.transferTo(temp, students);
    }

    protected void addToDiningRoom(StudentGroup students) {
        students.transferAllTo(diningRoom);
    }
}
