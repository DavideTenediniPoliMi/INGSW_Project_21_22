package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandExpertControllerTest {

    @Test
    void getInfluenceOf() {
        IslandExpertController islandExpertController = new IslandExpertController(new CharacterCardController());
        Game game = Game.getInstance();
        Board board = game.getBoard();

        game.addPlayer(new Player(0, "mar", TowerColor.BLACK, CardBack.CB_2, true));
        board.addSchool(game.getPlayerByID(0));

        game.placeMNAt(0);
        board.addStudentsToIsland(0, new StudentGroup(Color.BLUE, 2));
        game.giveProfessorTo(0, Color.BLUE);

        game.instantiateCharacterCard(3);

        CardParameters params = new CardParameters();
        params.setBoostedTeam(TowerColor.BLACK);

        game.buyCharacterCard(0, 0);
        game.setCardParameters(params);

        int oldInfluence = 3;
        islandExpertController.moveMN(12);

        assertEquals(oldInfluence + 2, islandExpertController.getInfluenceOf(TowerColor.BLACK));
    }
}