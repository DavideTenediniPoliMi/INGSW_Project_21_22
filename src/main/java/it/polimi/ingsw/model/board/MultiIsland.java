package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Class representing multiple islands merged together.
 */
public class MultiIsland extends Island {
    private final Island leftIsland;
    private final Island rightIsland;

    /**
     * Sole constructor for MultiIsland, merges two generic islands
     *
     * @param leftIsland the island on the left
     * @param rightIsland the island on the right
     */
    public MultiIsland(Island leftIsland, Island rightIsland) {
        this.leftIsland = leftIsland;
        this.rightIsland = rightIsland;

        super.conquerIsland(leftIsland.getTeamColor());
        setMotherNatureTo(true);
    }

    /**
     * Returns the number of students of the specified color both in this <code>Island</code> and its "children" islands
     *
     * @param c the <code>Color</code> of the students
     * @return Amount of students of specified color in this Island
     */
    @Override
    public int getNumStudentsByColor(Color c) {
        return super.getNumStudentsByColor(c)
                + leftIsland.getNumStudentsByColor(c)
                + rightIsland.getNumStudentsByColor(c);
    }

    /**
     * Conquers this <code>Island</code> and its children
     *
     * @param teamColor the team conquering this <code>Island</code>
     */
    @Override
    protected void conquerIsland(TowerColor teamColor) {
        super.conquerIsland(teamColor);

        leftIsland.conquerIsland(teamColor);
        rightIsland.conquerIsland(teamColor);
    }

    /**
     * Sets whether Mother Nature is on this <code>Island</code>
     *
     * @param motherNature the flag specifying if Mother Nature is on this <code>Island</code>
     */
    @Override
    protected void setMotherNatureTo(boolean motherNature) {
        super.setMotherNatureTo(motherNature);

        leftIsland.setMotherNatureTo(motherNature);
        rightIsland.setMotherNatureTo(motherNature);
    }

    /**
     * Returns the amount of single islands contained in this <code>MultiIsland</code>
     *
     * @return Amount of islands
     */
    @Override
    public int getNumIslands() {
        return leftIsland.getNumIslands() + rightIsland.getNumIslands();
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        leftIsland.deserialize(jsonObject.get("leftIsland").getAsJsonObject());
        rightIsland.deserialize(jsonObject.get("rightIsland").getAsJsonObject());
        super.deserialize(jsonObject);
    }
}
