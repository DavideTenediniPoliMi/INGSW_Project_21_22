package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Printable;

import java.util.ArrayList;
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
        super.conquerIsland(leftIsland.getTeamColor());
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

    @Override
    public List<String> print(boolean... params) {
        List<String> islandBuilder = new ArrayList<>();
        int leftRealIndex = 0;
        Board board = Game.getInstance().getBoard();

        for(int i = 0; i < board.getNumIslands(); i++) {
            if(board.getIslandAt(i).equals(this)) {
                break;
            }
            leftRealIndex += board.getIslandAt(i).getNumIslands();
        }

        islandBuilder.addAll(leftIsland.print(calculateOpenings(leftRealIndex, true, params)));

        int rightRealIndex = leftRealIndex + leftIsland.getNumIslands();
        islandBuilder.addAll(rightIsland.print(calculateOpenings(rightRealIndex, false, params)));

        return islandBuilder;
    }

    private boolean[] calculateOpenings(int realIndex, boolean isLeft, boolean...params) {
        if(0 <= realIndex && realIndex < 4) {
            //Upper row
            if(isLeft) {
                return new boolean[]{
                        params[0], //N
                        true,      //E
                        params[2], //S
                        params[3]  //W
                };
            } else {
                return new boolean[]{
                        params[0],
                        params[1],
                        params[2],
                        true
                };
            }
        } else if(4 <= realIndex && realIndex < 6) {
            //Mid-right
            if(isLeft) {
                return new boolean[]{
                        params[0],
                        params[1],
                        true,
                        params[3]
                };
            } else {
                return new boolean[]{
                        true,
                        params[1],
                        params[2],
                        params[3]
                };
            }
        } else {
            return calculateOpenings(realIndex - 6, !isLeft, params);
        }
    }
}
