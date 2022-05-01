package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.parameters.SetupResponseParameters;

import java.util.*;

/**
 * Class representing a Lobby before a game of Eriantys. Allows for CardBack and Team selection and/or changes for each
 * <code>Player</code>.
 */
public class Lobby extends Observable<SetupResponseParameters> {
    private static Lobby instance;
    private final List<Player> players;
    private final Map<Integer, Boolean> readyStatus;

    /**
     * Sole constructor to avoid being instantiated more than once.
     * <code>Lobby</code> is a Singleton class
     */
    private Lobby() {
        players = new ArrayList<>();
        readyStatus = new HashMap<>();
    }

    /**
     * Returns existing Lobby, otherwise instantiates a new one.
     *
     * @return Lobby instance
     */
    public static Lobby getLobby() {
        if(instance == null)
            instance = new Lobby();
        return instance;
    }

    /**
     * Resets the current <code>Lobby</code> instance
     */
    public static void resetLobby() {
        instance = null;
    }

    /**
     * Adds a new <code>Player</code> to this <code>Lobby</code>, with the specified ID and Nickname.
     *
     * @param playerID the ID of the new <code>Player</code>.
     * @param name the Nickname of the new <code>Player</code>.
     */
    public void addPlayer(int playerID, String name) {
        players.add(new Player(playerID, name));
        readyStatus.put(playerID, false);

        SetupResponseParameters params = new SetupResponseParameters().setPlayer(getPlayerByID(playerID));
        notify(params);
    }

    /**
     * Removes the <code>Player</code> with the specified ID from this <code>Lobby</code>.
     *
     * @param playerID the ID of the <code>Player</code> to remove.
     */
    public void removePlayer(int playerID) {
        Player toRemove = getPlayerByID(playerID);
        if(toRemove != null) {
            players.remove(toRemove);
        }

        SetupResponseParameters params = new SetupResponseParameters().setPlayers(players);
        notify(params);
    }

    /**
     * Returns whether the <code>Player</code> with the specified ID has already joined this <code>Lobby</code>.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @return <code>true</code> if a <code>Player</code> with the same ID is already in this <code>Lobby</code>.
     */
    public boolean hasJoined(int playerID) {
        return getPlayerByID(playerID) != null;
    }

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
     * Moves the specified <code>Player</code> to the specified <code>TowerColor</code> team.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param teamColor the team to join.
     */
    public void selectTeam(int playerID, TowerColor teamColor){
        Player p = getPlayerByID(playerID);
        if(p != null) {
            p.setTeamColor(teamColor);
            p.setTowerHolder(isFirstOnTeam(playerID, teamColor));
        }

        SetupResponseParameters params = new SetupResponseParameters().setPlayer(getPlayerByID(playerID));
        notify(params);
    }

    /**
     * Selects a <code>CardBack</code> for the specified <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param cardBack the <code>CardBack</code> selected.
     */
    public void selectCardBack(int playerID, CardBack cardBack) {
        Player p = getPlayerByID(playerID);
        if(p != null) {
            p.setCardBack(cardBack);
        }

        SetupResponseParameters params = new SetupResponseParameters().setPlayer(getPlayerByID(playerID));
        notify(params);
    }

    /**
     * Sets whether the specified <code>Player</code> is ready.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param ready the flag specifying if the <code>Player</code> is ready.
     */
    public void setReadyStatus(int playerID, boolean ready) {
        readyStatus.put(playerID, ready);
    }

    /**
     * Returns whether the specified <code>Player</code> is ready.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @return <code>true</code> if the specified <code>Player</code> is ready.
     */
    public boolean isReady(int playerID) {
        return readyStatus.get(playerID);
    }

    /**
     * Returns whether the specified name is already taken
     *
     * @param name the name to check
     * @return <code>true</code> if another <code>Player</code> already has that name
     */
    public boolean isNameTaken(String name) {
        return players.stream().anyMatch(player -> player.getName().equals(name));
    }

    /**
     * Returns whether the specified <code>Player</code> is the first one to join the specified team.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param teamColor the team joined.
     * @return <code>true</code> if the specified <code>Player</code> is the first to join the team.
     */
    private boolean isFirstOnTeam(int playerID, TowerColor teamColor){
        for(Player player : players) {
            if(player.getID() == playerID) {
                continue;
            }

            if(player.getTeamColor() != null && player.getTeamColor().equals(teamColor)) {
                return false;
            }
        }
        return true;
    }
}
