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
        students.transferAllTo(this.students);
        setAvailableTo(true);
    }

    protected StudentGroup collectStudents() {
        StudentGroup temp = new StudentGroup();
        students.transferAllTo(temp);

        setAvailableTo(false);

        return temp;
    }

    private void setAvailableTo(boolean available) {
        this.available = available;
    }
}
