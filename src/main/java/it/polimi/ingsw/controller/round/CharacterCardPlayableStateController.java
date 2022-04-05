package it.polimi.ingsw.controller.round;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class CharacterCardPlayableStateController extends RoundStateController {
    StudentGroup fromOrigin;

    public CharacterCardPlayableStateController(RoundStateController oldState, TurnState stateType) {
        super(oldState, stateType);
    }

    @Override
    public void buyCharacterCard(int cardIndex) {
        if(cardIndex < 0 || cardIndex > 2) {
            //BAD PARAMETERS EXCEPTION
        }
        characterCardController.buyCharacterCard(getCurrentPlayerID(), cardIndex);
        fromOrigin = null;
    }

    @Override
    public void setCardParameters(Parameters params) {
        //TODO surround with try/catch
        characterCardController.setCardParameters(params);
        CharacterCard card = Game.getInstance().getActiveCharacterCard();

        if(card.getEffectType().equals(EffectType.EXCHANGE_STUDENTS) || card.getEffectType().equals(EffectType.STUDENT_GROUP)){
            fromOrigin = params.getFromOrigin().clone();
        }
    }

    @Override
    public void activateCard() {
        int res = characterCardController.activateCard();
        CharacterCard card = Game.getInstance().getActiveCharacterCard();

        if(card.getEffectType().equals(EffectType.EXCHANGE_STUDENTS) || card.getEffectType().equals(EffectType.STUDENT_GROUP)){
            if(res == -1){ //If card has altered someone's dining room
                diningRoomController.manageDiningRoomOf(getCurrentPlayerID(), fromOrigin);
            }
        }
    }
}
