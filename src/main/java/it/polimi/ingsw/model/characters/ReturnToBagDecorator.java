package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class ReturnToBagDecorator extends CharacterCardDecorator{
    private final int NUM_STUDENTS_TO_RETURN = 3;

    public ReturnToBagDecorator(CharacterCard card){
        super(card);
    }

    public void activate(){
        card.activate();
    }

    public void returnStudent(Color c){
        Game game = Game.getInstance();
        Board board = game.getBoard();

        for(Player player: game.getPlayers()){
            School targetSchool = board.getSchoolByPlayerID(player.getID());
            int numAvailableStudents = targetSchool.getNumStudentsInDiningRoomByColor(c);
            int numStudentsToReturn = Math.min(NUM_STUDENTS_TO_RETURN, numAvailableStudents);

            StudentGroup temp = new StudentGroup(c, numStudentsToReturn);

            board.removeFromDiningRoomOf(player.getID(), temp);
            StudentBag.getBag().putBack(temp);
        }
    }
}
