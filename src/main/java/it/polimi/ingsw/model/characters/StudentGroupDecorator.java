package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class StudentGroupDecorator extends CharacterCardDecorator{
    private final int SWAP_CARD_STUDENTS = 6;
    private final int TRANSFER_CARD_STUDENTS = 4;

    private final StudentGroup students;
    private final boolean isToIsland, isToDiningRoom;
    private final StudentGroup fromCard = new StudentGroup();
    private final StudentGroup fromEntrance = new StudentGroup();
    private int islandIndex, playerIndex;

    public StudentGroupDecorator(GenericCard card, boolean isToIsland, boolean isToDiningRoom){
        super(card);

        this.isToIsland = isToIsland;
        this.isToDiningRoom = isToDiningRoom;

        if(!isToIsland && !isToDiningRoom){
            students = StudentBag.getBag().drawStudents(SWAP_CARD_STUDENTS);
        }else{
            students = StudentBag.getBag().drawStudents(TRANSFER_CARD_STUDENTS);
        }
    }

    public int activate(){
        card.activate();
        StudentGroup temp = new StudentGroup();

        students.transferTo(temp, fromCard);

        if(isToIsland){
            Game.getInstance().getBoard().addStudentsToIsland(fromCard, islandIndex);

            refillStudents();
        }else if(isToDiningRoom){
            Game.getInstance().getBoard().addToDiningRoomOf(playerIndex, fromCard);

            refillStudents();
        }else{
            Game.getInstance().getBoard().removeFromEntranceOf(playerIndex, fromEntrance);
            Game.getInstance().getBoard().addToEntranceOf(playerIndex, temp);

            fromEntrance.transferAllTo(students);
        }

        return -1;
    }

    private void refillStudents(){
        StudentBag.getBag().drawStudents(1).transferAllTo(students);
    }

    public void setParameters(Parameters params){
        params.getFromOrigin().transferAllTo(fromCard);
        if(isToIsland) {
            this.islandIndex = params.getIslandIndex();
        }else if(isToDiningRoom){
            this.playerIndex = params.getPlayerIndex();
        }else{
            params.getFromDestination().transferAllTo(fromEntrance);
            this.playerIndex = params.getPlayerIndex();
        }
    }
}
