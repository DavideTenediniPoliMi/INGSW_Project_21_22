package it.polimi.ingsw.model.helpers;

import com.google.gson.*;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class representing a bag of students. Used to implement random student drawing.
 */
public class StudentBag implements Serializable {
    private final List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values().clone()));
    private final StudentGroup students = new StudentGroup();
    private final Random rand = new Random();
    private int studentsLeft;
    private boolean empty;

    /**
     * Creates a <code>StudentBag</code> with the specified amount of students for each color. Parametrized constructor
     * taking an amount of students by color.
     *
     * @param numStudentsByColor the amount of students to have for each color
     */
    public StudentBag(int numStudentsByColor) {
        if(numStudentsByColor == 0) {
            availableColors.clear();
            empty = true;
        }

        studentsLeft = Color.NUM_COLORS * numStudentsByColor;

        for(Color c : Color.values()){
            students.addByColor(c, numStudentsByColor);
        }
    }

    /**
     * Returns whether this <code>StudentBag</code> is empty.
     *
     * @return <code>true</code> if this <code>StudentBag</code> is empty.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Draws randomly the amount of students specified from this <code>StudentBag</code>.
     *
     * @param amount the amount of students to draw
     * @return A <code>StudentGroup</code> containing
     */
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

    /**
     * Randomly draws a single student from this <code>StudentBag</code>.
     *
     * @param drawnStudents the <code>StudentGroup</code> containing the students drawn so far
     * @return <code>true</code> if the drawing was successful, <code>false</code> if this <code>StudentBag</code> is
     * empty
     */
    private boolean drawStudent(StudentGroup drawnStudents){
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

    /**
     * Puts the specified students back into this <code>StudentBag</code>.
     *
     * @param toReturn the students to return to this <code>StudentBag</code>
     */
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

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("availableColors", gson.toJsonTree(availableColors));
        jsonObject.add("students", students.serialize());
        jsonObject.add("studentsLeft", new JsonPrimitive(studentsLeft));
        jsonObject.add("empty", new JsonPrimitive(empty));

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        availableColors.clear();
        JsonArray colors = jsonObject.getAsJsonArray("availableColors");
        for(JsonElement entry : colors) {
            Color color = Color.valueOf(entry.getAsString());
            availableColors.add(color);
        }

        students.deserialize(jsonObject.get("students").getAsJsonObject());
        studentsLeft = jsonObject.get("studentsLeft").getAsInt();
        empty = jsonObject.get("empty").getAsBoolean();
    }
}
