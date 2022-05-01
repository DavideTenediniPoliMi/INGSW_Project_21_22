package it.polimi.ingsw.utils;

import com.google.gson.JsonObject;

/**
 * Interface for serializable classes. Adds <code>serialize</code> and <code>deserialize</code> methods.
 */
public interface Serializable {
    JsonObject serialize();
    void deserialize(String json);
}
