package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Island> islands = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<School> schools = new ArrayList<>();
    private ProfessorTracker professorOwners;
    private int numCoinsLeft;

    public Board() {
    }

    private School getSchoolByPlayerID(int playerID) {
        return schools.get(0);
    }

    public void placeMNAt(int islandIndex) {
        islands.get(islandIndex).setMotherNatureTo(true);
    }

    public void moveMN(int steps) {

    }

    public void addStudentsToIsland(StudentGroup students, int islandIndex) {

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

    }

    public void conquerIsland(TowerColor teamColor, int islandIndex) {
        islands.get(islandIndex).conquerIsland(teamColor);
    }

    public void takeCoin() {
        numCoinsLeft --;
    }

    public void removeFromEntranceOf(int playerID, StudentGroup students) {

    }

    public void addToEntranceOf(int playerID, StudentGroup students) {

    }

    public void removeFromDiningRoomOf(int playerID, StudentGroup students) {

    }

    public void addToDiningRoomOf(int playerID, StudentGroup students) {

    }
}