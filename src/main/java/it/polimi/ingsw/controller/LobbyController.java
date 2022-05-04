package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.lobby.*;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.commands.Command;

/**
 * Class representing the controller for all pre-game (Lobby) actions. Handles:
 * <ul>
 *     <li>Joining a game</li>
 *     <li>Team selection</li>
 *     <li>CardBack selection</li>
 * </ul>
 */
public class LobbyController {
    private final Lobby lobby;
    private final MatchInfo matchInfo;

    /**
     * Sole constructor for <code>LobbyController</code>.
     */
    public LobbyController() {
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();
        matchInfo.setGameStatus(GameStatus.LOBBY);
    }

    /**
     * Requests the specified command in the current <code>Lobby</code>.
     *
     * @param command the requested <code>Command</code>.
     */
    public synchronized void requestCommand(Command command) throws EriantysException {
        if(!matchInfo.getGameStatus().equals(GameStatus.LOBBY)) {
            throw new GameStartedException();
        }
        command.execute();
    }

    public void createGame(int selectedNumPlayer, boolean expertMode) throws BadParametersException {
        if(selectedNumPlayer < 0 || selectedNumPlayer > 4) {
            throw new BadParametersException("Wrong player number selection");
        }
        if(matchInfo.getSelectedNumPlayer() != 0) {
            matchInfo.setUpGame(matchInfo.getSelectedNumPlayer(), matchInfo.isExpertMode());
            return;
        }
        matchInfo.setUpGame(selectedNumPlayer, expertMode);
    }

    /**
     * Adds a new <code>Player</code> with the specified ID and Nickname to the current <code>Lobby</code>.
     *
     * @param playerID the ID of the new <code>Player</code>.
     * @param name the Nickname of the new <code>Player</code>.
     * @throws GameFullException If the current <code>Lobby</code> is full.
     * @throws DuplicateIDException If there is already another <code>Player</code> with the same ID.
     * @throws NameTakenException If there is already another <code>Player</code> with the same Nickname.
     */
    public void joinLobby(int playerID, String name) throws DuplicateIDException, NameTakenException, GameFullException {
        if(lobby.getPlayers().size() == matchInfo.getSelectedNumPlayer()) {
            throw new GameFullException();
        }else if(lobby.hasJoined(playerID)){
            throw new DuplicateIDException(playerID);
        } else if(lobby.isNameTaken(name)){
            throw new NameTakenException(name);
        }

        lobby.addPlayer(playerID, name);
    }

    /**
     * Selects the specified <code>CardBack</code> for the specified <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param cardBack the <code>CardBack</code> selected.
     * @throws NoSuchPlayerException If there is no <code>Player</code> with the specified ID in this <code>Lobby</code>.
     * @throws CardBackTakenException If another <code>Player</code> has already selected the same <code>CardBack</code>.
     */
    public void setCardBack(int playerID, CardBack cardBack) throws NoSuchPlayerException, CardBackTakenException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }else if(isCardBackSelected(cardBack)) {
            throw new CardBackTakenException(cardBack);
        }

        lobby.selectCardBack(playerID, cardBack);
    }

    /**
     * Selects the specified <code>TowerColor</code> for the specified <code>Player</code>.
     *
     * @param playerID the Id of the <code>Player</code>.
     * @param towerColor the <code>TowerColor</code> selected.
     * @throws NoSuchPlayerException If there is no <code>Player</code> with the specified ID in this <code>Lobby</code>.
     * @throws TeamFullException If the team already has the maximum amount of members.
     */
    public void setTowerColor(int playerID, TowerColor towerColor) throws NoSuchPlayerException, TeamFullException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }else if(!canJoin(towerColor)) {
            throw new TeamFullException(towerColor);
        }

        lobby.selectTeam(playerID, towerColor);
    }

    /**
     * Sets the specified ready status for the specified <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @param ready the flag specifying if the <code>Player</code> is ready.
     * @throws NoSuchPlayerException If there is no <code>Player</code> with the specified ID in this <code>Lobby</code>.
     */
    public void setReadyStatus(int playerID, boolean ready) throws NoSuchPlayerException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }

        lobby.setReadyStatus(playerID, ready);
    }

    /**
     * Removes the specified <code>Player</code> from this <code>Lobby</code>.
     *
     * @param playerID the ID of the <code>Player</code>.
     * @throws NoSuchPlayerException If there is no <code>Player</code> with the specified ID in this <code>Lobby</code>.
     */
    public void removePlayer(int playerID) throws NoSuchPlayerException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }

        lobby.removePlayer(playerID);
    }

    /**
     * Returns whether the specified <code>CardBack</code> is already selected by a <code>Player</code>.
     *
     * @param cardBack the <code>CardBack</code>.
     * @return <code>true</code> if a <code>Player</code> has already selected the specified <code>CardBack</code>.
     */
    private boolean isCardBackSelected(CardBack cardBack) {
        for(Player player : lobby.getPlayers()) {
            if(cardBack.equals(player.getCardBack())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether a <code>Player</code> can join the specified team.
     *
     * @param towerColor the <code>TowerColor</code> of the team.
     * @return <code>true</code> if a <code>Player</code> can join.
     */
    private boolean canJoin(TowerColor towerColor) {
        int members = 0;

        for(Player player : lobby.getPlayers()) {
            if(towerColor.equals(player.getTeamColor())) {
                if(matchInfo.getSelectedNumPlayer() == 4 && members == 0) {
                    members += 1;
                    continue;
                }
                return false;
            }
        }

        return true;
    }
}
