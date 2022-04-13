package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;

/**
 * Class to manage the behaviour of 3 <code>CharacterCard</code> responsible for
 * altering the influence calculated on an <code>Island</code>. Specifically, the
 * cards are <code>INFLUENCE_ADD_TWO</code>, <code>IGNORE_TOWERS</code> and
 * <code>IGNORE_COLOR</code>.
 */
public class AlterInfluenceDecorator extends CharacterCardDecorator {
    private TowerColor boostedTeam;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int islandIndex;
    private final boolean isAddTwo, isIgnoreTowers, isIgnoreColor;

    /**
     * Constructor that instantiates this card specifying which one of the 3 it is.
     *
     * @param card           the <code>GenericCard</code> used in the decoration process
     * @param isAddTwo       the flag to indicate whether this card is a <code>INFLUENCE_ADD_TWO</code>
     * @param isIgnoreTowers the flag to indicate whether this card is a <code>IGNORE_TOWERS</code>
     * @param isIgnoreColor  the flag to indicate whether this card is a <code>IGNORE_COLOR</code>
     */
    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor) {
        super(card);

        this.isAddTwo = isAddTwo;
        this.isIgnoreTowers = isIgnoreTowers;
        this.isIgnoreColor = isIgnoreColor;
    }

    /**
     * Sets necessary parameters for this card to be used. When this card is bought <code>setParameters<code/> must
     * receive either a valid <code>selectedColor<code/> (if <code>isIgnoreColor == true<code/>) or a valid
     * <code>boostedTeam<code/> (if <code>isAddTwo == true<code/>). The following calls for
     * <code>setParameters<code/> must receive a valid <code>currentTeam<code/> and <code>islandIndex<code/>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    @Override
    public void setParameters(Parameters params) {
        if(isIgnoreColor && params.getSelectedColor() != null) {
            selectedColor = params.getSelectedColor();
            return;
        }else if(isAddTwo && params.getBoostedTeam() != null){
            boostedTeam = params.getBoostedTeam();
            return;
        }

        currentTeam = params.getCurrentTeam();
        islandIndex = params.getIslandIndex();
    }

    /**
     * Computes the difference in influence score that this active card is applying.
     *
     * @return the influence score delta this active card imposing (pos(+) for <code>INFLUENCE_ADD_TWO</code>,
     * neg(-) for <code>IGNORE_TOWERS</code> and <code>IGNORE_COLOR</code>)
     */
    @Override
    public int activate() {
        card.activate();

        Game game = Game.getInstance();
        Board board = game.getBoard();

        int delta = 0;

        if(isAddTwo && currentTeam.equals(boostedTeam)) {
            delta += 2;
        }else if(isIgnoreTowers) {
            Island island = board.getIslandAt(islandIndex);

            if(island.getTeamColor().equals(currentTeam)) {
                delta -= island.getNumIslands();
            }
        }else if(isIgnoreColor) {
            int professorOwnerID = board.getProfessorOwners().getOwnerIDByColor(selectedColor);

            if(professorOwnerID == -1)  {
                return 0; // will be handled by exceptions
            }

            Player professorOwner = game.getPlayerByID(professorOwnerID);

            if(professorOwner.getTeamColor().equals(currentTeam)) {
                Island island = board.getIslandAt(islandIndex);
                delta -= island.getNumStudentsByColor(selectedColor);
            }
        }

        return delta;
    }
}
