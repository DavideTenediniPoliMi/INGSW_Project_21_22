package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentBag {
    private StudentGroup students;
    private static StudentBag instance;

    private StudentBag() {
        students = new StudentGroup();
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

    public StudentGroup drawStudents() {
        // TODO
        return students;
    }
}
