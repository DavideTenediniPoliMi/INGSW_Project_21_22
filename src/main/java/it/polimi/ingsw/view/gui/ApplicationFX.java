package it.polimi.ingsw.view.gui;

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
    public static Stage stage;
    private static final int resX = 1920;
    private static final int resY = 1080;

    @Override
    public void start(Stage stage) {
        ApplicationFX.stage = stage;
        Button btn = new Button();
        btn.setText("PLAY");
        btn.setOnAction(event -> semaphore.release());

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, resX, resY);

        stage.setTitle("Eriantys!");
        stage.setResizable(false);
        stage.setScene(scene);
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

    public static void showBindingScreen() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ApplicationFX.class.getResource("/bindingScene.fxml")));

        stage.setScene(new Scene(root, resX , resY));
        stage.show();
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

    public static void showHandshakeScreen() {
        Button btn = new Button();
        btn.setText("test");
        btn.setOnAction(event -> System.out.println("test"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, resX, resY);

        stage.setScene(scene);
        stage.show();
    }
}
