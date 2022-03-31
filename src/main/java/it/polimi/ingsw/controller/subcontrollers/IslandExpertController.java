package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;

public class IslandExpertController extends IslandController {
    private final CharacterCardController characterCardController;

    public IslandExpertController(CharacterCardController characterCardController) {
        this.characterCardController = characterCardController;
    }

    @Override
    protected int getInfluenceOf(TowerColor teamColor) {
        Game game = Game.getInstance();
        int targetIslandIndex = game.getBoard().getMNPosition();
        int adjustedScore = super.getInfluenceOf(teamColor);

        if(characterCardController.isActiveCardOfType(EffectType.ALTER_INFLUENCE)) {
            Parameters params = new Parameters();
            params.setCurrentTeam(teamColor);
            params.setIslandIndex(targetIslandIndex);
            characterCardController.setCardParameters(params);

            adjustedScore += characterCardController.activateCard();
        }

        return adjustedScore;
    }
}
