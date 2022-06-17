package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.observer.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class FXController extends Observable<String> {
    @FXML
    private Label error;

    public void showError(String errorText) {
        error.setText(errorText);
        error.setVisible(true);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> error.setVisible(false), 5, TimeUnit.SECONDS);
    }

    public void applyChanges() {}

    public void showAlert() {}
}
