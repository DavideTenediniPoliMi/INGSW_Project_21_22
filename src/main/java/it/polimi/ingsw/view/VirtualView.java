package it.polimi.ingsw.view;

import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.network.parameters.ActionResponseParameters;
import it.polimi.ingsw.network.parameters.SetupResponseParameters;

public class VirtualView {

    public VirtualView() {

    }

    class GameObserver implements Observer<ActionResponseParameters> {

        public GameObserver() {
            //TODO attach to game
        }

        @Override
        public void update(ActionResponseParameters message) {

        }
    }

    class LobbyObserver implements Observer<SetupResponseParameters> {

        public LobbyObserver() {
            //TODO attach to lobby
        }

        @Override
        public void update(SetupResponseParameters message) {

        }
    }
}
