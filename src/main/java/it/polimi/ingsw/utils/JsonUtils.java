package it.polimi.ingsw.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.TurnState;

/**
 * Class containing methods that read a JsonObject and return valuable information.
 */
public class JsonUtils {
    /**
     * Checks whether a message contains CharacterCards and should be elaborated.
     *
     * @param jo the message received.
     * @param playerID the ID of the Hero.
     * @return true if the message contains characterCards and the state of the game are favorable, false otherwise.
     */
    public static boolean isNotCharCardJSON(JsonObject jo, int playerID) {
        MatchInfo matchInfo = MatchInfo.getInstance();
        return !jo.has("characterCards") ||
                matchInfo.getCurrentPlayerID() != playerID ||
                matchInfo.getStateType().equals(TurnState.PLANNING) ||
                matchInfo.getStateType().equals(TurnState.CLOUD);
    }

    /**
     * Returns the name of the active characterCard in the message.
     *
     * @param jo the message received.
     * @return the name of the active card.
     */
    public static String getActiveCardName(JsonObject jo) {
        JsonArray jsonArray = jo.get("characterCards").getAsJsonArray();
        for (JsonElement je : jsonArray) {
            String name = je.getAsJsonObject().get("name").getAsString();
            CharacterCard c = CharacterCards.valueOf(name).instantiate();
            c.deserialize(je.getAsJsonObject());

            if(c.isActive())
                return c.getName();
        }
        return "";
    }

    /**
     * Checks whether the message indicates that it's the player's turn.
     *
     * @param jo the message received.
     * @param playerID the ID of the Hero.
     * @return true if it's the player's turn, false otherwise.
     */
    public static boolean isNotPlayerTurn(JsonObject jo, int playerID) {
        return jo.get("playOrder").getAsJsonArray().size() == 0 ||
                jo.get("playOrder").getAsJsonArray().get(0).getAsInt() != playerID;
    }

    /**
     * Checks whether the message indicates that the game is over.
     *
     * @param jo the message received.
     * @return true if the game is over, false otherwise.
     */
    public static boolean isGameOver(JsonObject jo) {
        if(jo.has("gameOver")) {
            return jo.get("gameOver").getAsBoolean();
        }
        return false;
    }

    /**
     * Checks whether the message indicates that a player reconnected to the game.
     *
     * @param jo the message received.
     * @return true if a player has reconnected, false otherwise.
     */
    public static boolean hasPlayerReconnected(JsonObject jo) {
        return MatchInfo.getInstance().getNumPlayersConnected() > jo.get("numPlayersConnected").getAsInt();
    }

    /**
     * Checks whether the message indicates that the game is paused.
     *
     * @param jo the message received.
     * @return true if the game is paused, false otherwise.
     */
    public static boolean isGamePaused(JsonObject jo) {
        if(jo.has("gamePaused")) {
            return jo.get("gamePaused").getAsBoolean();
        }
        return false;
    }

    /**
     * Compares the state indicated in the message and the one given.
     *
     * @param jo the message received.
     * @param state the state to compare it to.
     * @return true if the two states are different, false otherwise.
     */
    public static boolean areDifferentStates(JsonObject jo, TurnState state) {
        return !getTurnState(jo).equals(state);
    }

    /**
     * Finds the turnState indicated in a message.
     *
     * @param jo the message received.
     * @return the turnState found.
     */
    public static TurnState getTurnState(JsonObject jo) {
        return TurnState.valueOf(jo.get("stateType").getAsString());
    }
}
