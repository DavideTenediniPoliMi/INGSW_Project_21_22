package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class ExchangeStudentsDecorator extends CharacterCardDecorator{

    public ExchangeStudentsDecorator(CharacterCard card){
        super(card);
    }

    public void activate(){
        card.activate();
    }

    public void swap(int playerID, StudentGroup fromEntrance, StudentGroup fromDiningRoom){
        Board board = Game.getInstance().getBoard();

        board.removeFromEntranceOf(playerID, fromEntrance);
        board.removeFromDiningRoomOf(playerID, fromDiningRoom);
        board.addToEntranceOf(playerID, fromDiningRoom);
        board.addToDiningRoomOf(playerID, fromEntrance);
    }
}
