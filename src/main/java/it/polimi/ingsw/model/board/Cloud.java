package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class corresponding to the Cloud entity in a Game
 */
public class Cloud implements Serializable, Printable<List<String>> {
    private final StudentGroup students = new StudentGroup();
    private boolean available;

    /**
     * Sole constructor for this class
     */
    public Cloud() {
    }

    /**
     * Returns the students contained in this <code>Cloud</code>
     *
     * @return <code>StudentGroup</code> with the students contained in this <code>Cloud</code>
     */
    public StudentGroup getStudents() {
        return (StudentGroup) students.clone();
    }

    /**
     * Sets the available flag for this <code>Cloud</code>
     *
     * @param available the flag to set
     */
    private void setAvailableTo(boolean available) {
        this.available = available;
    }

    /**
     * Returns whether students can be collected from this <code>Cloud</code>
     *
     * @return Availability of this <code>Cloud</code>.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Refills this <code>Cloud</code> with the specified students and makes it available.
     *
     * @param toAdd the students to add to this <code>Cloud</code>
     */
    protected void refillCloud(StudentGroup toAdd) {
        toAdd.transferAllTo(students);
        setAvailableTo(true);
    }

    /**
     * Collects students contained in this <code>Cloud</code>.
     *
     * @return <code>StudentGroup</code> containing this <code>Cloud</code>'s students.
     */
    protected StudentGroup collectStudents() {
        StudentGroup temp = new StudentGroup();
        students.transferAllTo(temp);

        setAvailableTo(false);

        return temp;
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        setAvailableTo(jsonObject.get("available").getAsBoolean());
        students.deserialize(jsonObject.get("students").getAsJsonObject());
    }

    @Override
    public List<String> print(boolean...params) {
        StringBuilder cloudBuilder = new StringBuilder();
        List<String> cloudString = new ArrayList<>();

        cloudBuilder.append("┌");

        int amt = 0;
        for(Color color : Color.values()) {
            if(students.getByColor(color) > 0) {
                cloudBuilder.append("────");
                amt++;
            }
        }
        cloudBuilder.append("─");
        cloudBuilder.append("┐");
        cloudString.add(cloudBuilder.toString());
        cloudBuilder.setLength(0);

        cloudBuilder.append("│").append(students.print(false)).append("│");
        cloudString.add(cloudBuilder.toString());
        cloudBuilder.setLength(0);

        cloudBuilder.append("└");

        cloudBuilder.append("─".repeat(Math.max(0, amt * 4 + 1)));

        cloudBuilder.append("┘");
        cloudString.add(cloudBuilder.toString());
        cloudBuilder.setLength(0);

        return cloudString;
    }
}
