package it.polimi.ingsw.view.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.utils.JsonUtils;
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
    private static boolean playedPlanning;
    private static boolean boughtCard;
    private static boolean waitForActivatedCard;
    private static String activeCardName;
    private static boolean activatedCard;

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

    public static void showError(String error) {
        sceneController.showError(error);
    }

    public static void setName(String name) {
        GUI.name = name;
    }

    public static String getName() {
        return name;
    }

    public static void applyChanges(JsonObject jsonObject) {
        sceneController.applyChanges(jsonObject);
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

    public static boolean nextState(JsonObject jo) {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(!jo.has("matchInfo")) {
            if(JsonUtils.isNotCharCardJSON(jo, playerId)) {
                return false;
            }

            System.out.println("SAW CHARACTER CARDS");

            activeCardName = JsonUtils.getActiveCardName(jo);

            if(!activeCardName.equals("")) {
                if(waitForActivatedCard) {
                    waitForActivatedCard = false;
                    activatedCard = true;
                } else {
                    boughtCard = true;
                }
            }

            return false;
        }

        jo = jo.get("matchInfo").getAsJsonObject();

        if(JsonUtils.isGameOver(jo)) {

            return true;
        }

        if(JsonUtils.hasPlayerReconnected(jo)) {
            return false;
        }

        if(JsonUtils.isGamePaused(jo)) {

            return false;
        }

        if(JsonUtils.isNotPlayerTurn(jo, playerId))
            return false;

        if(MatchInfo.getInstance().isGamePaused()) {

            return true;
        }

        if(boughtCard) {
            System.out.println("SAW MATCHINFO AFTER BUYING");
            boughtCard = false;

            if("INFLUENCE_ADD_TWO".equals(activeCardName)) {
                System.out.println("WAITING FOR SETTING PARAMS ADD TWO");
                return false;
            }

            if("IGNORE_TOWERS".equals(activeCardName)) {
                System.out.println("AND WAS FOR IGNORE TOWERS");

                activeCardName = "";
                return true;
            }

            System.out.println("AND WAITING FOR USER");
            waitForActivatedCard = true;
            return true;
        }

        if("INFLUENCE_ADD_TWO".equals(activeCardName)) {
            System.out.println("FINISHED ADD TWO");
            activeCardName = "";

            return true;
        }

        if(activatedCard) {
            System.out.println("ACTIVATED CARD FROM USER");
            activeCardName = "";
            activatedCard = false;

            return true;
        }

        System.out.println("DIDN'T BUY CARDS");

        switch(matchInfo.getStateType()) {
            case PLANNING:
                if(JsonUtils.areDifferentStates(jo, TurnState.PLANNING)) {

                    return true;
                }

                if(!playedPlanning) {
                    playedPlanning = true;

                    return true;
                }

                return false;
            case STUDENTS:
                int numMovedStudents = jo.get("numMovedStudents").getAsInt();
                playedPlanning = false;

                if(JsonUtils.areDifferentStates(jo, TurnState.STUDENTS)) {

                    return true;
                }

                if(matchInfo.getNumMovedStudents() != numMovedStudents) {
                    if(numMovedStudents < MatchInfo.getInstance().getMaxMovableStudents()) {

                        return true;
                    }
                }

                return false;
            case MOTHER_NATURE:
                if(JsonUtils.areDifferentStates(jo, TurnState.MOTHER_NATURE)) {

                    return true;
                }

                return false;
            case CLOUD:
                if(JsonUtils.areDifferentStates(jo, TurnState.CLOUD)) {

                    return jo.get("numMovedStudents").getAsInt() == 0;
                }

                return false;
            default:
                return false;
        }
    }
}
