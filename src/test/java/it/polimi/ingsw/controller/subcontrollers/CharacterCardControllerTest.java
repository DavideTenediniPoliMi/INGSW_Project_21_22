package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.controller.round.CharacterCardPlayableStateController;
import it.polimi.ingsw.controller.round.RoundStateController;
import it.polimi.ingsw.controller.round.StudentsStateController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.NullCharacterCardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.events.StartDocument;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardControllerTest {
    RoundStateController rc;
    CharacterCardPlayableStateController cardController;
    Game g;
    MatchInfo matchInfo;

    @BeforeEach
    void setUp() {
        rc = new RoundStateController(new IslandController(), new DiningRoomController());
        cardController = new CharacterCardPlayableStateController(rc,TurnState.STUDENTS);

        g = Game.getInstance();
        g.addPlayer(0, "lele", TowerColor.GREY, CardBack.CB_3, true);
        g.giveCoinToPlayer(0);
        matchInfo = MatchInfo.getInstance();
        matchInfo.addPlayer(0);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
        MatchInfo.resetInstance();
        rc = null;
        cardController = null;
    }

    @Test
    void buyCharacterCard_alreadyInstantiatedCard() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);

        assertThrowsExactly(NullCharacterCardException.class, () -> cardController.buyCharacterCard(0));
    }

    @Test
    void buyCharacterCard_notEnoughCoins() {
        g.instantiateCharacterCard(1);
        cardController.buyCharacterCard(0);
    }

    @Test
    void buyCharacterCard_successfulBuy() {
        g.instantiateCharacterCard(0);
        assertDoesNotThrow(() -> cardController.buyCharacterCard(0));
    }

    @Test
    void setCardParameters_activeCardIsNull() {
        assertThrowsExactly(NullCharacterCardException.class, () -> cardController.setCardParameters(new Parameters()));
    }

    @Test
    void setCardParameters_effectUsed() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);

        Parameters params = new Parameters();
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);


        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_StudentGroup_BadParameters() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);

        assertThrowsExactly(NullPointerException.class, () -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_StudentGroup_NullPlayer() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setPlayerID(-1);
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_AlterInfluence() {
        g.instantiateCharacterCard(3);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setSelectedColor(Color.PINK);
        params.setBoostedTeam(TowerColor.BLACK);

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_AlterInfluence_BadParameters() {
        g.instantiateCharacterCard(3);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ReturnToBag() {
        g.instantiateCharacterCard(7);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setSelectedColor(Color.BLUE);

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ReturnToBag_BadParameters() {
        g.instantiateCharacterCard(7);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents() {
        g.instantiateCharacterCard(6);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents_BadParameters() {
        g.instantiateCharacterCard(6);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();

        assertThrowsExactly(NullPointerException.class, () -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_checkParametersFail_ExchangeStudents_NullPlayer() {
        g.instantiateCharacterCard(6);
        g.giveCoinToPlayer(0);
        g.giveCoinToPlayer(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setPlayerID(-1);
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));

        assertDoesNotThrow(() -> cardController.setCardParameters(params));
    }

    @Test
    void setCardParameters_parametersSet() {
        assertThrowsExactly(NullCharacterCardException.class, () -> cardController.setCardParameters(new Parameters()));
    }

    @Test
    void isActiveCardOfType_cardIsNull() {
        CharacterCardController characterCardController = new CharacterCardController();

        assertThrowsExactly(NullCharacterCardException.class, () -> characterCardController.isActiveCardOfType(EffectType.STUDENT_GROUP));
    }

    @Test
    void isActiveCardOfType_cardNotNull() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);
        CharacterCardController characterCardController = new CharacterCardController();

        assertTrue(characterCardController.isActiveCardOfType(EffectType.STUDENT_GROUP));
    }

    @Test
    void activateCard() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);
        Parameters params = new Parameters();
        params.setFromOrigin(new StudentGroup(Color.BLUE, 2));
        params.setFromDestination(new StudentGroup(Color.GREEN, 2));
        params.setIslandIndex(0);
        cardController.setCardParameters(params);

        assertDoesNotThrow(() -> cardController.activateCard());
    }

    @Test
    void clearEffects_cardIsNull() {
        assertThrowsExactly(NullCharacterCardException.class, () -> cardController.clearEffects());
    }

    @Test
    void clearEffects_cardNotNull() {
        g.instantiateCharacterCard(0);
        g.buyCharacterCard(0,0);
        CharacterCard card = g.getActiveCharacterCard();

        assertTrue(card.isActive());
        cardController.clearEffects();
        assertFalse(card.isActive());
    }
}