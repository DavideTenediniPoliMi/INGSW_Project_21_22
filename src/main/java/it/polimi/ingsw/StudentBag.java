package it.polimi.ingsw;

public class StudentBag {
    private StudentHolder students;
    private static StudentBag instance;

    private StudentBag() {
        students = new StudentHolder();
        for(Color c: Color.values()) {
            students.addByColor(c, 24); // TODO make 24 a constant in Rules
        }
    }

    public static StudentBag getBag(){
        if( instance == null){
            instance = new StudentBag();
        }
        return instance;
    }

    public StudentHolder drawStudents() {
        // TODO
        return students;
    }
}
