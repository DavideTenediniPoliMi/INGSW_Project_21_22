package it.polimi.ingsw.view.gui.controllers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.observer.Observable;
import it.polimi.ingsw.view.gui.GUIState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class representing a generic controller for any scene.
 */
public abstract class FXController extends Observable<String> {
    @FXML
    private Label error;

    /**
     * Shows the error to screen.
     * @param errorText the error to be shown.
     */
    public void showError(String errorText) {
        error.setText(errorText);
        error.setVisible(true);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> error.setVisible(false), 5, TimeUnit.SECONDS);
    }

    /**
     * Changes various elements of the graphic to match the changes coming from the server.
     *
     * @param jsonObject the last message received from the server.
     */
    public void applyChanges(JsonObject jsonObject) {}

    /**
     * Shows the alert on screen.
     */
    public void showAlert() {}

    /**
     * Changes various elements of the graphic to let the user interact. Changes are based on the state the GUI is
     * supposed to be in.
     *
     * @param nextState the state you want the GUI to be.
     */
    public void handleInteraction(GUIState nextState) {}
}
