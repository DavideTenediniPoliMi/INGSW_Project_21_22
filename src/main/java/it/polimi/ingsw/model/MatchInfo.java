package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class used to keep data about a game of Eriantys
 */
public class MatchInfo extends Observable<ResponseParameters> implements Serializable {
    private static MatchInfo instance;
    private int selectedNumPlayer;
    private boolean expertMode;
    private int numPlayersConnected;
    private TurnState stateType;
    private final Queue<Integer> playOrder = new LinkedList<>();
    private int numMovedStudents;
    private boolean gameOver;
    private final List<TowerColor> winners = new ArrayList<>();
    private boolean gameTied;
    private GameStatus gameStatus;
    private boolean movedMN;
    private boolean gamePaused;

    /**
     * Sole constructor to avoid being instantiated more than once. MatchInfo is a singleton <code>class</code>.
     */
    private MatchInfo() {

    }

    /**
     * Returns existing MatchInfo, otherwise instantiates a new one.
     *
     * @return MatchInfo instance
     */
    public synchronized static MatchInfo getInstance() {
        if(instance == null) {
            instance = new MatchInfo();
        }

        return instance;
    }

    /**
     * Resets MatchInfo instance to null.
     */
    public synchronized static void resetInstance() {
        instance = null;
    }

    /**
     * Returns the size of the game
     *
     * @return the amount of players in the game
     */
    public synchronized int getSelectedNumPlayer() {
        return selectedNumPlayer;
    }

    /**
     * Sets the size of the game
     *
     * @param selectedNumPlayer the amount of players
     */
    private void setSelectedNumPlayer(int selectedNumPlayer) {
        this.selectedNumPlayer = selectedNumPlayer;
    }

    /**
     * Returns whether the game is in expert mode
     *
     * @return <code>true</code> if expert mode is active
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Sets expert mode flag
     *
     * @param expertMode the selected game mode
     */
    private void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    /**
     * Sets the information needed to start a game.
     *
     * @param selectedNumPlayer the number of players that are allowed to play this game
     * @param expertMode the selected game mode
     */
    public void setUpGame(int selectedNumPlayer, boolean expertMode) {
        setSelectedNumPlayer(selectedNumPlayer);
        setExpertMode(expertMode);
        notify(new ResponseParameters().setSendMatchInfo(true));
    }

    /**
     * Returns amount of players connected to the game
     *
     * @return the amount of players connected
     */
    public synchronized int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    /**
     * Update number of players connected
     *
     * @param numPlayersConnected the new amount of players connected
     */
    public synchronized void setNumPlayersConnected(int numPlayersConnected) {
        this.numPlayersConnected = numPlayersConnected;
    }

    /**
     * Returns maximum amount of movable students in each turn
     *
     * @return the amount of students a player can move during his turn
     */
    public int getMaxMovableStudents() { return (selectedNumPlayer % 2 == 0) ? 3 : 4 ;}

    /**
     * Returns the initial amount of students in each <code>Player<code/>'s <code>School</code>
     *
     * @return the initial amount of students in a <code>School</code>
     */
    public int getInitialNumStudents() { return (selectedNumPlayer % 2 == 0) ? 7 : 9 ;}


    /**
     * Returns maximum amount of towers
     *
     * @return the maximum amount of towers in the school
     */
    public int getMaxTowers() { return (selectedNumPlayer % 2 == 0) ? 8 : 6 ;}

    /**
     * Returns current state of the game
     *
     * @return current <code>TurnState</code>
     */
    public synchronized TurnState getStateType() {
        return stateType;
    }

    /**
     * Sets current state of the game
     *
     * @param stateType the new <code>TurnState</code>
     */
    public synchronized void setStateType(TurnState stateType) {
        if(stateType.equals(this.stateType)) return;

        this.stateType = stateType;
        notifyMatchInfo();
    }

    /**
     * Returns the order of play for this round. Queue with player IDs.
     *
     * @return playing order
     */
    public synchronized Queue<Integer> getPlayOrder() {
        return new LinkedList<>(playOrder);
    }

    /**
     * Removes a player from the play order. To be called after a <code>Player</code> has finished his planning/action
     * phase
     */
    public synchronized void removePlayer() {
        playOrder.remove();
    }

    /**
     * Adds a player to the play order. To be called when deciding order of play
     *
     * @param playerID the ID of the player to add next
     */
    public synchronized void addPlayer(int playerID) {
        playOrder.add(playerID);
    }

    /**
     * Returns amount of students moved so far
     *
     * @return the amount of students moved during current player's turn
     */
    public synchronized int getNumMovedStudents() {
        return numMovedStudents;
    }

    /**
     * Resets amount of students moved. To be called after a player's action phase
     */
    public synchronized void resetNumMovedStudents() {
        numMovedStudents = 0;
    }

