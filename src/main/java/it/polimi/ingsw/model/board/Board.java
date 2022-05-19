package it.polimi.ingsw.model.board;

import com.google.gson.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;
import it.polimi.ingsw.utils.StringUtils;

import java.util.*;

/**
 * Class to hold data for every entity of a Game (Islands, Schools, Clouds, Professors).
 */
public class Board implements Serializable, Printable<List<String>> {
    private final int NUM_STARTING_ISLANDS = 12;
    private final int NUM_STARTING_COINS = 20;

    private List<Island> islands = new ArrayList<>();
    private List<Cloud> clouds = new ArrayList<>();
    private List<School> schools = new ArrayList<>();
    private ProfessorTracker professorOwners = new ProfessorTracker();
    private int numCoinsLeft = NUM_STARTING_COINS;

    /**
     * Sole constructor for Board, instantiates the 12 Islands for the game.
     */
    public Board() {
        for(int i = 0; i < NUM_STARTING_ISLANDS; i++) {
            islands.add(new SimpleIsland());
        }
    }

    // ISLAND

    /**
     * Gets the <code>Island</code> at the specified index.
     *
     * @param islandIndex the index of the <code>Island</code> to fetch.
     * @return <code>Island</code> with specified index.
     */
    public Island getIslandAt(int islandIndex) {
        return islands.get(islandIndex);
    }

    /**
     * Returns the list of islands.
     *
     * @return list of <code>Island</code>.
     */
    public List<Island> getIslands() {
        return new ArrayList<>(islands);
    }

    public void setIslands(List<Island> islands) {
        this.islands = new ArrayList<>(islands);
    }

    /**
     * Returns the amount of islands in this game.
     *
     * @return Current amount of islands.
     */
    public int getNumIslands() {
        return islands.size();
    }

    /**
     * Transfers specified students to specified island.
     *
     * @param islandIndex the index of the recipient <code>Island</code>.
     * @param students the <code>StudentGroup</code> to transfer.
     */
    public void addStudentsToIsland(int islandIndex, StudentGroup students) {
        getIslandOfAbsoluteIndex(islandIndex).addStudents(students);
    }

    public Island getIslandOfAbsoluteIndex(int islandIndex) {
        int runningIndex = 0;
        int fixedIndex = 0;

        while(getIslandAt(fixedIndex).getNumIslands() + runningIndex <= islandIndex){
            runningIndex += getIslandAt(fixedIndex).getNumIslands();
            fixedIndex++;
        }

        return getIslandAt(fixedIndex).getIslandOfRelativeIndex(islandIndex - runningIndex);
    }

    /**
     * Conquers the island where Mother Nature is currently on for the specified team.
     *
     * @param teamColor the team conquering the <code>Island</code>.
     */
    public void conquerIsland(TowerColor teamColor) {
        int islandIndex = getMNPosition();
        islands.get(islandIndex).conquerIsland(teamColor);
    }

    /**
     * Merges two islands into one. Called by Game.
     *
     * @param leftIslandIndex the index of the left <code>Island</code>.
     * @param rightIslandIndex the index of the right <code>Island</code>.
     */
    public void mergeIslands(int leftIslandIndex, int rightIslandIndex) {
        Island leftIsland = islands.get(leftIslandIndex);
        Island rightIsland = islands.get(rightIslandIndex);

        Island temp = new MultiIsland(leftIsland, rightIsland);

        islands.remove(rightIslandIndex);

        if(leftIslandIndex > rightIslandIndex) leftIslandIndex --;

        islands.set(leftIslandIndex, temp);
    }

    // MOTHER NATURE

    /**
     * Returns the index of the <code>Island</code> with Mother Nature on.
     *
     * @return Index of the <code>Island</code> with Mother Nature.
     */
    public int getMNPosition() {
        Optional<Island> result = islands.stream()
                .filter(Island::isMotherNatureOnIsland)
                .findAny();

        return islands.indexOf(result.orElse(null));
    }

