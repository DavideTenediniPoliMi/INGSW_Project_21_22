package it.polimi.ingsw.model.characters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.parameters.ResponseParameters;

/**
 * Class to manage the behaviour of 1 <code>CharacterCard</code>, <code>RETURN_TO_BAG</code>.
 */
public class ReturnToBagDecorator extends CharacterCardDecorator {
    private final int NUM_STUDENTS_TO_RETURN = 3;
    private Color selectedColor;

    /**
     * Constructor that instantiates this card.
     *
     * @param card the <code>GenericCard</code> used in the decoration process
     */
    public ReturnToBagDecorator(CharacterCard card) {
        super(card);
    }

    /**
     * Sets necessary parameters for this card to be used. The field <code>selectedColor</code>
     * must NOT be <code>null</code>.
     *
     * @param params the <code>Parameters</code> to set in this card.
     */
    @Override
    public void setParameters(CardParameters params) {
        selectedColor = params.getSelectedColor();
    }

    /**
     * Returns <code>ResponseParameters</code> with the necessary parameters. Could contain more than one
     * <code>School</code> and <code>StudentBag</code>.
     *
     * @return <code>ResponseParameters</code> for this <code>CharacterCard</code>.
     */
    @Override
    public ResponseParameters getResponseParameters() {
        Game game = Game.getInstance();
        ResponseParameters params = new ResponseParameters();

        return params.setSchools(game.getBoard().getSchools())
                .setBagEmpty(game.isStudentBagEmpty());
    }

    @Override
    public int activate() {
        card.activate();

        Game game = Game.getInstance();
        Board board = game.getBoard();

        for(Player player: game.getPlayers()) {
            School targetSchool = board.getSchoolByPlayerID(player.getID());

            int numAvailableStudents = targetSchool.getNumStudentsInDiningRoomByColor(selectedColor);
            int numStudentsToReturn = Math.min(NUM_STUDENTS_TO_RETURN, numAvailableStudents);

            StudentGroup temp = new StudentGroup(selectedColor, numStudentsToReturn);

            board.removeFromDiningRoomOf(player.getID(), temp);
            game.putStudentsBack(temp);
        }

        return 0;
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();

        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        if(jsonObject.has("selectedColor"))
            selectedColor = Color.valueOf(jsonObject.get("selectedColor").getAsString());

        card.deserialize(jsonObject.get("card").getAsJsonObject());
    }
}
