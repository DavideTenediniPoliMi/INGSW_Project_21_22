package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class corresponding to the Island entity
 */
public abstract class Island implements Serializable, Printable<List<String>> {
    private final StudentGroup students = new StudentGroup();
    private TowerColor teamColor;
    private boolean motherNature;
    private boolean startingZero;

    public boolean isStartingZero() {
        return startingZero;
    }

    public void setStartingZero(boolean startingZero) {
        this.startingZero = startingZero;
    }

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

    /**
     * Returns the StudentGroup inside this Island
     *
     * @return the students inside this Island
     */
    public StudentGroup getStudents() {
        return (StudentGroup) students.clone();
    }

    /**
     * Return the island with a specific internal relative index
     *
     * @param index the relative index of the requested island
     * @return the index-th island inside this island
     */
    public abstract Island getIslandOfRelativeIndex(int index);

    public void markBridges(List<Boolean> bridges, int startingIndex) {}

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        if(jsonObject.has("students"))
            students.deserialize(jsonObject.get("students").getAsJsonObject());

        if(jsonObject.has("teamColor"))
            teamColor = TowerColor.valueOf(jsonObject.get("teamColor").getAsString());
        else
            teamColor = null;
        motherNature = jsonObject.get("motherNature").getAsBoolean();
        startingZero = jsonObject.get("startingZero").getAsBoolean();
    }

    @Override
    public List<String> print(boolean... params) {
        if(params.length < 4) {
            return new ArrayList<>();
        }
        StringBuilder islandBuilder = new StringBuilder();
        List<String> islandString = new ArrayList<>();

        boolean bridge_N = params[0];
        boolean bridge_E = params[1];
        boolean bridge_S = params[2];
        boolean bridge_W = params[3];

        //width: 23 (For studentGroup)

        islandBuilder.append("┌───────");
        if(bridge_N) {
            islandBuilder.append("┘     └");
        } else {
            islandBuilder.append("───────");
        }
        islandBuilder.append("───────┐");
        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        //First row: Conqueror team info
        int offset = 12 - Game.getInstance().getBoard().getStartingZeroRealIndex();
        int index = (Game.getInstance().getIndexOfIsland(this) + offset ) % 12;

        islandBuilder.append("│" + index + (index < 10 ? " " : "") + "          Team: ")
                    .append(getTeamColor() == null ? "   " : getTeamColor().print())
                    .append("│");

        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        if(bridge_W) {
            islandBuilder.append("┘");
        } else {
            islandBuilder.append("│");
        }

        islandBuilder.append(" ".repeat(21));

        if(bridge_E) {
            islandBuilder.append("└");
        } else {
            islandBuilder.append("│");
        }
        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        //Second row: Students info

        if(bridge_W) {
            islandBuilder.append(" ");
        } else {
            islandBuilder.append("│");
        }

        String studentsPure = students.print(false);
        for(AnsiCodes code : AnsiCodes.values()) {
            studentsPure = studentsPure.replace(code.toString(), "");
        }

        islandBuilder.append(students.print(false))
                    .append(" ".repeat(21 - studentsPure.length()))
                    .append(AnsiCodes.RESET);

        if(bridge_E) {
            islandBuilder.append(" ");
        } else {
            islandBuilder.append("│");
        }
        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        //Third row: empty

        if(bridge_W) {
            islandBuilder.append("┐");
        } else {
            islandBuilder.append("│");
        }

        islandBuilder.append(" ".repeat(21));

        if(bridge_E) {
            islandBuilder.append("┌");
        } else {
            islandBuilder.append("│");
        }

        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        //Fourth row: Mother nature

        islandBuilder.append("│").append(" ".repeat(9));

        if(motherNature) {
            islandBuilder.append(AnsiCodes.BROWN_BACKGROUND).append(" M ").append(AnsiCodes.RESET);
        } else {
            islandBuilder.append(" ".repeat(3));
        }

        islandBuilder.append(" ".repeat(9))
                    .append(AnsiCodes.RESET).append("│");

        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        //Fourth row: Closing the box

        islandBuilder.append("└───────");

        if(bridge_S) {
            islandBuilder.append("┐     ┌");
        } else {
            islandBuilder.append("───────");
        }
        islandBuilder.append("───────┘");
        islandString.add(islandBuilder.toString());
        islandBuilder.setLength(0);

        return islandString;
    }
}
