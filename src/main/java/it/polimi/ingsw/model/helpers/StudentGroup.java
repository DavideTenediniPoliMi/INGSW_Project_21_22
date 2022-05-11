package it.polimi.ingsw.model.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;
import it.polimi.ingsw.view.CLI.AnsiCodes;

import java.util.Arrays;

/**
 * Class representing a group (container) of students of the 5 colors
 */
public class StudentGroup implements Cloneable, Serializable, Printable {
    private final int[] students;

    /**
     * Constructor creating an empty StudentGroup
     */
    public StudentGroup() {
        this.students = new int[Color.NUM_COLORS];
    }

    /**
     * Constructor creating a <code>StudentGroup</code> with the specified amount of students of the specified
     * <code>Color</code>
     *
     * @param c the <code>Color</code> of the initial students
     * @param amount the amount of initial students
     */
    public StudentGroup(Color c, int amount) {
        this();
        addByColor(c, amount);
    }

    /**
     * Returns the amount of students of the specified <code>Color</code>
     *
     * @param c the <code>Color</code> of the students
     * @return Amount of students with specified <code>Color</code>
     * @see Color
     */
    public int getByColor(Color c) {
        return students[c.ordinal()];
    }

    /**
     * Add the specified amount of students of the specified <code>Color</code> to this <code>StudentGroup</code>
     *
     * @param c the <code>Color</code> of the students
     * @param amount the amount of students to add
     */
    public void addByColor(Color c, int amount) {
        students[c.ordinal()] += amount;
    }

    /**
     * Returns whether the specified StudentGroup is contained in this <code>StudentGroup</code>
     *
     * @param students the <code>StudentGroup</code> to check.
     * @return <code>true</code> if this <code>StudentGroup</code> contains the specified one.
     */
    public boolean contains(StudentGroup students) {
        for(Color c : Color.values()) {
            if(students.getByColor(c) > getByColor(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Transfer all the students in this <code>StudentGroup</code> into the specified recipient <code>StudentGroup</code>
     *
     * @param recipient the recipient <code>StudentGroup</code> of this transaction
     */
    public void transferAllTo(StudentGroup recipient) {
        transferTo(recipient, this);
    }

    /**
     * Transfer some specified amounts of students from this <code>StudentGroup</code> into the specified recipient
     * <code>StudentGroup</code>. The <code>StudentGroup</code> toTransfer indicates which students are involved in this
     * transaction
     * 
     * @param recipient the recipient <code>StudentGroup</code> of this transaction
     * @param toTransfer the students to be transferred from this <code>StudentGroup</code> to the recipient
     */
    public void transferTo(StudentGroup recipient, StudentGroup toTransfer) {
        for(Color c : Color.values()) {
            int amount = toTransfer.getByColor(c);

            if(getByColor(c) >= amount) {
                students[c.ordinal()] -= amount;
                recipient.addByColor(c, amount);
            }
        }
    }

    @Override
    public Object clone() {
        StudentGroup temp = new StudentGroup();

        System.arraycopy(students, 0, temp.students, 0, Color.NUM_COLORS);

        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGroup that = (StudentGroup) o;
        return Arrays.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(students);
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        JsonArray studentsJson = jsonObject.getAsJsonArray("students");

        for(int c = 0; c < Color.NUM_COLORS; c++) {
            students[c] = studentsJson.get(c).getAsInt();
        }
    }

    @Override
    public String print() {
        StringBuilder studentsString = new StringBuilder(" ");

        for(Color color : Color.values()) {
            if(students[color.ordinal()] > 0) {
                studentsString.append(AnsiCodes.getColor(color)).append(" ").append(students[color.ordinal()]).append(" ").append(AnsiCodes.RESET).append(" ");
            }
        }

        return studentsString.toString();
    }
}
