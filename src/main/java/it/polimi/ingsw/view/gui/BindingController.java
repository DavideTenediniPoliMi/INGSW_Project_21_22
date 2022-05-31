package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BindingController {
    public static Socket socket;

    @FXML
    private Label error;
    @FXML
    private TextField ipAddressText;
    @FXML
    private TextField portText;

    public void handleBindingButton() {
        System.out.println("button");
        String ip = ipAddressText.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portText.getText().trim());
            socket = new Socket(ip, port);

            ApplicationFX.semaphore.release();
        } catch(Exception e) {
            error.setText("Something went wrong! Try again!");
            error.setVisible(true);
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.schedule(() -> error.setVisible(false), 5, TimeUnit.SECONDS);
        }
    }

}
