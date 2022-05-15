package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.viewStates.ViewState;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection extends Connection {
    private final Client client;
    private final CLI cli;

    public ServerConnection(Socket socket, Client client) throws IOException {
        super(socket);

        this.client = client;
        ViewState viewState = new ViewState();
        viewState.addObserver(this);
        cli = new CLI(viewState);
    }

    @Override
    public void run() {
        handleHandshake();


    }

    public void handleHandshake() {
        Scanner scanner = new Scanner(System.in);

        do {
            AnsiConsole.sysOut().println("Insert you name :");
            String name = scanner.nextLine();

            RequestParameters handshake = new RequestParameters()
                    .setCommandType(CommandType.HANDSHAKE)
                    .setName(name);

            sendMessage(handshake.serialize().toString());

            String response = receiveMessage();

            JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

            if (!jo.has("error")) {
                new ResponseParameters().deserialize(jo);


                break;
            }

            AnsiConsole.sysOut().println(jo.get("error"));
        } while(true);
    }
}
