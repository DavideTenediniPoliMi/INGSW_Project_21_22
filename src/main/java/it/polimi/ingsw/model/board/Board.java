package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {
    private List<Island> islands = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<School> schools = new ArrayList<>();
    private ProfessorTracker professorOwners;
    private int numCoinsLeft;

    public Board() {
    }

    private School getSchoolByPlayerID(final int playerID) {
        Optional<School> result = schools.stream()
                .filter((school) -> (school.getOwner().getID() == playerID))
                .findAny();

        return result.orElse(null);
    }

    private int getIslandIndexWithMN() {
        Optional<Island> result = islands.stream()
                .filter(Island::isMotherNatureOnIsland)
                .findAny();

        return islands.indexOf(result.orElse(null));
    }

    public void placeMNAt(int islandIndex) {
        islands.get(islandIndex).setMotherNatureTo(true);
    }

    public void moveMN(int steps) {
        int currentIslandIndex = getIslandIndexWithMN();
        islands.get(currentIslandIndex).setMotherNatureTo(false);

        int nextIslandIndex = (currentIslandIndex + steps) % islands.size();
        islands.get(nextIslandIndex).setMotherNatureTo(true);
    }

    public void addStudentsToIsland(StudentGroup students, int islandIndex) {
        islands.get(islandIndex).addStudents(students);
    }

    public StudentGroup collectFromCloud(int cloudIndex) {
        return clouds.get(cloudIndex).collectStudents();
    }

    public void giveProfessorTo(int playerID, Color c) {
        professorOwners.setOwnerIDByColor(playerID, c);
    }

    public void putCoinsBack(int amount) {
        numCoinsLeft += amount;
    }

    public void refillClouds(int studentAmount) {
        StudentBag bag = StudentBag.getBag();
        for(Cloud cloud: clouds) {
            StudentGroup temp = bag.drawStudents(studentAmount);
            cloud.refillCloud(temp);
        }
    }

    public void conquerIsland(TowerColor teamColor, int islandIndex) {
        islands.get(islandIndex).conquerIsland(teamColor);
    }

    public void takeCoin() {
        numCoinsLeft --;
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
}