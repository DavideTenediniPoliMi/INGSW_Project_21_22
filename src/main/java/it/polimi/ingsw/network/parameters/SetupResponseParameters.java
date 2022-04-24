package it.polimi.ingsw.network.parameters;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class SetupResponseParameters {
    private List<Player> players;
    private Player player;

    public List<Player> getPlayers() {
        return players;
    }

    public SetupResponseParameters setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public SetupResponseParameters setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
