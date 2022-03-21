package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class Cloud {

    private final StudentGroup students = new StudentGroup();
    private boolean available = true;

    public Cloud(){
    }

    public boolean isAvailable() {
        return available;
    }

    protected void refillCloud(StudentGroup students) {

    }

    protected StudentGroup collectStudents() {
        return new StudentGroup();
    }
}
