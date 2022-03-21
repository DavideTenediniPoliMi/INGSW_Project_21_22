package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.Random;

public class StudentBag {
    private final int NUM_STARTING_STUDENT_BY_COLOR = 24;
    private final StudentGroup students;
    private final Random r;
    private static StudentBag instance;

    private StudentBag() {
        students = new StudentGroup();
        r = new Random();

        for(Color c : Color.values()){
            students.addByColor(c, NUM_STARTING_STUDENT_BY_COLOR);
        }
    }

    public static StudentBag getBag(){
        if( instance == null){
            instance = new StudentBag();
        }
        return instance;
    }

    public StudentGroup drawStudents(int amt) {
        int drawn;
        Color color;
        StudentGroup tmp = new StudentGroup();
        for(int x = 0; x < amt; x += drawn){
            color = Color.values()[r.nextInt(Color.NUM_COLORS)];
            drawn = r.nextInt(Math.min(students.getByColor(color), amt-x)) + 1;

            tmp.addByColor(color, drawn);
        }

        StudentGroup drawnStudents = new StudentGroup();
        students.transferTo(drawnStudents, tmp);
        return students;
    }

    public void putBack(StudentGroup toAdd){
        toAdd.transferAllTo(students);
    }
}
