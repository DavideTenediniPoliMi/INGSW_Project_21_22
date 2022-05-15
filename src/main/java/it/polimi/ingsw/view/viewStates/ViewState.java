package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.utils.Printable;

public class ViewState extends Observable<String> implements Printable<String> {
    private boolean interactionComplete;

    public ViewState() {

    }

    public ViewState(ViewState oldViewState) {
        for(Observer<String> observer: oldViewState.getObservers()) {
            addObserver(observer);
        }
    }

    public void setInteractionComplete(boolean interactionComplete) {
        this.interactionComplete = interactionComplete;
    }

    public boolean isInteractionComplete() {
        return interactionComplete;
    }

    public String printCLIPrompt() {
        return "";
    }

    public String manageCLIInput(String input) {
        return "";
    }

    @Override
    public String print(boolean... params) {
        return "";
    }
}
