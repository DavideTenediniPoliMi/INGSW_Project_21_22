package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.utils.Printable;

public class ViewState extends Observable<String> implements Printable<String> {
    private boolean interactionComplete;
    private String buffer = "";
    protected int playerID;

    public ViewState() {}

    public ViewState(ViewState oldViewState) {
        playerID = oldViewState.playerID;
        for(Observer<String> observer: oldViewState.getObservers()) {
            addObserver(observer);
        }
    }

    public String getBuffer() {
        return buffer;
    }

    public void appendBuffer(String s) {
        if(!"".equals(buffer)) {
            buffer += "\n";
        }
        buffer += s;
    }

    public void setInteractionComplete(boolean interactionComplete) {
        this.interactionComplete = interactionComplete;
    }

    public boolean isInteractionComplete() {
        return interactionComplete;
    }

    public void printCLIPrompt(boolean shouldPrint) {}

    public void resetInteraction() {}

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String manageCLIInput(String input) {
        return "";
    }

    @Override
    public String print(boolean... params) {
        return "";
    }
}
