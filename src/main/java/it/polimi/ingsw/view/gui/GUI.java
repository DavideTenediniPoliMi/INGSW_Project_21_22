package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.view.gui.controllers.FXController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class GUI extends Application {
    public static Semaphore semaphore = new Semaphore(1);
    private static FXController sceneController;
    private static Connection serverConnection;
    private static Stage stage;
    private static final int resX = 1366;
    private static final int resY = 768;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        GUI.stage = stage;

        stage.setTitle("Eriantys!");
        stage.setResizable(false);
        stage.setX(0);
        stage.setY(0);

        loadScene("/scenes/bindingScene.fxml");

        //stops process on window close
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void loadScene(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUI.class.getResource(path)));
        Parent root = loader.load();

        sceneController = loader.getController();
        sceneController.addObserver(serverConnection);

        stage.setScene(new Scene(root, resX, resY));
        stage.show();
    }

    public static void setServerConnection(Connection con) {
        serverConnection = con;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(serverConnection);
    }

    public static void showHandshakeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUI.class.getResource("/scenes/handshakeScene.fxml")));
        Parent root = loader.load();
        sceneController = loader.getController();
        sceneController.addObserver(serverConnection);

        stage.setScene(new Scene(root, resX, resY));
        stage.show();
    }

    public static void showError(String error) {
        sceneController.showError(error);
    }
}
