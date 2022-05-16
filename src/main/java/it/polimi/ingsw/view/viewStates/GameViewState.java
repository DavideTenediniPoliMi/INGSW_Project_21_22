package it.polimi.ingsw.view.viewStates;

public class GameViewState extends ViewState {
    public GameViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return null;
    }

    @Override
    public void printCLIPrompt() {
    }

    @Override
    public String manageCLIInput(String input) {
        return null;
    }
}
