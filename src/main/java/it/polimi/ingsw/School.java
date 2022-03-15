package it.polimi.ingsw;

public class School {
    private int numTowers;
    private final StudentCounter entrance;
    private final StudentCounter diningRoom;

    public School(int numTowers){
        this.numTowers = numTowers;
        this.entrance = new StudentCounter();   // from rules
        this.diningRoom = new StudentCounter();
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public StudentCounter getEntrance() {
        return entrance;
    }

    public StudentCounter getDiningRoom() {
        return diningRoom;
    }
}
