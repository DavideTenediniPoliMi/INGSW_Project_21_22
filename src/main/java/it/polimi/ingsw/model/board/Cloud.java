package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class Cloud {
    private final StudentGroup students = new StudentGroup();
    private boolean available;

    public Cloud() {
    }

    public StudentGroup getStudents() {
        return (StudentGroup) students.clone();
    }

    private void setAvailableTo(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    protected void refillCloud(StudentGroup toAdd) {
        toAdd.transferAllTo(students);
        setAvailableTo(true);
    }

    protected StudentGroup collectStudents() {
        StudentGroup temp = new StudentGroup();
        students.transferAllTo(temp);

        setAvailableTo(false);

        return temp;
    }
}
