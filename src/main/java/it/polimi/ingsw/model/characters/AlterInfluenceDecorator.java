package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;

public class AlterInfluenceDecorator extends CharacterCardDecorator{
    private Color selectedColor;
    private TowerColor teamColor;
    private ArrayList<Island>blockedIslands;
    private int toUnlock = -1;

    private boolean isAddTwo, isIgnoreTowers, isIgnoreColor, isNoEntry = false;

    public AlterInfluenceDecorator(GenericCard card, boolean isAddTwo, boolean isIgnoreTowers, boolean isIgnoreColor, boolean isNoEntry){
        super(card);
    }

    public void activate(){

    }

    public int getAlteredInfluence(TowerColor team, int influenceScore, int islandIndex){
        return -1; //TODO
    }

    public void clearEffect(){

    }

    public void blockIsland(){

    }

    public void setColorToIgnore(){

    }

    public void setBoostedTeam(TowerColor team){

    }

    private void unlockIsland(int index){

    }
}
