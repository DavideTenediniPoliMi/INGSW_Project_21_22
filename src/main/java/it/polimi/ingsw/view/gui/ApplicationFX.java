package it.polimi.ingsw.view.gui;

import javafx.application.Application;
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
    public static Stage stage;

    @Override
    public void start(Stage stage) {
        ApplicationFX.stage = stage;
        Button btn = new Button();
        btn.setText("PLAY");
        btn.setOnAction(event -> {
            semaphore.release();
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 1920, 1080);

        stage.setTitle("Welcome to Eriantys!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void loadBindingScreen() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationFX.class.getResource("/bindingScene.fxml")));

        stage.setTitle("Binding");
        stage.setScene(new Scene(root, 1920 , 1080));
        stage.show();
    }
}
