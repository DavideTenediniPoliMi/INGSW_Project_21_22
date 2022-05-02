package it.polimi.ingsw.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ActionRequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;

public class VirtualView extends Observable<String> implements Observer<ResponseParameters> {
    public VirtualView() {
        Game.getInstance().addObserver(this);
        MatchInfo.getInstance().addObserver(this);
        Lobby.getLobby().addObserver(this);
    }

    public void handleRequest(String message) {
        JsonObject jo = JsonParser.parseString(message).getAsJsonObject();
        String commandType = jo.get("commandType").getAsString();

        if(CommandType.valueOf(commandType).isInGame) {
            requestAction(jo);
        } else {
            requestSetup(jo);
        }
    }

    private void requestSetup(JsonObject jo) {
        ActionRequestParameters params = new ActionRequestParameters();
        params.deserialize(jo);

    }

    private void requestAction(JsonObject jo) {

    }

    @Override
    public void update(ResponseParameters params) {
        notify(params.getMessage());
    }
}
