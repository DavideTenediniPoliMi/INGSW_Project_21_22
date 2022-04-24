package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.exceptions.students.NotEnoughStudentsException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.parameters.ActionResponseParameters;

/**
 * Class to manage the behaviour of 3 <code>CharacterCard</code> which hold a <code>StudentGroup<code/>, and
 * allow transfers to occur between this card and another game entity.
 * Specifically, the cards are <code>MOVE_TO_ISLAND</code>, <code>MOVE_TO_DINING_ROOM</code> and
 * <code>POOL_SWAP</code>.
 */
public class StudentGroupDecorator extends CharacterCardDecorator {
    private final int SWAP_CARD_STUDENTS = 6;
    private final int TRANSFER_CARD_STUDENTS = 4;

    private final StudentGroup students;
    private StudentGroup fromCard;
    private StudentGroup fromEntrance;
    private int islandIndex, playerID;
    private final boolean isToIsland, isToDiningRoom;

    /**
     * Constructor that instantiates this card specifying which one of the 3 it is. If both the flags are
     * <code>false</code> then it's a <code>POOL_SWAP</code> card.
     *
     * @param card           the <code>GenericCard</code> used in the decoration process
     * @param isToIsland     the flag to indicate whether this card is a <code>MOVE_TO_ISLAND</code>
     * @param isToDiningRoom the flag to indicate whether this card is a <code>MOVE_TO_DINING_ROOM</code>
     */
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

    /**
     * Sets necessary parameters for this card to be used. The field <code>fromCard</code>
     * must NOT be <code>null</code>. If this card is <code>MOVE_TO_ISLAND</code> then the field
     * <code>islandIndex</code> must NOT be <code>null</code>. If this card is <code>MOVE_TO_DINING_ROOM</code> or
     * <code>POOL_SWAP</code> then the field <code>playerID</code> must NOT be <code>null</code>. If this card
     * is <code>POOL_SWAP</code> then the field <code>fromEntrance</code> must NOT be <code>null</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    @Override
    public void setParameters(CardParameters params) {
        if(!students.contains(params.getFromOrigin())) {
            throw new NotEnoughStudentsException(Color.RED); //Default, change?
        }
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

    /**
     * Returns <code>ResponseParameters</code> with the necessary parameters. Could contain
     * <code>CharacterCards</code> and if <code>CharacterCard</code> is MOVE_TO_ISLAND then <code>Islands</code> and
     * <code>StudentBag</code>; if <code>CharacterCard</code> is MOVE_TO_DINING_ROOM then <code>School</code> and
     * <code>StudentBag</code>; if <code>CharacterCard</code> is POOL_SWAP then <code>School</code>.
     *
     * @return <code>ResponseParameters</code> for this <code>CharacterCard</code>.
     */
    @Override
    public ActionResponseParameters getResponseParameters() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        ActionResponseParameters params = new ActionResponseParameters();

        params.setCharacterCards(game.getCharacterCards());

        if(isToIsland)
            return params.setIslands(board.getIslands()).
                    setBagEmpty(game.isStudentBagEmpty());
        if(isToDiningRoom)
            return params.addSchool(board.getSchoolByPlayerID(playerID)).
                    setBagEmpty(game.isStudentBagEmpty());
        return params.addSchool(board.getSchoolByPlayerID(playerID));
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
            return -1;
        } else {
            board.removeFromEntranceOf(playerID, fromEntrance);
            board.addToEntranceOf(playerID, temp);

            fromEntrance.transferAllTo(students);
        }

        return 0;
    }

    /**
     * Returns information about the internal <code>StudentGroup</code> without exposing it.
     *
     * @param color the <code>Color</code> of the students
     * @return the number of students of color <code>color</code>
     */
    public int getStudentsByColor(Color color) {
        return students.getByColor(color);
    }
}
