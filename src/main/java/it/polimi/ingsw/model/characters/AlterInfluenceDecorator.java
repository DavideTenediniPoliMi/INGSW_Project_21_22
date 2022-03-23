package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

public class AlterInfluenceDecorator extends CharacterCardDecorator{
    private Color selectedColor;
    private TowerColor teamColor;
    private int toUnlock = -1;

    private final boolean isAddTwo, isIgnoreTowers, isIgnoreColor;

    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor){
        super(card);

        this.isAddTwo = isAddTwo;
        this.isIgnoreTowers = isIgnoreTowers;
        this.isIgnoreColor = isIgnoreColor;
    }

    public int activate(){
        card.activate();

        return 0;
    }

    public int getAlteredInfluence(TowerColor team, int influenceScore, int islandIndex){
        int newScore = influenceScore;

        if(card.isActive()){
            if(isAddTwo && team.equals(teamColor)){
                newScore += 2;
            }
            else if(isIgnoreTowers){
                Island i = Game.getInstance().getBoard().getIsland(islandIndex);
                if(i.getTeamColor().equals(team)){
                    //newScore -= i.getNumTowers();
                }
            }
            else if(isIgnoreColor){
                int colorOwnerID = Game.getInstance().getBoard().getProfessorOwners().getOwnerIDByColor(selectedColor);

                if(Game.getInstance().getPlayerByID(colorOwnerID).getTeamColor().equals(team)){
                    Island i = Game.getInstance().getBoard().getIsland(islandIndex);
                    newScore -= i.getNumStudentsByColor(selectedColor);
                }
            }
        }

        return newScore;
    }

    public void clearEffect(){
        card.clearEffect();
    }

    public void setColorToIgnore(Color c){
        this.selectedColor = c;
    }

    public void setBoostedTeam(TowerColor team){
        this.teamColor = team;
    }

}
