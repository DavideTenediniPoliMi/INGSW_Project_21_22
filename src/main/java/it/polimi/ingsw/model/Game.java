package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private static Game instance;
    private final Board board = new Board();
    private final List<Player> players = new ArrayList<>();
    private final List<CharacterCard> cards = new ArrayList<>();
    private final StudentBag bag = new StudentBag();

    private Game() {
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

    public void createClouds(int amount) {
        board.createClouds(amount);
    }

    public void addPlayer(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder){
        players.add(new Player(ID, name, teamColor, cardBack, towerHolder));

    }

    public boolean isNameTaken(String name){
        return players.stream().anyMatch(player -> player.getName().equals(name));
    }

    public void placeMNAt(int islandIndex){
        board.placeMNAt(islandIndex);
    }

    public void moveMN(int steps){
        board.moveMN(steps);
    }

    public void addStudentToIsland(int islandIndex, Color c){
        StudentGroup temp = new StudentGroup(c, 1);

        board.addStudentsToIsland(islandIndex, temp);
    }

    public void transferToIsland(int playerID, int islandIndex, Color c){
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addStudentsToIsland(islandIndex, temp);
    }

    public void transferToDiningRoom(int playerID, Color c){
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addToDiningRoomOf(playerID, temp);
    }

    public void collectFromCloud(int playerID, int cloudIndex){
        StudentGroup temp = board.collectFromCloud(cloudIndex);
        board.addToEntranceOf(playerID, temp);
    }

    public void giveProfessorTo(int playerID, Color c){
        board.giveProfessorTo(playerID, c);
    }

    public void instantiateCharacterCard(int cardID){
        cards.add(CharacterCards.values()[cardID].instantiate());
    }

    public void giveCoinToPlayer(int playerID){
        getPlayerByID(playerID).addCoin();
    }

    public void giveStudentsTo(int playerID, int amount){
        StudentGroup temp = drawStudents(amount);
        board.addToEntranceOf(playerID, temp);
    }

    public void refillClouds(int studentsAmount){
        board.refillClouds(studentsAmount);
    }

    public void conquerIsland(int playerID, int islandIndex){
        TowerColor teamColor = getPlayerByID(playerID).getTeamColor();
        board.conquerIsland(islandIndex, teamColor);
    }

    public void mergeIslands(int leftIslandIndex, int rightIslandIndex) {
        board.mergeIslands(leftIslandIndex, rightIslandIndex);
    }

    public void addTowers(int playerID, int numTowers){
        board.addTowersTo(playerID, numTowers);
    }

    public void removeTowers(int playerID, int numTowers){
        board.removeTowerFrom(playerID, numTowers);
    }

    public List<CharacterCard> getCharacterCardsByEffectType(EffectType effectType){
        return this.cards;
    }

    public void playCard(int playerID, Card selectedCard){
        getPlayerByID(playerID).setSelectedCard(selectedCard);
        selectedCard.use();
    }

    public void resetCards(){
        //TODO
    }

    public CharacterCard getActiveCard(){
        Optional<CharacterCard> active = cards.stream().filter(CharacterCard::isActive).findAny();

        return active.orElse(null);
    }

    public void buyCharacterCard(int playerID, int cardIndex){
        CharacterCard card = cards.get(cardIndex);

        int cardCost = card.getCost();

        getPlayerByID(playerID).removeCoins(cardCost);
        board.putCoinsBack(cardCost - 1);
    }

    public void setCardParameters(Parameters params){
        CharacterCard c = getActiveCard();
        if(c != null){
            c.setParameters(params);
        }
    }

    public int activateCard(){
        CharacterCard c = getActiveCard();
        if(c != null){
            return c.activate();
        }

        return 0;
    }

    public StudentGroup drawStudents(int amount){
        return bag.drawStudents(amount);
    }

    public void putStudentsBack(StudentGroup students) {

    }
}