    /**
     * Places Mother Nature at the specified <code>Island</code>.
     *
     * @param islandIndex the index of the <code>Island</code>.
     */
    public void placeMNAt(int islandIndex) {
        islands.get(islandIndex).setMotherNatureTo(true);
    }

    /**
     * Moves Mother Nature the specified amount of steps (clockwise). Called by Game.
     *
     * @param steps the amount of steps to move.
     */
    public void moveMN(int steps) {
        int currentIslandIndex = getMNPosition();
        islands.get(currentIslandIndex).setMotherNatureTo(false);

        int nextIslandIndex = (currentIslandIndex + steps) % islands.size();
        islands.get(nextIslandIndex).setMotherNatureTo(true);
    }

    // CLOUD

    /**
     * Creates the specified amount of clouds. Called by Game.
     *
     * @param amount the amount of clouds to create.
     */
    public void createClouds(int amount) {
        for(int i = 0; i < amount; i++) {
            clouds.add(new Cloud());
        }
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = new ArrayList<>(clouds);
    }

    /**
     * Collects students from the specified <code>Cloud</code>.
     *
     * @param cloudIndex the index of the <code>Cloud</code>.
     * @return <code>StudentGroup</code> containing the students from the <code>Cloud</code>.
     */
    public StudentGroup collectFromCloud(int cloudIndex) {
        return clouds.get(cloudIndex).collectStudents();
    }

    /**
     * Refills the clouds with the specified amount of students.
     *
     * @param studentAmount the amount of students to fill each <code>Cloud</code> with.
     */
    public void refillClouds(int studentAmount) {
        for(Cloud cloud: clouds) {
            if(cloud.isAvailable()) continue;

            StudentGroup temp = Game.getInstance().drawStudents(studentAmount);
            cloud.refillCloud(temp);
        }
    }

    /**
     * Returns the list of clouds.
     *
     * @return List of <code>Cloud</code>.
     */
    public List<Cloud> getClouds(){
        return new ArrayList<>(clouds);
    }

    // SCHOOL

    /**
     * Adds a new <code>School</code> bound to the specified <code>Player</code>.
     *
     * @param owner the owner of the new <code>School</code>.
     * @see School
     */
    public void addSchool(Player owner) {
        schools.add(new School(owner));
    }

    /**
     * Returns the <code>School</code> bound to the <code>Player</code> with the specified ID. Returns null if no
     * matching <code>Player</code>.
     *
     * @param playerID the ID of the owner of the <code>School</code>.
     * @return Specified player's <code>School</code>.
     */
    public School getSchoolByPlayerID(int playerID) {
        Optional<School> result = schools.stream()
                .filter((school) -> (school.getOwner().getID() == playerID))
                .findAny();

        return result.orElse(null);
    }

    /**
     * Returns the schools.
     *
     * @return List of <code>School</code> of this Game.
     */
    public List<School> getSchools() {
        return new ArrayList<>(schools);
    }

    public void setSchools(List<School> schools) {
        this.schools = new ArrayList<>(schools);
    }

