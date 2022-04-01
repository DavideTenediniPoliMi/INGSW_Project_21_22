package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;

public class DiningRoomExpertController extends DiningRoomController{

    public DiningRoomExpertController() {

    }

    @Override
    public void manageDiningRoomOf(int playerID, Color c) {
        super.manageDiningRoomOf(playerID, c);
        checkCoin(playerID, c);
    }

    private void checkCoin(int playerID, Color c) {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        int numStuds = board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
        if(numStuds > 0 && numStuds % 3 == 0)
            game.giveCoinToPlayer(playerID);
    }
}
