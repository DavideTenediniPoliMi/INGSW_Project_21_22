package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.CardParameters;

/**
 * Subcontroller for influence computation in expert mode.
 */
public class IslandExpertController extends IslandController {
    private final CharacterCardController characterCardController;

    /**
     * Sole constructor, takes a <code>CharacterCardController</code> to check whether there are active <code>CharacterCards</code>
     * during influence computation.
     *
     * @param characterCardController the <code>CharacterCardController</code>.
     */
    public IslandExpertController(CharacterCardController characterCardController) {
        this.characterCardController = characterCardController;
    }

    /**
     * Returns the influence of the specified <code>TowerColor</code> on the <code>Island</code> where Mother Nature
     * is currently on. Considers <code>CharacterCard</code> effects as well.
     *
     * @param teamColor the color of the team.
     * @return Influence score of the specified team.
     */
    @Override
    protected int getInfluenceOf(TowerColor teamColor) {
        Game game = Game.getInstance();
        int targetIslandIndex = game.getBoard().getMNPosition();
        int adjustedScore = super.getInfluenceOf(teamColor);

        if(characterCardController.isActiveCardOfType(EffectType.ALTER_INFLUENCE)) {
            CardParameters params = new CardParameters();
            params.setCurrentTeam(teamColor);
            params.setIslandIndex(targetIslandIndex);
            game.getActiveCharacterCard().setParameters(params);

            adjustedScore += characterCardController.activateCard();
        }

        return adjustedScore;
    }
}
