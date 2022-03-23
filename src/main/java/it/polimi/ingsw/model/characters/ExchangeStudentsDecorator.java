package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class ExchangeStudentsDecorator extends CharacterCardDecorator{
    private int playerID;
    private final StudentGroup fromEntrance;
    private final StudentGroup fromDiningRoom;

    public ExchangeStudentsDecorator(CharacterCard card){
        super(card);
        this.fromEntrance = new StudentGroup();
        this.fromDiningRoom = new StudentGroup();
    }

    public int activate(){
        card.activate();
        Board board = Game.getInstance().getBoard();

        board.removeFromEntranceOf(playerID, fromEntrance);
        board.removeFromDiningRoomOf(playerID, fromDiningRoom);

        board.addToEntranceOf(playerID, fromDiningRoom);
        board.addToDiningRoomOf(playerID, fromEntrance);

        return -1;
    }

    public void setParameters(Parameters params){
        this.playerID = params.getPlayerIndex();

        params.getFromOrigin().transferAllTo(fromEntrance);
        params.getFromDestination().transferAllTo(fromDiningRoom);
    }
}
