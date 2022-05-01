package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.enumerations.ActionType;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.ActionRequestParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    GameController gameController;
    Game game;
    Lobby lobby;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        game = Game.getInstance();
        lobby = Lobby.getLobby();
        matchInfo = MatchInfo.getInstance();

        lobby.addPlayer(0, "a");
        lobby.addPlayer(1, "b");
        lobby.selectTeam(0, TowerColor.BLACK);
        lobby.selectTeam(1, TowerColor.WHITE);
        lobby.selectCardBack(0, CardBack.CB_1);
        lobby.selectCardBack(1, CardBack.CB_2);

        matchInfo.setSelectedNumPlayer(2);
        matchInfo.setNumPlayersConnected(2);
        matchInfo.setExpertMode(true);

        gameController.createGame();
    }

    @AfterEach
    void tearDown() {
        gameController = null;
        Game.resetInstance();
        MatchInfo.resetInstance();
    }

    @Test
    void playCardTest() {
        int playerID = matchInfo.getCurrentPlayerID();
        ActionRequestParameters params = new ActionRequestParameters();
        params.setActionType(ActionType.PLAY_CARD)
                .setIndex(0);
        /*try {
            gameController.requestCommand(playerID, params);
        } catch (EriantysException e) {
            throw new RuntimeException(e);
        }
        assertFalse(game.getPlayerByID(playerID).getPlayableCards().contains(Card.CARD_1));*/
    }
    
}