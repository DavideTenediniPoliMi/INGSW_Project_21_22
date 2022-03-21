package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class StudentGroupDecorator extends CharacterCardDecorator{
    private final int SWAP_CARD_STUDENTS = 6;
    private final int TRANSFER_CARD_STUDENTS = 4;

    private StudentGroup students;
    private boolean toIslands, toDiningRoom;

    public StudentGroupDecorator(GenericCard card, boolean toIslands, boolean toDiningRoom){
        super(card);

        this.toIslands = toIslands;
        this.toDiningRoom = toDiningRoom;

        if(!toIslands && !toDiningRoom){
            students = StudentBag.getBag().drawStudents(SWAP_CARD_STUDENTS);
        }else{
            students = StudentBag.getBag().drawStudents(TRANSFER_CARD_STUDENTS);
        }
    }

    public void activate(){
        if(toIslands){
            //TODO ask view
        }else if(toDiningRoom){
            //TODO ask view
        }else{
            //TODO ask view
        }
    }

    public void transferTo(Object destination, StudentGroup[] selectedStudents){
        if(card.isActive()){
            StudentGroup tmp = new StudentGroup();
            if(toIslands && destination instanceof Island){
                Island i = (Island) destination;
                students.transferTo(tmp, selectedStudents[0]);
                Game.getInstance().getBoard().addStudentsToIsland(tmp, i);
            }
            else if(destination instanceof School){
                School s = (School) destination;
                int playerID = s.getOwner().getID();
                if(toDiningRoom){
                    students.transferTo(tmp, selectedStudents[0]);

                    Game.getInstance().getBoard().addToDiningRoomOf(playerID, tmp);
                }else{
                    if(selectedStudents.length > 1){
                        students.transferTo(tmp, selectedStudents[0]);

                        Game.getInstance().getBoard().removeFromEntranceOf(playerID, selectedStudents[1]);
                        Game.getInstance().getBoard().addToEntranceOf(playerID, tmp);

                        selectedStudents[1].transferAllTo(students);
                    }
                }
            }
        }
    }

    private void refillStudents(){

    }
}
