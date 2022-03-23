package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;

public class AlterInfluenceDecorator extends CharacterCardDecorator{
    private Color selectedColor;
    private TowerColor teamColor;
    private ArrayList<Integer>blockedIslands;
    private int toUnlock = -1;

    private final boolean isAddTwo, isIgnoreTowers, isIgnoreColor, isNoEntry;

    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor, boolean isNoEntry){
        super(card);

        this.isAddTwo = isAddTwo;
        this.isIgnoreTowers = isIgnoreTowers;
        this.isIgnoreColor = isIgnoreColor;
        this.isNoEntry = isNoEntry;

        if(isNoEntry){
            blockedIslands = new ArrayList<>();
        }
    }

    public void activate(){
        card.activate();

        if(isIgnoreColor){
            //TODO ask view
        }else if(isNoEntry){
            //TODO ask view
        }
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
            else if(isNoEntry){
                if(blockedIslands.contains(islandIndex)){
                    newScore = 0;

                    if(toUnlock == -1){
                        toUnlock = islandIndex;
                    }
                }
            }
        }

        return newScore;
    }

    public void clearEffect(){
        if(!isNoEntry || blockedIslands.isEmpty()){
            card.clearEffect();
        }
        if(toUnlock > -1){
            unlockIsland(toUnlock);
            toUnlock = -1;
        }
    }

    public void blockIsland(int index){
        this.blockedIslands.add(index);
    }

    public void setColorToIgnore(Color c){
        this.selectedColor = c;
    }

    public void setBoostedTeam(TowerColor team){
        this.teamColor = team;
    }

    private void unlockIsland(int islandIndex){
        this.blockedIslands.remove((Object)islandIndex);
    }
}
