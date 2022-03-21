package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class School {
    private int numTowers;
    private final StudentGroup entrance;
    private final StudentGroup diningRoom;

    public School(int numTowers){
        this.numTowers = numTowers;
        this.entrance = new StudentGroup();   // from rules
        this.diningRoom = new StudentGroup();
    }

    public int getNumTowers() {
        return numTowers;
    }

    public void setNumTowers(int numTowers) {
        this.numTowers = numTowers;
    }

    public StudentGroup getEntrance() {
        return entrance;
    }

    public StudentGroup getDiningRoom() {
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
