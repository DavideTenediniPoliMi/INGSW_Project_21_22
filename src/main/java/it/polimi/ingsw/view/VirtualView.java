package it.polimi.ingsw.view;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ActionResponseParameters;
import it.polimi.ingsw.network.parameters.SetupResponseParameters;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;

public class VirtualView extends Observable<String> {

    private final GameObserver gameObserver;
    private final LobbyObserver lobbyObserver;

    public VirtualView() {
        gameObserver = new GameObserver();
        lobbyObserver = new LobbyObserver();
    }

    class GameObserver implements Observer<ActionResponseParameters> {

        public GameObserver() {
            Game.getInstance().addObserver(this);
        }

        @Override
        public void update(ActionResponseParameters message) {
            sendMessage(message.serialize().toString());
        }
    }

    class LobbyObserver implements Observer<SetupResponseParameters> {

        public LobbyObserver() {
            Lobby.getLobby().addObserver(this);
        }

        @Override
        public void update(SetupResponseParameters message) {
            sendMessage(message.serialize().toString());
        }
    }

    public void handleRequest(String message) {

    }

    private void requestSetup() {

    }

    private void requestAction() {

    }

    private void sendMessage(String message) {
        notify(message);
    }
}
