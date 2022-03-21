package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class Cloud {

    private final StudentGroup students;
    private boolean available = true;
    private final int capacity;

    public Cloud(int capacity){
        students = new StudentGroup();
        this.capacity = capacity;
    }

    public void collect(StudentGroup s){
        /*
        * TODO transferAll
        * */
        available = false;
    }

    public void replenish(){
        /*
        * Get from StudentBag
        * */
        available = true;
    }

    public StudentGroup getStudents(){
        return students;
    }

    public boolean isAvailable(){
        return available;
    }
}
