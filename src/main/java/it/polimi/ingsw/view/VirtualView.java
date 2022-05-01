package it.polimi.ingsw.view;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ActionResponseParameters;
import it.polimi.ingsw.network.parameters.SetupResponseParameters;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;

public class VirtualView extends Observable<String> {

    public VirtualView() {

    }

    class GameObserver implements Observer<ActionResponseParameters> {

        public GameObserver() {
            Game.getInstance().addObserver(this);
        }

        @Override
        public void update(ActionResponseParameters message) {

        }
    }

    class LobbyObserver implements Observer<SetupResponseParameters> {

        public LobbyObserver() {
            Lobby.getLobby().addObserver(this);
        }

        @Override
        public void update(SetupResponseParameters message) {

        }
    }
}
