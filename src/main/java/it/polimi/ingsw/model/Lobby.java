package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.player.NameTakenException;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class representing a Lobby before a game of Eriantys. Allows for CardBack and Team selection and/or changes for each
 * <code>Player</code>.
 */
public class Lobby {
    private static Lobby instance;
    private List<Player> players;

    /**
     * Sole constructor to avoid being instantiated more than once.
     * <code>Lobby</code> is a Singleton class
     */
    private Lobby() {
        players = new ArrayList<>();
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
     * Adds a new <code>Player</code> to this <code>Lobby</code>, with the specified ID and Nickname.
     *
     * @param playerID the ID of the new <code>Player</code>.
     * @param name the Nickname of the new <code>Player</code>.
     * @throws NameTakenException If the Nickname is already taken.
     */
    public void addPlayer(int playerID, String name) throws NameTakenException {
        if(!isNameTaken(name)) {
            players.add(new Player(playerID, name));
        }else{
            throw new NameTakenException(name);
        }
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
    }

    /**
     * Returns whether the specified name is already taken
     *
     * @param name the name to check
     * @return <code>true</code> if another <code>Player</code> already has that name
     */
    private boolean isNameTaken(String name) {
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