    /**
     * Increments students moved during current player's turn
     */
    public synchronized void studentWasMoved() {
        numMovedStudents++;
    }

    /**
     * Returns the number of players who have yet to play the current planning/action phase
     *
     * @return size of play order queue
     */
    public synchronized int getNumPlayersStillToAct() {
        return playOrder.size();
    }

    /**
     * Gets current player (First of the playing order queue). Returns -1 if playing order queue is empty
     *
     * @return current player ID
     */
    public synchronized int getCurrentPlayerID() {
        return (playOrder.peek() != null) ? playOrder.peek() : -1;
    }

    /**
     * Returns if game is over
     *
     * @return <code>true</code> if game has ended
     */
    public synchronized boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns whether the game has ended in a tie
     *
     * @return <code>true</code> if two or more teams have tied
     */
    public synchronized boolean isGameTied() {
        return gameTied;
    }

    /**
     * Returns whether the game is paused.
     *
     * @return <code>true</code> if the game is paused
     */
    public synchronized boolean isGamePaused() {
        return gamePaused;
    }

    /**
     * Set the <code>boolean</code> that indicates whether the game is paused.
     *
     * @param gamePaused <code>boolean</code> that indicates whether the game is paused
     */
    public synchronized void setGamePaused(boolean gamePaused) {
        if(gamePaused == this.gamePaused) return;

        this.gamePaused = gamePaused;

        notifyMatchInfo();
    }

    /**
     * Returns game winner. If more teams have tied, returns a list of winning teams.
     *
     * @return Winner of a game. List of teams if the game ended in a tie.
     */
    public synchronized List<TowerColor> getWinners() {
        return new ArrayList<>(winners);
    }

    /**
     * Declares a winner team for this game. Ends this game.
     *
     * @param teamColor the winner team color
     */
    public synchronized void declareWinner(TowerColor teamColor) {
        gameOver = true;
        winners.add(teamColor);
    }

    /**
     * Declares a tie for this game between the specified team colors.
     *
     * @param teamColors the teams who tied.
     */
    public synchronized void declareTie(List<TowerColor> teamColors) {
        gameOver = true;
        gameTied = true;
        winners.addAll(teamColors);
    }

    /**
     * Returns the current <code>GameStatus</code> (Lobby/InGame/Resetting).
     *
     * @return Current <code>GameStatus</code>.
     * @see GameStatus
     */
    public synchronized GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Sets the <code>GameStatus</code> to the specified one.
     *
     * @param gameStatus the new <code>GameStatus</code>.
     */
    public synchronized void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        if(gameStatus == GameStatus.IN_GAME)
            notify(new ResponseParameters().setSendGame(true).setSendMatchInfo(true));
    }

    /**
     * Sets Mother Nature status to the specified flag.
     *
     * @param moved the flag specifying if Mother Nature has moved
     */
    public synchronized void setMNMoved(boolean moved) {
        movedMN = moved;
    }

    /**
     * Returns whether Mother Nature was moved during this Action phase.
     *
     * @return <code>true</code> if Mother Nature was moved.
     */
    public synchronized boolean hasMNMoved() {
        return movedMN;
    }

    /**
     * If the <code>GameStatus</code> is not <code>IN_GAME</code> notifies the match info
     */
    public void notifyMatchInfo() {
        if(gameStatus != GameStatus.IN_GAME) return;

        notify(new ResponseParameters().setSendMatchInfo(true));
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(this).getAsJsonObject();

        jsonObject.remove("observers");
        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        selectedNumPlayer = jsonObject.get("selectedNumPlayer").getAsInt();
        expertMode = jsonObject.get("expertMode").getAsBoolean();
        numPlayersConnected = jsonObject.get("numPlayersConnected").getAsInt();

        if(jsonObject.has("stateType"))
            stateType = TurnState.valueOf(jsonObject.get("stateType").getAsString());

        JsonArray jsonPlayOrder = jsonObject.get("playOrder").getAsJsonArray();
        playOrder.clear();
        for(JsonElement player : jsonPlayOrder) {
            playOrder.add(player.getAsInt());
        }

        numMovedStudents = jsonObject.get("numMovedStudents").getAsInt();
        gameOver = jsonObject.get("gameOver").getAsBoolean();
        gamePaused = jsonObject.get("gamePaused").getAsBoolean();

        JsonArray jsonWinners = jsonObject.get("winners").getAsJsonArray();
        for(JsonElement winner : jsonWinners) {
            if(winner.isJsonNull()) continue;
            winners.add(TowerColor.valueOf(winner.getAsString()));
        }

        gameTied = jsonObject.get("gameTied").getAsBoolean();
        gameStatus = GameStatus.valueOf(jsonObject.get("gameStatus").getAsString());
    }
}
