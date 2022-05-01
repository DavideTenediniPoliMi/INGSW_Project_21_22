package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Class representing a response from the server after any action has been played during a pre-game <code>Lobby</code>.
 */
public class SetupResponseParameters {
    private List<Player> players;
    private Player player;

    /**
     * Gets the list of players of this <code>SetupResponseParameters</code>.
     *
     * @return the list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the specified list of players and returns this instance.
     *
     * @param players the list of players for this message.
     * @return this <code>SetupResponseParameters</code>
     */
    public SetupResponseParameters setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    /**
     * Gets the <code>Player</code> of this <code>SetupResponseParameters</code>.
     *
     * @return the <code>Player</code>.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the specified <code>Player</code> and returns this instance.
     *
     * @param player the <code>Player</code> for this message.
     * @return this <code>SetupResponseParameters</code>
     */
    public SetupResponseParameters setPlayer(Player player) {
        this.player = player;
        return this;
    }

    //SERIALIZE PLAYERS WILL ADD READY STATUS
}
