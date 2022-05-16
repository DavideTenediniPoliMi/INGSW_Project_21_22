package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.utils.Printable;

public class ViewState extends Observable<String> implements Printable<String> {
    private boolean interactionComplete;
    private String buffer = "";

    public ViewState() {}

    public ViewState(ViewState oldViewState) {
        for(Observer<String> observer: oldViewState.getObservers()) {
            addObserver(observer);
        }
    }

    public String getBuffer() {
        return buffer;
    }

    protected void appendBuffer(String s) {
        buffer += "\n" + s;
    }

    public void setInteractionComplete(boolean interactionComplete) {
        this.interactionComplete = interactionComplete;
    }

    public boolean isInteractionComplete() {
        return interactionComplete;
    }

    public void printCLIPrompt() {

    }

    public String manageCLIInput(String input) {
        return "";
    }

    @Override
    public String print(boolean... params) {
        return "";
    }
}
