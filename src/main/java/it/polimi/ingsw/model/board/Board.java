package it.polimi.ingsw.model.board;

import com.google.gson.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class to hold data for every entity of a Game (Islands, Schools, Clouds, Professors).
 */
public class Board implements Serializable {
    private final int NUM_STARTING_ISLANDS = 12;
    private final int NUM_STARTING_COINS = 20;

    private final List<Island> islands = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<School> schools = new ArrayList<>();
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

    private Island getIslandOfAbsoluteIndex(int islandIndex) {
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
}