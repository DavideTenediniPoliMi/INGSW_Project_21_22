package it.polimi.ingsw;

public class StudentBag {
    private StudentCounter students;

    public StudentBag() {
        students = new StudentCounter();
        for(Color c: Color.values()) {
            students.addByColor(c, 24); // TODO make 24 a constant in Rules
        }
    }

    public StudentCounter drawStudents() {
        // TODO
        return students;
    }
}
