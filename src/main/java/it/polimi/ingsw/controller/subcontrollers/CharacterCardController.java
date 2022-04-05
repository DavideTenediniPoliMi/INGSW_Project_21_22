package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class CharacterCardController {
    private boolean effectUsed;

    public CharacterCardController() {
        effectUsed = false;
    }

    public void buyCharacterCard(int playerID, int cardIndex) {
        if(Game.getInstance().getActiveCharacterCard() == null) {
            Player player = Game.getInstance().getPlayerByID(playerID);
            CharacterCard card = Game.getInstance().getCharacterCards().get(cardIndex);

            if(player.getNumCoins() >= card.getCost()) {
                effectUsed = false;
                Game.getInstance().buyCharacterCard(playerID, cardIndex);
            }//TODO throw notEnoughCoins
        }
    }

    public void setCardParameters(Parameters params) {
        if(Game.getInstance().getActiveCharacterCard() != null){ //Separated so it's only checked once
            try{
                if(checkParameters(params) && !effectUsed){
                    Game.getInstance().setCardParameters(params);
                }
            } catch (BadParametersException | NullPlayerException exc) {
                System.out.println("[EXCEPTION]" + exc);
            }
        }
    }

    /*
    * Returns false also if there is no active card, maybe change to exception?
    * */
    public boolean isActiveCardOfType(EffectType effectType) {
        CharacterCard card = Game.getInstance().getActiveCharacterCard();
        if(card != null) {
            return card.getEffectType().equals(effectType);
        }
        return false;
    }

    public int activateCard() {
        effectUsed = true;
        return Game.getInstance().activateCard();
    }

    /*
    * Throws BadParametersException ?
    * */
    private boolean checkParameters(Parameters params) throws BadParametersException, NullPlayerException {
        CharacterCard card = Game.getInstance().getActiveCharacterCard();

        switch (card.getEffectType()){
            case ALTER_INFLUENCE:
                Color selectedColor_infl = params.getSelectedColor();
                TowerColor boostedTeam = params.getBoostedTeam();

                if(selectedColor_infl != null && boostedTeam != null){
                    return true;
                }
                throw new BadParametersException("ALTER_INFLUENCE card. selectedColor: " + selectedColor_infl
                                                 + "boostedTeam: " + boostedTeam);
            case STUDENT_GROUP:
                StudentGroup fromCard = params.getFromOrigin();
                StudentGroup fromEntrance = params.getFromDestination();
                int playerID = params.getPlayerID();
                int islandIndex = params.getIslandIndex();

                if(fromCard != null && fromEntrance != null && playerID >= 0 && islandIndex >= 0){
                    return true;
                }

                if(playerID < 0){
                    throw new NullPlayerException();
                }

                throw new BadParametersException("STUDENT_GROUP card. fromCard: " + fromCard
                        + "fromEntrance: " + fromEntrance
                        + "playerID: " + playerID
                        + "islandIndex: " + islandIndex);
            case RETURN_TO_BAG:
                Color selectedColor_bag = params.getSelectedColor();

                if(selectedColor_bag != null){
                    return true;
                }
                throw new BadParametersException("RETURN_TO_BAG card. selectedColor is null");
            case EXCHANGE_STUDENTS:
                StudentGroup fromEntrance_ex = params.getFromOrigin();
                StudentGroup fromDiningRoom = params.getFromDestination();
                int playerID_ex = params.getPlayerID();

                if(fromEntrance_ex != null && fromDiningRoom != null && playerID_ex >= 0){
                    return true;
                }

                if(playerID_ex < 0) {
                    throw new NullPlayerException();
                }

                throw new BadParametersException("EXCHANGE_STUDENTS card. fromEntrance: " + fromEntrance_ex
                                                 + "fromDiningRoom: " + fromDiningRoom
                                                 + "playerID: " + playerID_ex);
            default:
                break;
        }
        return false;
    }
}
