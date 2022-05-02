package it.polimi.ingsw.model.characters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.parameters.ResponseParameters;

/**
 * Class to manage the behaviour of 1 <code>CharacterCard</code>, <code>EXCHANGE_STUDENTS</code>.
 */
public class ExchangeStudentsDecorator extends CharacterCardDecorator {
    private StudentGroup fromEntrance;
    private StudentGroup fromDiningRoom;
    private int playerID;

    /**
     * Constructor that instantiates this card.
     *
     * @param card the <code>GenericCard</code> used in the decoration process
     */
    public ExchangeStudentsDecorator(CharacterCard card) {
        super(card);
    }

    /**
     * Sets necessary parameters for this card to be used. The fields <code>playerID</code>,
     * <code>fromEntrance</code> and <code>fromDiningRoom</code> must NOT be <code>null</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    @Override
    public void setParameters(CardParameters params) {
        playerID = params.getPlayerID();

        fromEntrance = params.getFromOrigin();
        fromDiningRoom = params.getFromDestination();
    }

    /**
     * Returns <code>ResponseParameters</code> with the necessary parameters. Could contain <code>School</code>.
     *
     * @return <code>ResponseParameters</code> for this <code>CharacterCard</code>.
     */
    @Override
    public ResponseParameters getResponseParameters() {
        Game game = Game.getInstance();
        ResponseParameters params = new ResponseParameters();

        return params.addSchool(game.getBoard()
                .getSchoolByPlayerID(playerID));
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

    @Override
    public JsonObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {

    }
}
