package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;

public class TiebreakerDecorator extends CharacterCardDecorator{

    public TiebreakerDecorator(GenericCard card){
        super(card);
    }

    public void activate(){
        card.activate();
    }

    public void checkForColor(int playerID, Color color){
        if(isActive()){
            Board b = Game.getInstance().getBoard();
            int amtOfPlayer = b.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(color);
            int ownerID = b.getProfessorOwners().getOwnerIDByColor(color);

            if(amtOfPlayer == b.getSchoolByPlayerID(ownerID).getNumStudentsInDiningRoomByColor(color)){
                b.giveProfessorTo(playerID, color);
            }
        }
    }
}
