package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.Parameters;

public class AlterInfluenceDecorator extends CharacterCardDecorator{
    private Color selectedColor;
    private TowerColor teamColor;

    private final boolean isAddTwo, isIgnoreTowers, isIgnoreColor;

    private TowerColor currentTeam;
    private int islandIndex;

    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor){
        super(card);

        this.isAddTwo = isAddTwo;
        this.isIgnoreTowers = isIgnoreTowers;
        this.isIgnoreColor = isIgnoreColor;
    }

    public int activate(){
        card.activate();
        int delta = 0;

        if(isAddTwo && currentTeam.equals(teamColor)){
            delta += 2;
        }else if(isIgnoreTowers){
            Island i = Game.getInstance().getBoard().getIsland(islandIndex);

            if(i.getTeamColor().equals(currentTeam)){
                delta -= i.getNumIslands();
            }
        }else if(isIgnoreColor){
            int colorOwnerID = Game.getInstance().getBoard().getProfessorOwners().getOwnerIDByColor(selectedColor);

            if(Game.getInstance().getPlayerByID(colorOwnerID).getTeamColor().equals(currentTeam)){
                Island i = Game.getInstance().getBoard().getIsland(islandIndex);
                delta -= i.getNumStudentsByColor(selectedColor);
            }
        }

        return delta;
    }

    public void clearEffect(){
        card.clearEffect();
    }

    /*
    * Sets parameters for the AlterInfluence Card
    * If the card needs teamColor or selectedColor, those are set only the FIRST time setParameters is called
    *
    * Every other time params.getSelectedColor() || params.getTeamColor() NEED to return null
    * (For example when calculating scores for each team)
    * */
    @Override
    public void setParameters(Parameters params){
        if(isIgnoreTowers){
            setCurrent(params); //Might set null params when first called (During first phase of player's turn)
        }else if(isIgnoreColor){
            if(params.getSelectedColor() == null){
                setCurrent(params);
            }else{
                this.selectedColor = params.getSelectedColor();
            }
        }else if(isAddTwo){
            if(params.getTeamColor() == null){
                setCurrent(params);
            }else{
                this.teamColor = params.getTeamColor();
            }
        }
    }

    private void setCurrent(Parameters params){
        this.currentTeam = params.getCurrentTeam();
        this.islandIndex = params.getIslandIndex();
    }

}
