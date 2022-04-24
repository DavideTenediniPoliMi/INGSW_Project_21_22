/*
package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller.round.RoundStateController;
import it.polimi.ingsw.exceptions.game.ExpertModeDisabledException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.player.CardUsedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController controller;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        controller = new GameController();

        matchInfo = MatchInfo.getInstance();
        matchInfo.setExpertMode(true);
        matchInfo.setSelectedNumPlayer(2);
        matchInfo.setNumPlayersConnected(2);

        game = Game.getInstance();

        game.instantiateCharacterCard(0);
        game.instantiateCharacterCard(3);
        game.instantiateCharacterCard(7);

        game.addPlayer(0, "name1", TowerColor.WHITE, CardBack.CB_1, true);
        game.incrementSteps();

        game.addPlayer(1, "name2", TowerColor.BLACK, CardBack.CB_2, true);
        game.incrementSteps();
    }

    @AfterEach
    void tearDown() {
        controller = null;
        Game.resetInstance();
        MatchInfo.resetInstance();
    }

    @Test
    void gameTest() throws Exception {
        controller.createGame();

        //PLANNING PHASE
        RoundStateController rsc = controller.getStateController();

        rsc.playCard(0);
        controller.nextState();

        rsc.playCard(9);
        controller.nextState();

        rsc = controller.getStateController();

        //ACTION PHASE 1
        //STUDENT 1
        moveThreeStudentsSomewhere(rsc);
        controller.nextState();
        rsc = controller.getStateController();
        //MN 1
        rsc.moveMN(1);
        controller.nextState();
        rsc = controller.getStateController();
        //CLOUD 1
        rsc.collectFromCloud(0);
        controller.nextState();

        rsc = controller.getStateController();
        //ACTION PHASE 2
        //STUDENT 2
        moveThreeStudentsSomewhere(rsc);
        controller.nextState();
        rsc = controller.getStateController();
        //MN 2
        rsc.moveMN(1);
        controller.nextState();
        rsc = controller.getStateController();
        //CLOUD 2
        rsc.collectFromCloud(1);
        controller.nextState();
        rsc = controller.getStateController();

        rsc.playCard(4);
        controller.nextState();

        rsc.playCard(5);
        controller.nextState();

        rsc = controller.getStateController();

        game.giveCoinToPlayer(0);
        game.incrementSteps();
        game.giveCoinToPlayer(0);
        game.incrementSteps();
        game.giveCoinToPlayer(0);
        game.incrementSteps();
        game.giveCoinToPlayer(0);
        game.incrementSteps();
        game.giveCoinToPlayer(0);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();
        game.giveCoinToPlayer(1);
        game.incrementSteps();

        //ACTION PHASE 1
        //STUDENT 1
        rsc.buyCharacterCard(2);
        Parameters params = new Parameters();
        params.setSelectedColor(Color.RED);
        rsc.setCardParameters(params);
        rsc.activateCard();
        moveThreeStudentsSomewhere(rsc);
        controller.nextState();
        rsc = controller.getStateController();
        //MN 1
        rsc.moveMN(1);
        controller.nextState();
        rsc = controller.getStateController();
        //CLOUD 1
        rsc.collectFromCloud(0);
        controller.nextState();

        rsc = controller.getStateController();
        //ACTION PHASE 2
        //STUDENT 2
        moveThreeStudentsSomewhere(rsc);
        controller.nextState();
        rsc = controller.getStateController();
        //MN 2
        rsc.buyCharacterCard(1);
        Parameters params1 = new Parameters();
        params1.setBoostedTeam(
                game.getPlayerByID(matchInfo.getCurrentPlayerID()).getTeamColor()
        );
        params1.setSelectedColor(Color.RED);
        rsc.setCardParameters(params1);
        //rsc.activateCard();
        rsc.moveMN(1);
        controller.nextState();
        rsc = controller.getStateController();
        //CLOUD 2
        rsc.collectFromCloud(1);
        controller.nextState();
        rsc = controller.getStateController();


    }

    private void moveThreeStudentsSomewhere(RoundStateController rsc) throws Exception{
        int moved = 0;
        Random r = new Random();
        School curr = game.getBoard().getSchoolByPlayerID(matchInfo.getCurrentPlayerID());

        while(moved < 3){
            for(Color c : Color.values()) {
                if(r.nextInt(2) == 0){
                    //Move to random island
                    int island = r.nextInt(game.getBoard().getNumIslands());
                    if(curr.getNumStudentsInEntranceByColor(c) > 0){
                        rsc.transferStudentToIsland(island, c);
                        moved++;
                    }
                }else{
                    //Move to dining
                    if(curr.getNumStudentsInEntranceByColor(c) > 0){
                        rsc.transferStudentToDiningRoom(c);
                        moved++;
                    }
                }

                if(moved == 3){
                    break;
                }
            }
        }
    }
}
*/