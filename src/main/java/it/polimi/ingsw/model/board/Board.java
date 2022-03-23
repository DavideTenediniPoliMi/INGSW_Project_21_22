package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {
    private final int NUM_STARTING_ISLANDS = 12;
    private final int NUM_STARTING_COINS = 20;

    private final List<Island> islands = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<School> schools = new ArrayList<>();
    private final ProfessorTracker professorOwners = new ProfessorTracker();
    private int numCoinsLeft = NUM_STARTING_COINS;

    public Board() {
        for(int i = 0; i < NUM_STARTING_ISLANDS; i++) {
            islands.add(new SimpleIsland());
        }
    }

    // ISLAND

    public Island getIslandAt(int islandIndex) {
        return islands.get(islandIndex);
    }

    public void addStudentsToIsland(int islandIndex, StudentGroup students) {
        islands.get(islandIndex).addStudents(students);
    }

    public void conquerIsland(int islandIndex, TowerColor teamColor) {
        islands.get(islandIndex).conquerIsland(teamColor);
    }

    public void mergeIslands(int leftIslandIndex, int rightIslandIndex) {
        Island leftIsland = islands.get(leftIslandIndex);
        Island rightIsland = islands.get(rightIslandIndex);

        Island temp = new MultiIsland(leftIsland, rightIsland);

        islands.remove(rightIslandIndex);
        islands.set(leftIslandIndex, temp);
    }

    // MOTHER NATURE

    private int getMNPosition() {
        Optional<Island> result = islands.stream()
                .filter(Island::isMotherNatureOnIsland)
                .findAny();

        return islands.indexOf(result.orElse(null));
    }

    public void placeMNAt(int islandIndex) {
        islands.get(islandIndex).setMotherNatureTo(true);
    }

    public void moveMN(int steps) {
        int currentIslandIndex = getMNPosition();
        islands.get(currentIslandIndex).setMotherNatureTo(false);

        int nextIslandIndex = (currentIslandIndex + steps) % islands.size();
        islands.get(nextIslandIndex).setMotherNatureTo(true);
    }

    // CLOUD

    public void createClouds(int amount) {
        for(int i = 0; i < amount; i++) {
            clouds.add(new Cloud());
        }
    }

    public StudentGroup collectFromCloud(int cloudIndex) {
        return clouds.get(cloudIndex).collectStudents();
    }

    public void refillClouds(int studentAmount) {
        for(Cloud cloud: clouds) {
            StudentGroup temp = Game.getInstance().drawStudents(studentAmount);
            cloud.refillCloud(temp);
        }
    }

    // SCHOOL

    public void addSchool(Player owner) {
        schools.add(new School(owner));
    }

    public School getSchoolByPlayerID(int playerID) {
        Optional<School> result = schools.stream()
                .filter((school) -> (school.getOwner().getID() == playerID))
                .findAny();

        return result.orElse(null);
    }

    public void removeFromEntranceOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).removeFromEntrance(students);
    }

    public void addToEntranceOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).addToEntrance(students);
    }

    public void removeFromDiningRoomOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).removeFromDiningRoom(students);
    }

    public void addToDiningRoomOf(int playerID, StudentGroup students) {
        getSchoolByPlayerID(playerID).addFromDiningRoom(students);
    }

    public void addTowersTo(int playerID, int amount) {
        getSchoolByPlayerID(playerID).addTowers(amount);
    }

    public void removeTowerFrom(int playerID, int amount) {
        getSchoolByPlayerID(playerID).removeTowers(amount);
    }

    // PROFESSOR OWNER

    public ProfessorTracker getProfessorOwners() {
        return professorOwners;
    }

    public void giveProfessorTo(int playerID, Color c) {
        professorOwners.setOwnerIDByColor(playerID, c);
    }

    // COINS LEFT

    public int getNumCoinsLeft() {
        return numCoinsLeft;
    }

    public void putCoinsBack(int amount) {
        numCoinsLeft += amount;
    }

    public void takeCoin() {
        numCoinsLeft --;
    }
}