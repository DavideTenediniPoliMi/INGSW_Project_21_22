package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.lobby.*;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.SetupParameters;

public class LobbyController {
    private static LobbyController instance;
    private Lobby lobby;
    private MatchInfo matchInfo;

    private LobbyController() {
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();
    }

    public static LobbyController getLobbyController() {
        if(instance == null)
            instance = new LobbyController();
        return instance;
    }

    /**
     * Requests the specified setup action in the current <code>Lobby</code>.
     *
     * @param playerID the ID of the <code>Player</code> requesting a change in the <code>Lobby</code>.
     * @param setupParams the parameters of the request.
     */
    public synchronized void requestSetup(int playerID, SetupParameters setupParams) throws EriantysException {
        switch (setupParams.getSetupType()) {
            case JOIN:
                joinLobby(playerID, setupParams.getName());
                break;
            case SEL_CARDBACK:
                setCardBack(playerID, setupParams.getCardBack());
                break;
            case SEL_TOWERCOLOR:
                setTowerColor(playerID, setupParams.getTowerColor());
                break;
        }
    }

    private void joinLobby(int playerID, String name) throws DuplicateIDException, NameTakenException {
        if(lobby.hasJoined(playerID)){
            throw new DuplicateIDException(playerID);
        } else if(lobby.isNameTaken(name)){
            throw new NameTakenException(name);
        }

        lobby.addPlayer(playerID, name);
    }

    private void setCardBack(int playerID, CardBack cardBack) throws NoSuchPlayerException, CardBackTakenException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }else if(isCardBackSelected(cardBack)) {
            throw new CardBackTakenException(cardBack);
        }

        lobby.selectCardBack(playerID, cardBack);
    }

    private void setTowerColor(int playerID, TowerColor towerColor) throws NoSuchPlayerException, TeamFullException {
        if(!lobby.hasJoined(playerID)) {
            throw new NoSuchPlayerException(playerID);
        }else if(!canJoin(towerColor)) {
            throw new TeamFullException(towerColor);
        }

        lobby.selectTeam(playerID, towerColor);
    }

    private boolean isCardBackSelected(CardBack cardBack) {
        for(Player player : lobby.getPlayers()) {
            if(player.getCardBack().equals(cardBack)) {
                return true;
            }
        }
        return false;
    }

    private boolean canJoin(TowerColor towerColor) {
        int members = 0;

        for(Player player : lobby.getPlayers()) {
            if(player.getTeamColor().equals(towerColor)) {
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
