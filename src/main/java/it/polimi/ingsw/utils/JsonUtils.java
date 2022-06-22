package it.polimi.ingsw.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.TurnState;

public class JsonUtils {
    public static boolean isNotCharCardJSON(JsonObject jo, int playerID) {
        MatchInfo matchInfo = MatchInfo.getInstance();
        return !jo.has("characterCards") ||
                matchInfo.getCurrentPlayerID() != playerID ||
                matchInfo.getStateType().equals(TurnState.PLANNING) ||
                matchInfo.getStateType().equals(TurnState.CLOUD);
    }

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

    public static boolean isNotPlayerTurn(JsonObject jo, int playerID) {
        return jo.get("playOrder").getAsJsonArray().size() == 0 ||
                jo.get("playOrder").getAsJsonArray().get(0).getAsInt() != playerID;
    }

    public static boolean isGameOver(JsonObject jo) {
        if(jo.has("gameOver")) {
            return jo.get("gameOver").getAsBoolean();
        }
        return false;
    }

    public static boolean hasPlayerReconnected(JsonObject jo) {
        return MatchInfo.getInstance().getNumPlayersConnected() != jo.get("numPlayersConnected").getAsInt();
    }

    public static boolean isGamePaused(JsonObject jo) {
        if(jo.has("gamePaused")) {
            return jo.get("gamePaused").getAsBoolean();
        }
        return false;
    }

    public static boolean areDifferentStates(JsonObject jo, TurnState state) {
        return !getTurnState(jo).equals(state);
    }

    public static TurnState getTurnState(JsonObject jo) {
        return TurnState.valueOf(jo.get("stateType").getAsString());
    }
}
