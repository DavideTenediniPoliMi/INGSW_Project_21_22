package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class ExchangeStudentsDecorator extends CharacterCardDecorator {
    private StudentGroup fromEntrance;
    private StudentGroup fromDiningRoom;
    private int playerID;

    public ExchangeStudentsDecorator(CharacterCard card) {
        super(card);
    }

    @Override
    public void setParameters(Parameters params) {
        playerID = params.getPlayerID();

        fromEntrance = params.getFromOrigin();
        fromDiningRoom = params.getFromDestination();
    }

    @Override
    public int activate() {
        card.activate();

        Board board = Game.getInstance().getBoard();

        board.removeFromEntranceOf(playerID, fromEntrance);
        board.removeFromDiningRoomOf(playerID, fromDiningRoom);

        board.addToEntranceOf(playerID, fromDiningRoom);
        board.addToDiningRoomOf(playerID, fromEntrance);

        return -1;
    }
}
