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
    private final List<CharacterCard> characterCards = new ArrayList<>();
    private final StudentBag bag = new StudentBag();

    private Game() {
    }

    public static Game getInstance(){
        if(instance == null)
            instance = new Game();
        return instance;
    }

    public static void resetInstance(){
        instance = null;
    }

    // BOARD

    public Board getBoard() {
        return board;
    }

    // ISLAND

    public void conquerIsland(TowerColor teamColor){
        board.conquerIsland(teamColor);
    }

    public void mergeIslands(int leftIslandIndex, int rightIslandIndex) {
        board.mergeIslands(leftIslandIndex, rightIslandIndex);
    }

    // MOTHER NATURE

    public void placeMNAt(int islandIndex){
        board.placeMNAt(islandIndex);
    }

    public void moveMN(int steps){
        board.moveMN(steps);
    }

    // CLOUDS

    public void createClouds(int amount) {
        board.createClouds(amount);
    }

    public void collectFromCloud(int cloudIndex, int playerID){
        StudentGroup temp = board.collectFromCloud(cloudIndex);
        board.addToEntranceOf(playerID, temp);
    }

    public void refillClouds(int studentsAmount){
        board.refillClouds(studentsAmount);
    }

    // SCHOOL

    public void addInitialStudentToIsland(int islandIndex, Color c) {
        StudentGroup temp = new StudentGroup(c, 1);

        board.addStudentsToIsland(islandIndex, temp);
    }

    public void transferStudentToIsland(int islandIndex, Color c, int playerID) {
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addStudentsToIsland(islandIndex, temp);
    }

    public void transferStudentToDiningRoom(int playerID, Color c) {
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addToDiningRoomOf(playerID, temp);
    }

    public void addTowersTo(int playerID, int numTowers) {
        board.addTowersTo(playerID, numTowers);
    }

    public void removeTowersFrom(int playerID, int numTowers) {
        board.removeTowerFrom(playerID, numTowers);
    }

    // PROFESSOR OWNER

    public void giveProfessorTo(int playerID, Color c){
        board.giveProfessorTo(playerID, c);
    }

    // PLAYER

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getPlayerByID(int ID) {
        Optional<Player> result = players.stream()
                .filter((player) -> (player.getID() == ID))
                .findAny();

        return result.orElse(null);
    }

    public void addPlayer(int ID, String name, TowerColor teamColor, CardBack cardBack, boolean towerHolder) {
        players.add(new Player(ID, name, teamColor, cardBack, towerHolder));
    }

    public boolean isNameTaken(String name) {
        return players.stream().anyMatch(player -> player.getName().equals(name));
    }

    public void playCard(int playerID, Card selectedCard) {
        getPlayerByID(playerID).setSelectedCard(selectedCard);
        selectedCard.use();
    }

    public void resetCards(){
        for(Card card: Card.values()) {
            card.reset();
        }
    }

    public void giveCoinToPlayer(int playerID) {
        board.takeCoin();
        getPlayerByID(playerID).addCoin();
    }

    public void giveStudentsTo(int playerID, int amount) {
        StudentGroup temp = drawStudents(amount);
        board.addToEntranceOf(playerID, temp);
    }

    // CHARACTER CARD

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void instantiateCharacterCard(int cardID) {
        characterCards.add(CharacterCards.values()[cardID].instantiate());
    }

    public CharacterCard getActiveCharacterCard() {
        Optional<CharacterCard> active = characterCards.stream()
                .filter(CharacterCard::isActive)
                .findAny();

        return active.orElse(null); // will be handled by an exception
    }

    public void buyCharacterCard(int playerID, int cardIndex) {
        CharacterCard card = characterCards.get(cardIndex);
        int cardCost = card.getCost();

        getPlayerByID(playerID).removeCoins(cardCost);
        board.putCoinsBack(cardCost - 1);
        card.increaseCost();

        card.setActive();
    }

    public void setCardParameters(Parameters params){
        getActiveCharacterCard().setParameters(params);
    }

    public int activateCard() {
        return getActiveCharacterCard().activate();
    }

    // STUDENT BAG

    public StudentGroup drawStudents(int amount) {
        return bag.drawStudents(amount);
    }

    public void putStudentsBack(StudentGroup students) {
        bag.putStudentsBack(students);
    }

    public boolean isStudentBagEmpty() {
        return bag.isEmpty();
    }
}
