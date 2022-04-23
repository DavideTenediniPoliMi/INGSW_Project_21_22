package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.controller.round.CharacterCardPlayableStateController;
import it.polimi.ingsw.controller.round.RoundStateController;
import it.polimi.ingsw.exceptions.game.CharacterCardActivationException;
import it.polimi.ingsw.exceptions.game.NullCharacterCardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardControllerTest {
    RoundStateController rc;
    CharacterCardPlayableStateController characterCardController;
    Game game;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        rc = new RoundStateController(new IslandController(), new DiningRoomController());
        characterCardController = new CharacterCardPlayableStateController(rc,TurnState.STUDENTS);

        game = Game.getInstance();
        game.addPlayer(0, "lele", TowerColor.GREY, CardBack.CB_3, true);
        game.giveCoinToPlayer(0);
        matchInfo = MatchInfo.getInstance();
        matchInfo.setExpertMode(true);
        matchInfo.addPlayer(0);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        MatchInfo.resetInstance();
        game = null;
        matchInfo = null;
        rc = null;
        characterCardController = null;
    }

    @Test
    void buyCharacterCard_alreadyBoughtCard() {
        game.instantiateCharacterCard(0);
        characterCardController.buyCharacterCard(0);
        System.out.println(game.getCharacterCards().get(0).getCost());

        int coinsBefore = game.getPlayerByID(0).getNumCoins();
        characterCardController.buyCharacterCard(0);
        assertEquals(coinsBefore, game.getPlayerByID(0).getNumCoins());
    }

    @Test
    void buyCharacterCard_notEnoughCoins() {
        game.instantiateCharacterCard(1);
        characterCardController.buyCharacterCard(0);
    }

    @Test
    void buyCharacterCard_successfulBuy() {
        game.instantiateCharacterCard(0);
        assertDoesNotThrow(() -> characterCardController.buyCharacterCard(0));
    }

    @Test
    void setCardParameters_activeCardIsNull() {
        assertThrowsExactly(NullCharacterCardException.class, () -> characterCardController.setCardParameters(new Parameters()));
    }

    @Test
    void setCardParameters_effectUsed() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);

        Parameters params = new Parameters();
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);


        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_StudentGroup_BadParameters() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);

        assertThrowsExactly(NullPointerException.class, () -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_StudentGroup_NullPlayer() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setPlayerID(-1);
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_AlterInfluence() {
        game.instantiateCharacterCard(3);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setSelectedColor(Color.PINK);
        params.setBoostedTeam(TowerColor.BLACK);

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_AlterInfluence_BadParameters() {
        game.instantiateCharacterCard(3);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ReturnToBag() {
        game.instantiateCharacterCard(7);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setSelectedColor(Color.BLUE);

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ReturnToBag_BadParameters() {
        game.instantiateCharacterCard(7);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents() {
        game.instantiateCharacterCard(6);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents_BadParameters() {
        game.instantiateCharacterCard(6);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertThrowsExactly(NullPointerException.class, () -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents_NullPlayer() {
        game.instantiateCharacterCard(6);
        game.giveCoinToPlayer(0);
        game.giveCoinToPlayer(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setPlayerID(-1);
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));

        assertDoesNotThrow(() -> characterCardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_parametersSet() {
        assertThrowsExactly(NullCharacterCardException.class, () -> characterCardController.setCardParameters(new Parameters()));
    }

    @Test
    void isActiveCardOfType_cardIsNull() {
        CharacterCardController characterCardController = new CharacterCardController();

        assertFalse(characterCardController.isActiveCardOfType(EffectType.STUDENT_GROUP));
    }

    @Test
    void isActiveCardOfType_cardNotNull() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);
        CharacterCardController characterCardController = new CharacterCardController();

        assertTrue(characterCardController.isActiveCardOfType(EffectType.STUDENT_GROUP));
    }

    @Test
    void activateCard() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        StudentGroup students = new StudentGroup();
        int amt = 0;

        for(Color c : Color.values()) {
            StudentGroupDecorator sgd = (StudentGroupDecorator) game.getActiveCharacterCard();
            if(sgd.getStudentsByColor(c) > 0) {
                students.addByColor(c, 1);
                amt++;
            }
        }

        params.setFromOrigin(students);
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);
        characterCardController.setCardParameters(params);

        assertDoesNotThrow(() -> characterCardController.activateCard());
    }

    @Test
    void clearEffects_cardNotNull() {
        game.instantiateCharacterCard(0);
        game.buyCharacterCard(0,0);
        CharacterCard card = game.getActiveCharacterCard();

        assertTrue(card.isActive());
        characterCardController.clearEffects();
        assertFalse(card.isActive());
    }
}