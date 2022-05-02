package it.polimi.ingsw.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton class to act as DAO for a game of Eriantys.
 * Contains all calls for each step of the game.
 */
public class Game extends Observable<ResponseParameters> implements Serializable {
    private final int NUM_STARTING_STUDENTS_BY_COLOR = 24;
    private static Game instance;
    private final Board board = new Board();
    private final List<Player> players = new ArrayList<>();
    private final List<CharacterCard> characterCards = new ArrayList<>();
    private final StudentBag bag = new StudentBag(NUM_STARTING_STUDENTS_BY_COLOR);

    /**
     * Sole constructor to avoid being instantiated more than once. Game is a singleton <code>class</code>.
     */
    private Game() {
    }

    /**
     * Returns existing Game, otherwise instantiates a new one.
     *
     * @return Game instance
     */
    public static Game getInstance(){
        if(instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Resets the current game instance
     */
    public static void resetInstance(){
        for(Card c: Card.values()) {
            c.reset();
        }
        instance = null;
    }

    // BOARD

    /**
     * Gets <code>Board</code> instance
     *
     * @return <code>Board</code> instance
     * @see Board
     */
    public Board getBoard() {
        return board;
    }

    // ISLAND

    /**
     * Conquers an <code>Island</code> for team <code>teamColor</code>
     *
     * @param teamColor the color of the team conquering the island
     * @see TowerColor
     */
    public void conquerIsland(TowerColor teamColor){
        ResponseParameters params = new ResponseParameters();
        TowerColor oldOwnerColor = board.getIslandAt(board.getMNPosition()).getTeamColor();
        if(oldOwnerColor != null) {
            params.addSchool(board.getSchoolByPlayerID(getTowerHolderIDOf(oldOwnerColor)));
        }

        board.conquerIsland(teamColor);

        TowerColor newOwnerColor = board.getIslandAt(board.getMNPosition()).getTeamColor();
        params.addSchool(board.getSchoolByPlayerID(getTowerHolderIDOf(newOwnerColor)))
                .setIslands(board.getIslands());

        notify(params);
    }

    /**
     * Merges two islands conquered by the same team. The islands specified must be
     * already conquered by the same team. The islands must be adjacent.
     * <p>
     * Merges are always between two islands. Convention is one of them is the "Left" <code>Island</code>, and the other
     * is the "Right" <code>Island</code>. Left/Right concepts are to be considered "clockwise", meaning the right
     * <code>Island</code> will always have index = leftIslandIndex + 1
     * </p>
     *
     * @param leftIslandIndex the index of the <code>Island</code> to the left
     * @param rightIslandIndex the index of the <code>Island</code> to the right
     * @see it.polimi.ingsw.model.board.Island
     * @see it.polimi.ingsw.model.board.SimpleIsland
     * @see it.polimi.ingsw.model.board.MultiIsland
     */
    public void mergeIslands(int leftIslandIndex, int rightIslandIndex) {
        board.mergeIslands(leftIslandIndex, rightIslandIndex);

        ResponseParameters params = new ResponseParameters().setIslands(board.getIslands());
        notify(params);
    }

    // MOTHER NATURE

    /**
     * Places Mother Nature at specified position. Only to be used when initializing the game
     *
     * @param islandIndex the position to place Mother Nature at
     */
    public void placeMNAt(int islandIndex){
        board.placeMNAt(islandIndex);
    }

    /**
     * Moves Mother Nature the specified amount of steps. The steps will always be taken "clockwise", meaning +1 will
     * move Mother Nature to the <code>Island</code> on the right, and -1 to the <code>Island</code> on the left.
     * <code>steps</code> must be positive.
     *
     * @param steps the amount of steps Mother Nature has to move
     */
    public void moveMN(int steps){
        board.moveMN(steps);

        ResponseParameters params = new ResponseParameters().setIslands(board.getIslands());
        notify(params);
    }

    // CLOUDS

    /**
     * Creates the specified amount of clouds. Only to be used when initializing the Game
     *
     * @param amount the number of clouds to create
     * @see it.polimi.ingsw.model.board.Cloud
     */
    public void createClouds(int amount) {
        board.createClouds(amount);
    }

    /**
     * Moves students contained on specified cloud, to specified player. <code>cloudIndex</code> must refer to an
     * existing <code>Cloud</code>, <code>playerID</code> must refer to an existing <code>Player</code>.
     *
     * @param cloudIndex the index of the <code>Cloud</code> to collect students from
     * @param playerID the ID of the <code>Player</code> collecting the students
     */
    public void collectFromCloud(int cloudIndex, int playerID){
        StudentGroup temp = board.collectFromCloud(cloudIndex);
        board.addToEntranceOf(playerID, temp);

        ResponseParameters params = new ResponseParameters()
                .setClouds(board.getClouds())
                .addSchool(board.getSchoolByPlayerID(playerID));
        notify(params);
    }

    /**
     * Refills all the clouds on the <code>Board</code> with <code>studentsAmount</code> amount of students and makes
     * them available again. To be called at the end of every round.
     *
     * @param studentsAmount the amount of students for each <code>Cloud</code>
     */
    public void refillClouds(int studentsAmount){
        board.refillClouds(studentsAmount);

        ResponseParameters params = new ResponseParameters()
                .setClouds(board.getClouds())
                .setBagEmpty(isStudentBagEmpty());
        notify(params);
    }

    // SCHOOL

    /**
     * Adds a new <code>School</code> bound to the specified <code>PlayerID</code>.
     *
     * @param playerID the ID of the player who will own the new <code>School</code>.
     */
    public void addSchool(int playerID) {
        board.addSchool(getPlayerByID(playerID));
    }

    /**
     * Adds the specified student to the specified <code>Island</code>. Only to be used when initializing the Game. The
     * <code>StudentGroup</code> must contain only 1 student.
     *
     * @param islandIndex the index of the recipient <code>Island</code>
     * @param student the student to add
     */
    public void addInitialStudentToIsland(int islandIndex, StudentGroup student) {
        board.addStudentsToIsland(islandIndex, student);
    }

    /**
     * Transfers a student of the specified <code>Color</code> from the specified <code>Player</code>'s <code>School</code>
     * entrance to the specified <code>Island</code>. The player must have at least 1 student of the specified
     * <code>Color</code>.
     *
     * @param islandIndex the index of the recipient <code>Island</code>
     * @param c the color of the student to transfer
     * @param playerID the ID of the <code>Player</code> requesting the transfer
     * @see Color
     */
    public void transferStudentToIsland(int islandIndex, Color c, int playerID) {
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addStudentsToIsland(islandIndex, temp);


        ResponseParameters params = new ResponseParameters()
                .setIslands(board.getIslands())
                .addSchool(board.getSchoolByPlayerID(playerID));
        notify(params);
    }

    /**
     * Transfers a student of the specified <code>Color</code> from the specified <code>Player</code>'s <code>School</code>
     * entrance to the dining room. The player must have at least 1 student of the specified <code>Color</code>.
     *
     * @param playerID the ID of the <code>Player</code> requesting the transfer
     * @param c the color of the student to transfer
     * @see Color
     */
    public void transferStudentToDiningRoom(int playerID, Color c) {
        StudentGroup temp = new StudentGroup(c, 1);

        board.removeFromEntranceOf(playerID, temp);
        board.addToDiningRoomOf(playerID, temp);

        ResponseParameters params = new ResponseParameters().addSchool(board.getSchoolByPlayerID(playerID));
        notify(params);
    }

    /**
     * Adds the specified amount of towers to the specified <code>Player</code>. The <code>Player</code> must be
     * towerHolder
     *
     * @param playerID the ID of the player. The towers are added to this player's <code>School</code>
     * @param numTowers the amount of towers to add
     */
    public void addTowersTo(int playerID, int numTowers) {
        board.addTowersTo(playerID, numTowers);
    }

    /**
     * Removes the specified amount of towers from the specified <code>Player</code>. The <code>Player</code> must be
     * towerHolder
     *
     * @param playerID the ID of the player. The towers are remove from this player's <code>School</code>
     * @param numTowers the amount of towers to remove
     */
    public void removeTowersFrom(int playerID, int numTowers) {
        board.removeTowerFrom(playerID, numTowers);
    }

    // PROFESSOR OWNER

    /**
     * Gives the professor of the specified <code>Color</code> to the specified <code>Player</code>
     *
     * @param playerID the ID of the new owner of the specified professor
     * @param c the color of the professor
     */
    public void giveProfessorTo(int playerID, Color c){
        board.giveProfessorTo(playerID, c);

        ResponseParameters params = new ResponseParameters().setProfessors(board.getProfessorOwners());
        notify(params);
    }

    // PLAYER

    /**
     * Returns the list of players in the Game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Returns the <code>Player</code> instance with specified ID. Returns <code>null</code> if no match is found
     *
     * @param ID the ID of the player
     * @return the <code>Player</code> instance with specified ID
     */
    public Player getPlayerByID(int ID) {
        Optional<Player> result = players.stream()
                .filter((player) -> (player.getID() == ID))
                .findAny();

        return result.orElse(null);
    }

    /**
     * Returns the ID of the player who holds the towers for the specified <code>TowerColor</code>.
     *
     * @param teamColor the <code>TowerColor</code> of the team.
     * @return Tower holder ID for the specified team.
     */
    public int getTowerHolderIDOf(TowerColor teamColor) {
        List<Player> players = Game.getInstance().getPlayers();

        Optional<Player> result = players.stream()
                .filter((player) -> (player.isTowerHolder() && player.getTeamColor() == teamColor))
                .findAny();

        if(result.isEmpty()) return -1;
        return result.get().getID();
    }

    /**
     * Adds a new <code>Player</code> to the game
     *
     * @param player the player to be added to the game
     * @see Player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Plays the specified (Assistant) <code>Card</code> for the specified <code>Player</code>
     *
     * @param playerID the ID of the player playing the assistant card
     * @param selectedCard the card to play
     * @see Card
     */
    public void playCard(int playerID, Card selectedCard) {
        getPlayerByID(playerID).setSelectedCard(selectedCard);
        selectedCard.use(playerID);

        ResponseParameters params = new ResponseParameters().setPlayer(getPlayerByID(playerID))
                .setSendCards(true);
        notify(params);
    }

    /**
     * Resets the cards used during a round, making them available for use again. Only to be called at the end of each round.
     */
    public void resetCards(){
        for(Card card: Card.values()) {
            card.reset();
        }

        ResponseParameters params = new ResponseParameters().setSendCards(true);
        notify(params);
    }

    /**
     * Takes a coin from the <code>Board</code> and adds it to the specified <code>Player</code>.
     *
     * @param playerID the ID of the player receiving the coin.
     */
    public void giveCoinToPlayer(int playerID) {
        board.takeCoin();
        getPlayerByID(playerID).addCoin();

        ResponseParameters params = new ResponseParameters().setPlayer(getPlayerByID(playerID))
                .setCoinsLeft(board.getNumCoinsLeft());
        notify(params);
    }

    /**
     * Gives initial <code>amount</code> amount of students to the specified <code>Player</code>'s <code>School</code>.
     * Only to be used when initializing the Game. The students are directly randomly drawn from the <code>StudentBag</code>
     *
     * @param playerID the ID of the player receiving the students
     * @param amount the amount of students to draw from the <code>StudentBag</code>
     */
    public void giveStudentsTo(int playerID, int amount) {
        StudentGroup temp = drawStudents(amount);
        board.addToEntranceOf(playerID, temp);
    }

    // CHARACTER CARD

    /**
     * Returns the instantiated Character Cards for this Game
     *
     * @return list of instantiated CharacterCards
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Instantiates a new CharacterCard with the specified ID. Only to be called when initializing the Game.
     *
     * @param cardID the ID of the card to instantiate
     * @see CharacterCards
     */
    public void instantiateCharacterCard(int cardID) {
        characterCards.add(CharacterCards.values()[cardID].instantiate());
    }

    /**
     * Gets the active CharacterCard. Returns <code>null</code> if no card is active during this Turn.
     *
     * @return active <code>CharacterCard</code>
     */
    public CharacterCard getActiveCharacterCard() {
        Optional<CharacterCard> active = characterCards.stream()
                .filter(CharacterCard::isActive)
                .findAny();

        return active.orElse(null); // will be handled by an exception
    }

    /**
     * Buys the specified <code>CharacterCard</code> for the specified <code>Player</code>. Removes the card's price
     * from the <code>Player</code>'s coins, then adds 1 coin to the card to increase its cost, and puts the other
     * coins back in the <code>Board</code>. Sets the card active.
     *
     * @param playerID the ID of the <code>Player</code> buying the card.
     * @param cardIndex the Index of the <code>CharacterCard</code> to buy.
     */
    public void buyCharacterCard(int playerID, int cardIndex) {
        CharacterCard card = characterCards.get(cardIndex);
        int cardCost = card.getCost();

        getPlayerByID(playerID).removeCoins(cardCost);
        board.putCoinsBack(cardCost - 1);
        card.increaseCost();

        card.setActive();

        ResponseParameters params = new ResponseParameters().setCharacterCards(characterCards);
        notify(params);
    }

    /**
     * Sets the specified <code>Parameters</code> to the active card. There must be an active <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to set to the active card.
     */
    public void setCardParameters(CardParameters params){
        getActiveCharacterCard().setParameters(params);
    }

    /**
     * Activates the effect of the active <code>CharacterCard</code>.
     *
     * @return result of the effect of the <code>CharacterCard</code>.
     */
    public int activateCard() {
        int temp = getActiveCharacterCard().activate();

        ResponseParameters params = getActiveCharacterCard().getResponseParameters();
        notify(params);
        return temp;
    }

    // STUDENT BAG

    /**
     * Draws the specified amount of students from the <code>StudentBag</code>.
     *
     * @param amount the amount of students to draw.
     * @return StudentGroup containing the amount of students requested.
     */
    public StudentGroup drawStudents(int amount) {
        return bag.drawStudents(amount);
    }

    /**
     * Adds the specified <code>StudentGroup</code> back to the <code>StudentBag</code>.
     *
     * @param students the students to put back in the <code>StudentBag</code>.
     */
    public void putStudentsBack(StudentGroup students) {
        bag.putStudentsBack(students);
    }

    /**
     * Checks whether the <code>StudentBag</code> is empty.
     *
     * @return <code>true</code> if the <code>StudentBag</code> is empty.
     */
    public boolean isStudentBagEmpty() {
        return bag.isEmpty();
    }

    @Override
    public JsonObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {

    }
}
