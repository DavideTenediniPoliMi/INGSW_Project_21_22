package it.polimi.ingsw.model.helpers;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StudentBag {
    private final int NUM_STARTING_STUDENT_BY_COLOR = 24;
    private List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values().clone()));

    private final StudentGroup students;
    private final Random rand;
    private int studentsLeft;
    private boolean empty;

    public StudentBag() {
        students = new StudentGroup();
        rand = new Random();
        empty = false;
        studentsLeft = Color.NUM_COLORS * NUM_STARTING_STUDENT_BY_COLOR;

        for(Color c : Color.values()){
            students.addByColor(c, NUM_STARTING_STUDENT_BY_COLOR);
        }
    }

    public StudentGroup drawStudents(int amount) {
        int totDrawn = 0;
        boolean bagEmptyFlag = false;
        StudentGroup drawnStudents = new StudentGroup();

        while(totDrawn < amount && !bagEmptyFlag) {
            bagEmptyFlag = drawStudent(drawnStudents);
            totDrawn++;
        }

        if(bagEmptyFlag){
            availableColors.clear();
        }

        return drawnStudents;
    }

    public boolean drawStudent(StudentGroup drawnStudents){
        if(!empty){
            Color chosenColor = availableColors.get(rand.nextInt(availableColors.size()));

            while(students.getByColor(chosenColor) == 0){
                availableColors.remove(chosenColor);
                chosenColor = availableColors.get(rand.nextInt(availableColors.size()));
            }

            students.transferTo(drawnStudents, new StudentGroup(chosenColor, 1));
            studentsLeft--;
            if(studentsLeft == 0){
                availableColors.remove(chosenColor);
                empty = true;
            }
            return false;
        }
        return true;
    }

    public void putStudentsBack(StudentGroup toReturn){
        int num;
        for(Color c : Color.values()){
            num = toReturn.getByColor(c);
            if(num > 0){
                studentsLeft += num;
                if(!availableColors.contains(c)){
                    availableColors.add(c);
                }
            }
        }
        toReturn.transferAllTo(students);
        if(empty){
            empty = false;
        }
    }
}
