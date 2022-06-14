package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.view.gui.controllers.FXController;
import it.polimi.ingsw.view.gui.controllers.HandshakeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class ApplicationFX extends Application {
    public static Semaphore semaphore = new Semaphore(1);
    private static FXController sceneController;
    private static Connection serverConnection;
    public static Stage stage;
    private static final int resX = 1366;
    private static final int resY = 768;

    @Override
    public void start(Stage stage) throws IOException {
        ApplicationFX.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationFX.class.getResource("/bindingScene.fxml")));

        Scene scene = new Scene(root, resX, resY);

        stage.setTitle("Eriantys!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();

        //stops process on window close
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setServerConnection(Connection con) {
        serverConnection = con;
    }

    public static void showClosingScreen() {
        Button btn = new Button();
        btn.setText("CLOSE");
        btn.setOnAction(event -> stage.close());

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, resX, resY);

        stage.setScene(scene);
        stage.show();
    }

    public static void showHandshakeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ApplicationFX.class.getResource("/handshakeScene.fxml")));
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
