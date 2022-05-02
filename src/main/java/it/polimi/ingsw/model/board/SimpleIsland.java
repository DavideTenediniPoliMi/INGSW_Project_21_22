package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class representing a single Island
 */
public class SimpleIsland extends Island {
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

}
