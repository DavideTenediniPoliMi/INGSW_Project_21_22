package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import org.fusesource.jansi.AnsiConsole;

public class HandshakeViewState extends ViewState {
    public HandshakeViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String printCLIPrompt() {
        return "Insert your name :";
    }

    @Override
    public String manageCLIInput(String input) {
        RequestParameters handshake = new RequestParameters()
                .setCommandType(CommandType.HANDSHAKE)
                .setName(input);

        notify(handshake.serialize().toString());
        setInteractionComplete(true);
        return "";
    }
}
