package it.polimi.ingsw.model.helpers;

public class StudentBag {
    private final StudentGroup students;
    private static StudentBag instance;

    private StudentBag() {
        students = new StudentGroup();
        //TODO ADD ALL THE STUDENTS
    }

    public static StudentBag getBag(){
        if( instance == null){
            instance = new StudentBag();
        }
        return instance;
    }

    public StudentGroup drawStudents(int amount) {
        // TODO
        return students;
    }

    public void putBack(StudentGroup students){

    }
}
