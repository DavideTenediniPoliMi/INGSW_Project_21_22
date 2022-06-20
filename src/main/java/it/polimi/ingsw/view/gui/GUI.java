package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
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
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI extends Application {
    private static FXController sceneController;
    private static Connection serverConnection;
    private static int playerId;
    private static String name;
    private static Stage stage;
    private static final int resX = 1366;
    private static final int resY = 768;
    private static boolean createdLobby;

    public static void main(String[] args) {
        launch();
    }

    public static void setCreatedLobby(boolean createdLobby) {
        GUI.createdLobby = createdLobby;
    }

    public static boolean didCreateLobby() {
        return GUI.createdLobby;
    }

    @Override
    public void start(Stage stage) {
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

    public static void loadScene(String path) {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(GUI.class.getResource(path)));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            showError(e.getMessage());
            return;
        }

        sceneController = loader.getController();
        if(sceneController != null) {
            sceneController.addObserver(serverConnection);
        }
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

    public static void setName(String name) {
        GUI.name = name;
    }

    public static String getName() {
        return name;
    }

    public static void applyChanges() {
        sceneController.applyChanges();
    }

    public static void bindPlayerId() {
        Optional<Player> result = Lobby.getLobby().getPlayers().stream()
                .filter((player) -> (player.getName().equals(name)))
                .findAny();

        if(result.isEmpty()) {
            result = Game.getInstance().getPlayers().stream()
                    .filter((player) -> (player.getName().equals(name)))
                    .findAny();
        }

        playerId = (result.isEmpty()) ? -1 : result.get().getID();
    }

    public static void showAlert() {
        sceneController.showAlert();
    }

    public static int getPlayerId() {
        return playerId;
    }
}
