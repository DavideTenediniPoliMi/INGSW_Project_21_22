package it.polimi.ingsw;

public class School {
    private int numTowers;
    private final StudentHolder entrance;
    private final StudentHolder diningRoom;

    public School(int numTowers){
        this.numTowers = numTowers;
        this.entrance = new StudentHolder();   // from rules
        this.diningRoom = new StudentHolder();
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public StudentHolder getEntrance() {
        return entrance;
    }

    public StudentHolder getDiningRoom() {
        return diningRoom;
    }
}
