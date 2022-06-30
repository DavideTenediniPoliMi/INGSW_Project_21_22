package it.polimi.ingsw.model.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.List;

/**
 * Class representing multiple islands merged together.
 */
public class MultiIsland extends Island {
    private Island leftIsland;
    private Island rightIsland;

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
     * Returns the amount of simple islands contained in this <code>MultiIsland</code>
     *
     * @return Amount of islands
     */
    @Override
    public int getNumIslands() {
        return leftIsland.getNumIslands() + rightIsland.getNumIslands();
    }

    @Override
    public Island getIslandOfRelativeIndex(int index) {
        if(leftIsland.getNumIslands() > index) {
            return leftIsland.getIslandOfRelativeIndex(index);
        }

        return rightIsland.getIslandOfRelativeIndex(index - leftIsland.getNumIslands());
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        JsonObject left = new JsonObject();
        JsonObject right = new JsonObject();

        if(isMulti(jsonObject)) {
            left = jsonObject.get("leftIsland").getAsJsonObject();
            right = jsonObject.get("rightIsland").getAsJsonObject();
        }

        if(isMulti(left)) {
            Island i1 = new SimpleIsland();
            Island i2 = new SimpleIsland();

            leftIsland = new MultiIsland(i1, i2);
        }

        if(isMulti(right)) {
            Island i1 = new SimpleIsland();
            Island i2 = new SimpleIsland();

            rightIsland = new MultiIsland(i1, i2);
        }

        leftIsland.deserialize(left);
        rightIsland.deserialize(right);
        super.conquerIsland(TowerColor.valueOf(jsonObject.get("teamColor").getAsString()));
        super.setMotherNatureTo(jsonObject.get("motherNature").getAsBoolean());
    }

    private boolean isMulti(JsonObject island) {
        return island.has("leftIsland") && island.has("rightIsland");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Island)) {
            return false;
        }
        Island is = (Island) obj;

        boolean equalsLft = leftIsland.equals(is);
        boolean equalsRgt = rightIsland.equals(is);

        return equalsLft || equalsRgt || (this == is);
    }

    @Override
    public void markBridges(List<Boolean> bridges, int startingIndex) {
        leftIsland.markBridges(bridges, startingIndex);
        rightIsland.markBridges(bridges, startingIndex + leftIsland.getNumIslands());
        bridges.set(startingIndex + leftIsland.getNumIslands() - 1, Boolean.TRUE);
    }
}
