package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utils.Printable;

/**
 * Class representing a single Island
 */
public class SimpleIsland extends Island implements Printable<String> {
    /**
     * Sole constructor
     */
    public SimpleIsland() {
    }

    @Override
    public int getNumIslands() {
        return 1;
    }

    @Override
    public JsonObject serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this).getAsJsonObject();
    }

    @Override
    public String print(boolean... params) {
        return printIsland(null, false, false, false, false);
    }
}
