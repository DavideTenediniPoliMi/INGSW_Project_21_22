package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.utils.Serializable;

/**
 * Class representing the School entity in the game
 */
public class School implements Serializable {
    private Player owner;
    private int numTowers;
    private StudentGroup entrance = new StudentGroup();
    private StudentGroup diningRoom = new StudentGroup();

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
        else
            entrance = new StudentGroup();

        if(jsonObject.has("diningRoom"))
            diningRoom.deserialize(jsonObject.get("diningRoom").getAsJsonObject());
        else
            diningRoom = new StudentGroup();
    }
}
