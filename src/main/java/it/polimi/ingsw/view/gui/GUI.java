package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.Client;
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
    public void loadStartingScreen() {
        ExecutorService executor = Executors.newFixedThreadPool(16);

        try {
            ApplicationFX.semaphore.acquire();

            executor.submit(() -> ApplicationFX.main(null));

            ApplicationFX.semaphore.acquire();
            ApplicationFX.semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadClosingScreen() {
    }

    @Override
    public Connection handleBinding(Client client) {
        Platform.runLater(() -> {
            try {
                ApplicationFX.loadBindingScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }

    @Override
    public void handleInteraction() {

    }

    @Override
    public void handleHandshake() {

    }

    @Override
    public void displayState() {

    }
}
