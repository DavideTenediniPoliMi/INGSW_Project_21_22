package it.polimi.ingsw.view.cli.viewStates;

import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.network.observer.Observer;
import it.polimi.ingsw.utils.Printable;

/**
 * Class that manages CLI view
 */
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

    /**
     * Returns the buffer.
     *
     * @return <code>String</code> that corresponds to the buffer
     */
    public String getBuffer() {
        return buffer;
    }

    /**
     * Adds the specified <code>String</code> to the buffer.
     *
     * @param s <code>String</code> that has to be added to the buffer
     */
    public void appendBuffer(String s) {
        if(!"".equals(buffer)) {
            buffer += "\n";
        }
        buffer += s;
    }

    /**
     * Sets interaction complete.
     *
     * @param interactionComplete <code>Boolean</code> that indicates if the interaction is complete
     */
    public void setInteractionComplete(boolean interactionComplete) {
        this.interactionComplete = interactionComplete;
    }

    /**
     * Returns whether the interaction is completed.
     *
     * @return <code>true</code> if the interaction is completed
     */
    public boolean isInteractionComplete() {
        return interactionComplete;
    }

    /**
     * Appends the buffer of the CLI needed for the different interactions with the user.
     *
     * @param shouldPrint <code>Boolean</code> that indicates if it should append the CLI
     */
    public void printCLIPrompt(boolean shouldPrint) {}

    /**
     * Resets the interaction to the state it was before the interaction started.
     */
    public void resetInteraction() {}

    /**
     * Sets the player ID.
     *
     * @param playerID ID of the player to be set
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Manages the input received by the user and handles the action to be executed.
     *
     * @param input Input received by the Input Stream
     * @return <code>String</code> that is the result of the action, could be an error or empty string
     */
    public String manageCLIInput(String input) {
        return "";
    }

    /**
     * Returns a <code>String</code> that represents what should be printed in the CLI.
     *
     * @param params Params that are needed to print
     *
     * @return <code>String</code> that represents what should be printed in the CLI
     */
    @Override
    public String print(boolean... params) {
        return "";
    }
}
