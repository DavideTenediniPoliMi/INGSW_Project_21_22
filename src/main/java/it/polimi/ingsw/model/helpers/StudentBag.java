package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

public class StudentBag {
    private final StudentGroup students;
    private static StudentBag instance;

    private StudentBag() {
        students = new StudentGroup();
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
