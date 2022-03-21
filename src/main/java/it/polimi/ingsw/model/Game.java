package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.StudentGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private final Board board;
    private final List<Player> players = new ArrayList<>();
    private final List<CharacterCard> cards = new ArrayList<>();
    private static Game instance;

    private Game() {
        this.board = new Board();
    }

    public static Game getInstance(){
        if(instance == null)
            instance = new Game();
        return instance;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getPlayerByID(int ID){
        Optional<Player> result = players.stream()
                .filter((player) -> (player.getID() == ID))
                .findAny();

        return result.orElse(null);
    }

    public void addPlayer(int ID, String name, boolean TowerHolder, TowerColor teamColor, CardBack cardBack){

    }

    public boolean isNameTaken(){
        //TODO
        return true;
    }

    public void placeMNAt(){

    }

    public void moveMN(int steps){

    }

    public void addStudentToIsland(int index, Color c){

    }

    public void transferToIsland(int playerID, int islandIndex, Color c){

    }

    public void transferToDiningRoom(int playerID, Color c){

    }

    public void collectFromCloud(int playerID, int cloudIndex){

    }

    public void giveProfessorTo(int playerID, Color c){

    }

    public void instantiateCharacterCard(int cardID){

    }

    public void giveCoinToPlayer(int playerID){

    }

    public void buyCharacterCard(int cardID){

    }

    public void giveStudentsTo(int playerID, int amount){

    }

    public void refillClouds(int studentsAmount){

    }

    public void conquerIsland(int playerID, int islandIndex){

    }

    public void addTowers(int playerID, int numTowers){

    }

    public void removeTowers(int playerID, int numTowers){

    }

    public List<CharacterCard> getCharacterCardsByEffectType(EffectType effectType){
        return this.cards;
    }

    public void playCard(int playerID, Card selectedCard){

    }

    private void putCoinsBack(int amount){

    }

    private StudentGroup drawStudents(int amount){
        StudentGroup temp = new StudentGroup();
        //TODO
        return temp;
    }
}
