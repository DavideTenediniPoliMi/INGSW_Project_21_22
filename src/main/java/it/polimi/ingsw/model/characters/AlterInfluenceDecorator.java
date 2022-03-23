package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;

public class AlterInfluenceDecorator extends CharacterCardDecorator {
    private TowerColor boostedTeam;
    private TowerColor currentTeam;
    private Color selectedColor;
    private int islandIndex;
    private final boolean isAddTwo, isIgnoreTowers, isIgnoreColor;

    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor) {
        super(card);

        this.isAddTwo = isAddTwo;
        this.isIgnoreTowers = isIgnoreTowers;
        this.isIgnoreColor = isIgnoreColor;
    }

    /*
    * Sets parameters for the AlterInfluence Card
    * If the card needs teamColor or selectedColor, those are set only the FIRST time setParameters is called
    *
    * Every other time params.getSelectedColor() || params.getTeamColor() NEED to return null
    * (For example when calculating scores for each team)
    * */
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

    @Override
    public int activate() {
        card.activate();

        int delta = 0;

        if(isAddTwo && currentTeam.equals(boostedTeam)) {
            delta += 2;
        }else if(isIgnoreTowers) {
            Island island = Game.getInstance().getBoard().getIsland(islandIndex);

            if(island.getTeamColor().equals(currentTeam)) {
                delta -= island.getNumIslands();
            }
        }else if(isIgnoreColor) {
            Game game = Game.getInstance();
            Board board = game.getBoard();

            int professorOwnerID = board.getProfessorOwners().getOwnerIDByColor(selectedColor);

            if(professorOwnerID == -1)  {
                return 0; // will be handled by exceptions
            }

            Player professorOwner = game.getPlayerByID(professorOwnerID);

            if(professorOwner.getTeamColor().equals(currentTeam)) {
                Island island = board.getIsland(islandIndex);
                delta -= island.getNumStudentsByColor(selectedColor);
            }
        }

        return delta;
    }
}
