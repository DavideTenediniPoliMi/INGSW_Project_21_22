package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.utils.Serializable;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the School entity in the game
 */
public class School implements Serializable, Printable<List<String>> {
    private Player owner;
    private int numTowers;
    private final StudentGroup entrance = new StudentGroup();
    private final StudentGroup diningRoom = new StudentGroup();

    /**
     * Sole constructor, binds this <code>School</code> to a <code>Player</code>
     *
     * @param owner the owner of this <code>School</code>
     */
    public School(Player owner){
        this.owner = owner;
    }

    /**
     * Returns the owner of this <code>School</code>
     *
     * @return Player owner of this <code>School</code>
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the amount of towers in this <code>School</code>
     *
     * @return Amount of towers in this <code>School</code>
     */
    public int getNumTowers(){
        return numTowers;
    }

    /**
     * Removes the specified amount of towers from this <code>School</code>
     *
     * @param amount the amount of towers to remove
     */
    protected void removeTowers(int amount) {
        numTowers = Math.max(numTowers - amount, 0);
    }

    /**
     * Adds the specified amount of towers to this <code>School</code>
     *
     * @param amount the amount of towers to add
     */
    protected void addTowers(int amount) {
        numTowers += amount;
    }

    /**
     * Returns the amount of students of the specified <code>Color</code> in this <code>School</code>'s dining room
     *
     * @param c the <code>Color</code> of the students
     * @return Amount of students of specified <code>Color</code>
     */
    public int getNumStudentsInDiningRoomByColor(Color c) {
        return diningRoom.getByColor(c);
    }

    /**
     * Returns the amount of students of the specified <code>Color</code> in this <code>School</code>'s entrance
     *
     * @param c the <code>Color</code> of the students
     * @return Amount of students of specified <code>Color</code>
     */
    public int getNumStudentsInEntranceByColor(Color c) { return entrance.getByColor(c); }

    /**
     * Removes the specified students from the entrance of this <code>School</code>
     *
     * @param students the <code>StudentGroup</code> to remove
     */
    protected void removeFromEntrance(StudentGroup students) {
        StudentGroup temp = new StudentGroup();
        entrance.transferTo(temp, students);
    }

    /**
     * Adds the specified students to the entrance of this <code>School</code>
     *
     * @param students the <code>StudentGroup</code> to add
     */
    protected void addToEntrance(StudentGroup students) {
        students.transferAllTo(entrance);
    }

    /**
     * Removes the specified students from the dining room of this <code>School</code>
     *
     * @param students the <code>StudentGroup</code> to remove
     */
    protected void removeFromDiningRoom(StudentGroup students) {
        StudentGroup temp = new StudentGroup();
        diningRoom.transferTo(temp, students);
    }

    /**
     * Adds the specified students to the dining room of this <code>School</code>
     *
     * @param students the <code>StudentGroup</code> to add
     */
    protected void addToDiningRoom(StudentGroup students) {
        students.transferAllTo(diningRoom);
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();

        JsonObject jsonObject = gson.toJsonTree(this).getAsJsonObject();
        jsonObject.remove("owner");
        if(owner != null)
            jsonObject.add("ownerID", new JsonPrimitive(owner.getID()));

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        int ownerID = jsonObject.get("ownerID").getAsInt();
        owner = Game.getInstance().getPlayerByID(ownerID);

        numTowers = jsonObject.get("numTowers").getAsInt();

        if(jsonObject.has("entrance"))
            entrance.deserialize(jsonObject.get("entrance").getAsJsonObject());

        if(jsonObject.has("diningRoom"))
            diningRoom.deserialize(jsonObject.get("diningRoom").getAsJsonObject());
    }

    @Override
    public List<String> print(boolean...params) {
        StringBuilder schoolBuilder = new StringBuilder();
        List<String> schoolString = new ArrayList<>();
        int nameTrim = (MatchInfo.getInstance().isExpertMode() ? 13 : 15);

        StringBuilder ownerName = new StringBuilder(owner.getName().substring(0, Math.min(owner.getName().length(), nameTrim)));

        ownerName.append(" ".repeat(nameTrim - ownerName.length()));

        Card card = owner.getSelectedCard();

        schoolBuilder.append("┌─────────────────────┐");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        //TEAM section
        schoolBuilder.append("│ ").append(owner.getTeamColor().print()).append(" ").append(ownerName);

        if(MatchInfo.getInstance().isExpertMode())
            schoolBuilder.append(owner.getNumCoins()).append(AnsiCodes.COIN);

        schoolBuilder.append(" │");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        //ASSISTANT CARD section
        schoolBuilder.append("│ Steps: ")
                    .append(card == null ? "na" : card.RANGE + " ")
                    .append(" Value: ")
                    .append(card == null ? "na" : card.WEIGHT)
                    .append(card == null ? "" : (card.WEIGHT < 10 ? " " : ""))
                    .append(" │");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        //ENTRANCE section
        schoolBuilder.append("├─Entrance:───────────┤");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        StringBuilder entranceBuilder = new StringBuilder(entrance.print(false));
        String pureEntrance = entrance.print(false);

        for(AnsiCodes code : AnsiCodes.values()) {
            pureEntrance = pureEntrance.replace(code.toString(), "");
        }

        entranceBuilder.append(" ".repeat(21 - pureEntrance.length()));

        schoolBuilder.append("│").append(entranceBuilder).append("│");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        schoolBuilder.append("├─────────────────────┤");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        schoolBuilder.append("├─Dining Room:────────┤");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        schoolBuilder.append("│").append(diningRoom.print(true)).append("│");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        schoolBuilder.append("│")
                    .append(Game.getInstance().getBoard().getProfessorOwners().printFor(owner.getID()))
                    .append("│");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);

        schoolBuilder.append("└─────────────────────┘");
        schoolString.add(schoolBuilder.toString());
        schoolBuilder.setLength(0);


        return schoolString;
    }
}
