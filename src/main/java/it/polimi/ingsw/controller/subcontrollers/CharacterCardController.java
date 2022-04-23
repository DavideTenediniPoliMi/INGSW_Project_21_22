package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.CharacterCardActivationException;
import it.polimi.ingsw.exceptions.game.NullCharacterCardException;
import it.polimi.ingsw.exceptions.game.NullPlayerException;
import it.polimi.ingsw.exceptions.player.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.students.NotEnoughStudentsException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Subcontroller handling all actions related to Character Card purchasing, setting, and activation.
 */
public class CharacterCardController {
    private boolean effectUsed;

    /**
     * Sole constructor.
     */
    public CharacterCardController() {
        effectUsed = false;
    }

    /**
     * Buys the specified <code>CharacterCard</code> for the specified <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code> buying the card.
     * @param cardIndex the Index of the <code>CharacterCard</code> to buy.
     * @throws NotEnoughCoinsException If the <code>Player</code> doesn't have enough coins to buy the specified card.
     */
    public void buyCharacterCard(int playerID, int cardIndex) throws NotEnoughCoinsException, CharacterCardActivationException {
        if(Game.getInstance().getActiveCharacterCard() == null) {
            Player player = Game.getInstance().getPlayerByID(playerID);
            CharacterCard card = Game.getInstance().getCharacterCards().get(cardIndex);

            if(player.getNumCoins() >= card.getCost()) {
                effectUsed = false;
                Game.getInstance().buyCharacterCard(playerID, cardIndex);
            }else {
                throw new NotEnoughCoinsException(player.getNumCoins(), card.getCost());
            }
        }else {
            throw new CharacterCardActivationException(CharacterCards.values()[cardIndex].toString(),
                    Game.getInstance().getActiveCharacterCard().toString());
        }
    }

    /**
     * Sets the specified <code>Parameters</code> for the active <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to set.
     */
    public void setCardParameters(Parameters params) {
        if(Game.getInstance().getActiveCharacterCard() != null){ //Separated so it's only checked once
            try{
                if(checkParameters(params) && !effectUsed){
                    Game.getInstance().setCardParameters(params);
                }
            } catch (BadParametersException | NullPlayerException | NotEnoughStudentsException exc) {
                exc.printStackTrace();
            }
        }else {
            throw new NullCharacterCardException();
        }
    }

    /**
     * Returns whether the active <code>CharacterCard</code> is of the specified <code>EffectType</code>.
     *
     * @param effectType the <code>EffectType</code>.
     * @return <code>true</code> if the active card is of the specified type.
     */
    public boolean isActiveCardOfType(EffectType effectType) {
        CharacterCard card = Game.getInstance().getActiveCharacterCard();
        if(card != null) {
            return card.getEffectType().equals(effectType);
        }else {
            return false;
        }
    }


    /**
     * Activates the effect of the active <code>CharacterCard</code>.
     *
     * @return The result of the effect of the <code>CharacterCard</code>.
     */
    public int activateCard() {
        effectUsed = true;
        return Game.getInstance().activateCard();
    }

    /**
     * Clears the effects of the active <code>CharacterCard</code> if present.
     */
    public void clearEffects() {
        CharacterCard card = Game.getInstance().getActiveCharacterCard();
        if(card != null) {
            card.clearEffect();
        }else {
            throw new NullCharacterCardException();
        }
    }

    /**
     * Returns whether the specified <code>Parameters</code> suit the active <code>CharacterCard</code>.
     *
     * @param params the <code>Parameters</code> to check.
     * @return <code>true</code> if the <code>Parameters</code> are correct.
     * @throws BadParametersException If one or more parameter is missing.
     * @throws NullPlayerException If the specified playerID is invalid.
     */
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
                                                 + " boostedTeam: " + boostedTeam);
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
                        + " fromEntrance: " + fromEntrance
                        + " playerID: " + playerID
                        + " islandIndex: " + islandIndex);
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
                                                 + " fromDiningRoom: " + fromDiningRoom
                                                 + " playerID: " + playerID_ex);
            default:
                return false;
        }
    }
}
