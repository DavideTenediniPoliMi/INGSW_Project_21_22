package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.ApplicationFX;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.Socket;

public class BindingController extends FXController {
    public static Socket socket;
    @FXML
    private TextField ipAddressText;
    @FXML
    private TextField portText;

    public void handleBindingButton() {
        String ip = ipAddressText.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portText.getText().trim());
            socket = new Socket(ip, port);

            ApplicationFX.semaphore.release();
        } catch(Exception e) {
            showError("Something went wrong! Try again!");
        }
    }

}
