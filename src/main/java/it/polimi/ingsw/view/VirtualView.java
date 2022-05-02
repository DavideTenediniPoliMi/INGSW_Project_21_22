package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ResponseParameters;

public class VirtualView extends Observable<String> implements Observer<ResponseParameters> {
    public VirtualView() {
        Game.getInstance().addObserver(this);
        MatchInfo.getInstance().addObserver(this);
        Lobby.getLobby().addObserver(this);
    }

    public void handleRequest(String message) {

    }

    private void requestSetup() {

    }

    private void requestAction() {

    }

    @Override
    public void update(ResponseParameters message) {
        notify(message.serialize().toString());
    }
}
