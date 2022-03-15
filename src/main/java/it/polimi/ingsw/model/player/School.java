package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.helpers.StudentHolder;

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

    public void addTowers(int numTowers){
        this.numTowers += numTowers;
    }

    public void addTower(){
        addTowers(1);
    }

    public void removeTowers(int numTowers){
        this.numTowers -= numTowers;
    }

    public void removeTower(){
        removeTowers(1);
    }
}
