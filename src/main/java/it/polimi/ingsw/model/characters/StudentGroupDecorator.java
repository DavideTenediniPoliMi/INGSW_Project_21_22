package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.Parameters;
import it.polimi.ingsw.model.helpers.StudentGroup;

public class StudentGroupDecorator extends CharacterCardDecorator {
    private final int SWAP_CARD_STUDENTS = 6;
    private final int TRANSFER_CARD_STUDENTS = 4;

    private final StudentGroup students;
    private StudentGroup fromCard;
    private StudentGroup fromEntrance;
    private int islandIndex, playerID;
    private final boolean isToIsland, isToDiningRoom;

    public StudentGroupDecorator(GenericCard card, boolean isToIsland, boolean isToDiningRoom) {
        super(card);

        this.isToIsland = isToIsland;
        this.isToDiningRoom = isToDiningRoom;

        if(!isToIsland && !isToDiningRoom) {
            students = Game.getInstance().drawStudents(SWAP_CARD_STUDENTS);
        } else {
            students = Game.getInstance().drawStudents(TRANSFER_CARD_STUDENTS);
        }
    }

    @Override
    public void setParameters(Parameters params) {
        fromCard = params.getFromOrigin();

        if(isToIsland) {
            islandIndex = params.getIslandIndex();
        } else if(isToDiningRoom) {
            playerID = params.getPlayerID();
        } else {
            playerID = params.getPlayerID();
            fromEntrance = params.getFromDestination();
        }
    }

    @Override
    public int activate() {
        card.activate();

        Game game = Game.getInstance();
        Board board = game.getBoard();
        StudentGroup temp = new StudentGroup();

        students.transferTo(temp, fromCard);

        if(isToIsland){
            board.addStudentsToIsland(islandIndex, fromCard);

            game.drawStudents(1).transferAllTo(students);
        } else if(isToDiningRoom) {
            board.addToDiningRoomOf(playerID, fromCard);

            game.drawStudents(1).transferAllTo(students);
        } else {
            board.removeFromEntranceOf(playerID, fromEntrance);
            board.addToEntranceOf(playerID, temp);

            fromEntrance.transferAllTo(students);
        }

        return 0;
    }

    public int getStudentsByColor(Color color) {
        return students.getByColor(color);
    }
}
