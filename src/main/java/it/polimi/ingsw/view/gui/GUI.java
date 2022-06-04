package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.viewStates.ViewState;
import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI extends View {
    public GUI(ViewState viewState) {
        super(viewState);
    }
    @Override
    public void loadStartingScreen() {}

    @Override
    public void loadClosingScreen() {}

    @Override
    public Connection handleBinding(Client client) {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        try {
            ApplicationFX.semaphore.acquire();

            executor.submit(() -> ApplicationFX.main(null));

            waitForGuiInteraction();
            return new ServerConnection(BindingController.socket, client, true);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    private void waitForGuiInteraction() throws InterruptedException {
        ApplicationFX.semaphore.acquire();
        ApplicationFX.semaphore.release();
    }

    @Override
    public void handleInteraction() {

    }

    @Override
    public void handleHandshake() {
        Platform.runLater(() -> {
            try {
                ApplicationFX.showHandshakeScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void displayState() {

    }
}
