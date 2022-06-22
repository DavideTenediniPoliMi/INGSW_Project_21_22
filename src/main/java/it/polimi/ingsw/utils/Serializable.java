package it.polimi.ingsw.utils;

import com.google.gson.JsonObject;

/**
 * Interface for serializable classes. Adds <code>serialize</code> and <code>deserialize</code> methods.
 */
public interface Serializable {
    /**
     * Serializes the <code>Object</code> in JSON.
     *
     * @return <code>JsonObject</code> that represent the <code>Object</code> serialized
     */
    JsonObject serialize();

    /**
     * Deserializes the specified <code>JsonObject</code> into the specific <code>Object</code>.
     *
     * @param jsonObject <code>JsonObject</code> to be deserialized
     */
    void deserialize(JsonObject jsonObject);
}