    /**
     * Removes specified students from specified <code>Player</code> ID's school entrance.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param students the students to remove.
     */
    public void removeFromEntranceOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).removeFromEntrance(students);
    }

    /**
     * Adds specified students to specified <code>Player</code> ID's school entrance.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param students the students to add.
     */
    public void addToEntranceOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).addToEntrance(students);
    }

    /**
     * Removes specified students from specified <code>Player</code> ID's school dining room.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param students the students to remove.
     */
    public void removeFromDiningRoomOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).removeFromDiningRoom(students);
    }

    /**
     * Adds specified students to specified <code>Player</code> ID's school dining room.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param students the students to add.
     */
    public void addToDiningRoomOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).addToDiningRoom(students);
    }

    /**
     * Adds specified amount of towers to specified <code>Player</code> ID's school.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param amount the amount of towers to add.
     */
    public void addTowersTo(int playerID, int amount) {
        getSchoolByPlayerID(playerID).addTowers(amount);
    }

    /**
     * Removes specified amount of towers from specified <code>Player</code> ID's school.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param amount the amount of towers to remove.
     */
    public void removeTowerFrom(int playerID, int amount) {
        getSchoolByPlayerID(playerID).removeTowers(amount);
    }

    // PROFESSOR OWNER

    /**
     * Returns the array containing the IDs of the owners for each professor.
     *
     * @return Array of owner IDs.
     */
    public ProfessorTracker getProfessorOwners() {
        return (ProfessorTracker) professorOwners.clone();
    }

    public void setProfessorOwners(ProfessorTracker professorOwners) {
        this.professorOwners = professorOwners;
    }

    /**
     * Gives the professor of specified <code>Color</code> to specified <code>Player</code>.
     *
     * @param playerID the ID of the new professor owner.
     * @param c the <code>Color</code> of the professor.
     */
    public void giveProfessorTo(int playerID, Color c) {
        professorOwners.setOwnerIDByColor(playerID, c);
    }

    // COINS LEFT

    /**
     * Returns the amount of coins left in this <code>Board</code>.
     *
     * @return Amount of coins left.
     */
    public int getNumCoinsLeft() {
        return numCoinsLeft;
    }

    /**
     * Puts the specified amount of coins back into this <code>Board</code>.
     *
     * @param amount the amount of coins to put back.
     */
    public void putCoinsBack(int amount) {
        numCoinsLeft += amount;
    }

    /**
     * Takes one coin from this <code>Board</code>.
     */
    public void takeCoin() {
        numCoinsLeft = Math.max(numCoinsLeft - 1, 0);
    }

    public void setNumCoinsLeft(int num) {
        this.numCoinsLeft = num;
    }

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();

        JsonArray jsonIslands = new JsonArray();
        for(Island island : islands) {
            jsonIslands.add(island.serialize());
        }
        jsonObject.add("islands", jsonIslands);

        JsonArray jsonClouds = new JsonArray();
        for(Cloud cloud : clouds) {
            jsonClouds.add(cloud.serialize());
        }
        jsonObject.add("clouds", jsonClouds);

        JsonArray jsonSchools = new JsonArray();
        for(School school : schools) {
            jsonSchools.add(school.serialize());
        }
        jsonObject.add("schools", jsonSchools);

        jsonObject.add("professorOwners", professorOwners.serialize());
        jsonObject.add("numCoinsLeft", new JsonPrimitive(numCoinsLeft));

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        islands.clear();

        if(jsonObject.has("islands")) {
            JsonArray jsonIslands = jsonObject.get("islands").getAsJsonArray();
            for (JsonElement jsonIsland : jsonIslands) {
                JsonObject islandObj = jsonIsland.getAsJsonObject();
                Island island;

                if (islandObj.has("leftIsland") && islandObj.has("rightIsland")) {
                    island = new MultiIsland(new SimpleIsland(), new SimpleIsland());
                } else {
                    island = new SimpleIsland();
                }

                island.deserialize(islandObj);
                islands.add(island);
            }
        }

        clouds.clear();
        if(jsonObject.has("clouds")) {
            JsonArray jsonClouds = jsonObject.get("clouds").getAsJsonArray();
            for (JsonElement jsonCloud : jsonClouds) {
                Cloud cloud = new Cloud();
                cloud.deserialize(jsonCloud.getAsJsonObject());
                clouds.add(cloud);
            }
        }else
            clouds.clear();

        schools.clear();
        if(jsonObject.has("schools")) {
            JsonArray jsonSchools = jsonObject.get("schools").getAsJsonArray();
            for (JsonElement jsonSchool : jsonSchools) {
                School school = new School(new Player(-1, ""));
                school.deserialize(jsonSchool.getAsJsonObject());
                schools.add(school);
            }
        }else
            schools.clear();

        if(jsonObject.has("professorOwners"))
            professorOwners.deserialize(jsonObject.get("professorOwners").getAsJsonObject());
        else
            professorOwners = new ProfessorTracker();
        numCoinsLeft = jsonObject.get("numCoinsLeft").getAsInt();
    }

    @Override
    public List<String> print(boolean... params) {
        List<Boolean> bridges = findBridges();
        Island island = getIslandOfAbsoluteIndex(0);

        List<String> strings = island.print(calculateOpenings(0, bridges));
        int spaces;

        // FIRST ROW OF ISLANDS (0 - 4)
        for(int i = 1; i < 5; i ++) {
            island = getIslandOfAbsoluteIndex(i);
            spaces = 6;

            if(bridges.get(i - 1) == Boolean.TRUE) {
                spaces = 0;
                strings = StringUtils.insertAfter(strings, getHorizontalBridge(), 2, 0);
            }

            strings = StringUtils.insertAfter(strings, island.print(calculateOpenings(i, bridges)), 0, spaces);
        }

        List<String> temp = new ArrayList<>(Collections.nCopies(2, ""));
        List<String> verticalBridge = getVerticalBridge();
        spaces = strings.get(0).length();

        if(bridges.get(11) == Boolean.TRUE) {
            spaces -= verticalBridge.get(0).length();
            temp = StringUtils.insertAfter(temp, verticalBridge, 0, 0);
        }

        if(bridges.get(4) == Boolean.TRUE) {
            spaces -= verticalBridge.get(0).length();
        }

        temp = StringUtils.insertSpacesAfter(temp, spaces);

        if(bridges.get(4) == Boolean.TRUE) {
             temp = StringUtils.insertAfter(temp, verticalBridge, 0, 0);
        }

        strings.addAll(temp);

        // SECOND ROW OF ISLANDS (11 and 5)
        island = getIslandOfAbsoluteIndex(11);
        temp = island.print(calculateOpenings(11, bridges));

        //CLOUDS
        temp = StringUtils.insertAfter(temp, printClouds(), 0,0);

        island = getIslandOfAbsoluteIndex(5);
        temp = StringUtils.insertAfter(temp, island.print(calculateOpenings(5, bridges)), 0, 0);

        strings.addAll(temp);

        temp = new ArrayList<>(Collections.nCopies(2, ""));
        spaces = strings.get(0).length();

        if(bridges.get(10) == Boolean.TRUE) {
            spaces -= verticalBridge.get(0).length();
            temp = StringUtils.insertAfter(temp, verticalBridge, 0, 0);
        }

        if(bridges.get(5) == Boolean.TRUE) {
            spaces -= verticalBridge.get(0).length();
        }

        temp = StringUtils.insertSpacesAfter(temp, spaces);

        if(bridges.get(5) == Boolean.TRUE) {
            temp = StringUtils.insertAfter(temp, verticalBridge, 0, 0);
        }

        strings.addAll(temp);

        island = getIslandOfAbsoluteIndex(10);
        temp = island.print(calculateOpenings(10, bridges));

        // LAST ROW OF ISLANDS (10 - 6)
        for(int i = 9; i >= 6; i --) {
            island = getIslandOfAbsoluteIndex(i);
            spaces = 6;

            if(bridges.get(i) == Boolean.TRUE) {
                spaces = 0;
                temp = StringUtils.insertAfter(temp, getHorizontalBridge(), 2, 0);
            }

            temp = StringUtils.insertAfter(temp, island.print(calculateOpenings(i, bridges)), 0, spaces);
        }

        strings.addAll(temp);
        strings.add(" ".repeat(strings.get(0).length()));
        strings.add("─".repeat(strings.get(0).length()));
        strings = StringUtils.insertAfter(strings, new ArrayList<>(List.of("────")), strings.size() - 1, 0);

        // PLAYERS
        strings.addAll(printPlayers());

        return strings;
    }

    public List<String> getHorizontalBridge() {
        List<String> strings = new ArrayList<>();

        strings.add("─".repeat(6));
        strings.add(" ".repeat(6));
        strings.add("─".repeat(6));

        return strings;
    }

    public List<String> getVerticalBridge() {
        return new ArrayList<>(Collections.nCopies(2, " ".repeat(8) + "│     │" + " ".repeat(8)));
    }

    private List<String> printClouds() {
        List<String> result = new ArrayList<>(Collections.nCopies(7, ""));

        switch(clouds.size()) {
            case 2:
                result = StringUtils.insertSpacesAfter(result, 21);
                result = StringUtils.insertAfter(result, clouds.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, clouds.get(1).print(), 1, 21);
                result = StringUtils.insertSpacesAfter(result, 21);
                break;
            case 3:
                result = StringUtils.insertSpacesAfter(result, 10);
                result = StringUtils.insertAfter(result, clouds.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, clouds.get(1).print(), 1, 8);
                result = StringUtils.insertAfter(result, clouds.get(2).print(), 1, 8);
                result = StringUtils.insertSpacesAfter(result, 10);
                break;
            case 4:
                result = StringUtils.insertSpacesAfter(result, 9);
                result = StringUtils.insertAfter(result, clouds.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, clouds.get(1).print(), 1, 5);
                result = StringUtils.insertAfter(result, clouds.get(2).print(), 1, 5);
                result = StringUtils.insertAfter(result, clouds.get(3).print(), 1, 5);
                result = StringUtils.insertSpacesAfter(result, 9);
                break;
            default:
                break;
        }

        return result;
    }

    public List<String> printPlayers() {
        List<String> result = new ArrayList<>(Collections.nCopies(10, ""));

        switch(schools.size()) {
            case 2:
                result = StringUtils.insertSpacesAfter(result, 36);
                result = StringUtils.insertAfter(result, schools.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, schools.get(1).print(), 1, 21);
                result = StringUtils.insertSpacesAfter(result, 36);
                break;
            case 3:
                result = StringUtils.insertSpacesAfter(result, 18);
                result = StringUtils.insertAfter(result, schools.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, schools.get(1).print(), 1, 17);
                result = StringUtils.insertAfter(result, schools.get(2).print(), 1, 17);
                result = StringUtils.insertSpacesAfter(result, 18);
                break;
            case 4:
                result = StringUtils.insertSpacesAfter(result, 10);
                result = StringUtils.insertAfter(result, schools.get(0).print(), 1, 0);
                result = StringUtils.insertAfter(result, schools.get(1).print(), 1, 9);
                result = StringUtils.insertAfter(result, schools.get(2).print(), 1, 9);
                result = StringUtils.insertAfter(result, schools.get(3).print(), 1, 9);
                result = StringUtils.insertSpacesAfter(result, 10);
                break;
            default:
                break;
        }

        return result;
    }

    public List<Boolean> findBridges() {
        List<Boolean> bridges = new ArrayList<>(Collections.nCopies(12, Boolean.FALSE));
        int runningIndex = 0;

        for(Island i: islands) {
            i.markBridges(bridges, runningIndex);
            runningIndex += i.getNumIslands();
        }

        return bridges;
    }

    public boolean[] calculateOpenings(int index, List<Boolean> bridges) {
        switch (index) {
            case 0: // TOP ROW LEFT
                return new boolean[] {
                        false,
                        bridges.get(0),
                        bridges.get(11),
                        false
                };
            case 1: // TOP ROW MID
            case 2:
            case 3:
                return new boolean[] {
                        false,
                        bridges.get(index),
                        false,
                        bridges.get(index - 1)
                };
            case 4: // TOP ROW RIGHT
                return new boolean[] {
                        false,
                        false,
                        bridges.get(4),
                        bridges.get(3)
                };
            case 5: // MID ROW RIGHT
                return new boolean[] {
                        bridges.get(4),
                        false,
                        bridges.get(5),
                        false
                };
            case 6: // BOTTOM ROW RIGHT
                return new boolean[] {
                        bridges.get(5),
                        false,
                        false,
                        bridges.get(6)
                };
            case 7: // BOTTOM ROW MID
            case 8:
            case 9:
                return new boolean[] {
                        false,
                        bridges.get(index - 1),
                        false,
                        bridges.get(index)
                };
            case 10: // BOTTOM ROW LEFT
                return new boolean[] {
                        bridges.get(10),
                        bridges.get(9),
                        false,
                        false
                };
            case 11: // MID ROW LEFT
                return new boolean[] {
                        bridges.get(11),
                        false,
                        bridges.get(10),
                        false
                };
            default:
                return null;
        }
    }
}