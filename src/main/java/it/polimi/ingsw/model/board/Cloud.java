package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentHolder;

public class Cloud {

    private final StudentHolder students;
    private boolean available = true;
    private final int capacity;

    public Cloud(int capacity){
        students = new StudentHolder();
        this.capacity = capacity;
    }

    public void collect(StudentHolder s){
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

    public StudentHolder getStudents(){
        return students;
    }

    public boolean isAvailable(){
        return available;
    }
}
