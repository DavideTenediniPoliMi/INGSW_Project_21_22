package it.polimi.ingsw;

public class Cloud {

    private final StudentCounter students;
    private boolean available = true;
    private final int capacity;

    public Cloud(int capacity){
        students = new StudentCounter();
        this.capacity = capacity;
    }

    public void collect(StudentCounter s){
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

    public StudentCounter getStudents(){
        return students;
    }

    public boolean isAvailable(){
        return available;
    }
}
