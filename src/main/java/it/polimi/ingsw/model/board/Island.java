package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.utils.Serializable;

/**
 * Abstract class corresponding to the Island entity
 */
public abstract class Island implements Serializable {
    private StudentGroup students = new StudentGroup();
    private TowerColor teamColor;
    private boolean motherNature;

    /**
     * Adds specified students to this <code>Island</code>
     *
     * @param toAdd the <code>StudentGroup</code> to add
     */
    protected void addStudents(StudentGroup toAdd) {
        toAdd.transferAllTo(students);
    }

    /**
     * Returns the color of the last team that conquered this <code>Island</code>
     *
     * @return <code>TowerColor</code> of the team that conquered this <code>Island</code>
     */
    public TowerColor getTeamColor() {
        return teamColor;
    }

    /**
     * Sets the color of the team that conquered this <code>Island</code>
     *
     * @param teamColor the color of the team
     */
    private void setTeamColor(TowerColor teamColor) {
        this.teamColor = teamColor;
    }

    /**
     * Returns whether Mother Nature is on this <code>Island</code>
     *
     * @return <code>true</code> if Mother Nature is on this <code>Island</code>
     */
    public boolean isMotherNatureOnIsland() {
        return motherNature;
    }

    /**
     * Sets whether Mother Nature is on this <code>Island</code>
     *
     * @param motherNature the flag specifying if Mother Nature is on this <code>Island</code>
     */
    protected void setMotherNatureTo(boolean motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * Returns the amount of students on this <code>Island</code> of the specified <code>Color</code>
     *
     * @param c the <code>Color</code> of the students
     * @return Amount of students of speficied <code>Color</code>
     */
    public int getNumStudentsByColor(Color c) {
        return students.getByColor(c);
    }

    /**
     * Conquers this <code>Island</code> with the specified team color
     *
     * @param teamColor the team conquering this <code>Island</code>
     */
    protected void conquerIsland(TowerColor teamColor) {
        setTeamColor(teamColor);
    }

    /**
     * Returns the amount of Islands (>1 if MultiIsland)
     *
     * @return Amount of islands
     */
    public abstract int getNumIslands();

    @Override
    public void deserialize(JsonObject jsonObject) {
        if(students == null)
            students = new StudentGroup();
        students.deserialize(jsonObject.get("students").getAsJsonObject());
        if(jsonObject.has("teamColor"))
            teamColor = TowerColor.valueOf(jsonObject.get("teamColor").getAsString());
        motherNature = jsonObject.get("motherNature").getAsBoolean();
    }
}
