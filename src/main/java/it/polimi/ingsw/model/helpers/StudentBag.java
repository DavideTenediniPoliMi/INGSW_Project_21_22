package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.Random;

public class StudentBag {
    private final int NUM_STARTING_STUDENT_BY_COLOR = 24;

    private final StudentGroup students;
    private final Random rand;

    public StudentBag() {
        students = new StudentGroup();
        rand = new Random();

        for(Color c : Color.values()){
            students.addByColor(c, NUM_STARTING_STUDENT_BY_COLOR);
        }
    }

    public StudentGroup drawStudents(int amount) {
        int totDrawn = 0;
        StudentGroup temp = new StudentGroup();
        StudentGroup drawnStudents = new StudentGroup();

        while(totDrawn < amount) {
            Color chosenColor = Color.values()[rand.nextInt(Color.NUM_COLORS)];
            int numMaxDrawable = Math.min(students.getByColor(chosenColor), amount - totDrawn);
            int drawn = rand.nextInt(numMaxDrawable) + 1;

            temp.addByColor(chosenColor, drawn);

            totDrawn += drawn;
        }

        students.transferTo(drawnStudents, temp);
        return students;
    }

    public void putStudentsBack(StudentGroup toReturn){
        toReturn.transferAllTo(students);
    }
}
