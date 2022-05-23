package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;

public class HandshakeViewState extends ViewState {
    public HandshakeViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public void printCLIPrompt() {
        appendBuffer("Insert your name :");
    }

    @Override
    public String manageCLIInput(String input) {
        appendBuffer(input);
        if(input.equals("")) {
            String error = "That's not a valid name! Try again!";
            appendBuffer(error);
            return error;
        }

        RequestParameters handshake = new RequestParameters()
                .setCommandType(CommandType.HANDSHAKE)
                .setName(input);

        notify(handshake.serialize().toString());
        setInteractionComplete(true);
        return "";
    }
}
