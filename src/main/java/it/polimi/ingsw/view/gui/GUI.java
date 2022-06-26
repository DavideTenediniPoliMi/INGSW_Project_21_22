package it.polimi.ingsw.view.gui;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GameStatus;
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

/**
 * Class representing the Graphic Interface and all of its features.
 */
public class GUI extends Application {
    private static FXController sceneController;
    private static Connection serverConnection;
    private static int playerId = -1;
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
    private static boolean waitingForPlayerRecon;
    private static boolean waitingForPlayerDC;
    private static boolean unpausing;

    /**
     * Asks the engine to start this application.
     *
     * @param args the parameters taken from the command line execution
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets the corresponding flag.
     *
     * @param createdLobby flag indication whether a lobby has been created.
     */
    public static void setCreatedLobby(boolean createdLobby) {
        GUI.createdLobby = createdLobby;
    }

    /**
     * Returns whether a lobby has been created by the user.
     *
     * @return true if a lobby was created, false otherwise.
     */
    public static boolean didCreateLobby() {
        return GUI.createdLobby;
    }

    /**
     * Asks this application to process an interaction prompted by a server message.
     *
     * @param nextState the state you want this application to process next.
     */
    public static void handleInteraction(GUIState nextState) {
        sceneController.handleInteraction(nextState);
    }

    /**
     * Initializes this application first scene.
     *
     * @param stage the main stage of the application.
     */
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

    /**
     * Loads and starts a new scene while binding the new controller.
     *
     * @param path the relative path of the file containing the FXML file of the specified scene.
     */
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

    /**
     * Sets the ServerConnection for this application.
     *
     * @param con the connection that this application will use for communicating.
     */
    public static void setServerConnection(Connection con) {
        serverConnection = con;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(serverConnection);
    }


    /**
     * Asks this application to show an error.
     *
     * @param error the error to be shown on screen.
     */
    public static void showError(String error) {
        sceneController.showError(error);
        if(GameStatus.IN_GAME == MatchInfo.getInstance().getGameStatus() && GUI.getPlayerId() != -1) {
            handleInteraction(GUIState.REPEAT_ACTION);
        }
    }

    /**
     * Sets the name of the user that is using this application.
     *
     * @param name the user name.
     */
    public static void setName(String name) {
        GUI.name = name;
    }

    /**
     * Returns the name of the user using this application.
     *
     * @return the user name.
     */
    public static String getName() {
        return name;
    }

    /**
     * Asks this application to show (and therefor apply) the changes the server sent.
     *
     * @param jsonObject the message sent by the server containing the changed structures.
     */
    public static void applyChanges(JsonObject jsonObject) {
        sceneController.applyChanges(jsonObject);
    }

    /**
     * Asks this application to save the user ID as seen from the server. Must be called only after having saved
     * the name of the user and after having received either a Lobby or a Game packet.
     */
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

    /**
     * Asks this application to show the alert in the scene.
     */
    public static void showAlert() {
        sceneController.showAlert();
    }

    /**
     * Returns the ID of the user as seen from the Server.
     *
     * @return the user ID.
     */
    public static int getPlayerId() {
        return playerId;
    }

    /**
     * Calculates the state this application should be in next based on past packets, internal states and
     * the last packet received.
     *
     * @param jo the last packet received from the server.
     * @return the state this application should be in next.
     */
    public static GUIState nextState(JsonObject jo) {
        MatchInfo matchInfo = MatchInfo.getInstance();

        if(!jo.has("matchInfo")) {
            if(JsonUtils.isNotCharCardJSON(jo, playerId)) {
                if(waitingForPlayerDC && jo.has("players")) {
                    waitingForPlayerDC = false;
                    return GUIState.DISCONNECTION;
                }
                if(waitingForPlayerRecon && jo.has("players")) {
                    waitingForPlayerRecon = false;
                    if(matchInfo.getCurrentPlayerID() == playerId && unpausing) {
                        unpausing = false;
                        return resetState(jo);
                    }
                    return GUIState.DISCONNECTION;
                }
                return GUIState.WAIT_RESPONSE;
            }

            activeCardName = JsonUtils.getActiveCardName(jo);

            if(!activeCardName.equals("")) {
                if(waitForActivatedCard) {
                    waitForActivatedCard = false;
                    activatedCard = true;
                } else {
                    boughtCard = true;
                }
            }

            return GUIState.WAIT_RESPONSE;
        }

        jo = jo.get("matchInfo").getAsJsonObject();

        if(JsonUtils.isGameOver(jo))
            return GUIState.END_GAME;

        if(JsonUtils.hasPlayerDisconnected(jo)) {
            waitingForPlayerDC = true;
            return GUIState.DISCONNECTION;
        }

        if(JsonUtils.hasPlayerReconnected(jo)) {
            waitingForPlayerRecon = true;
            if(matchInfo.isGamePaused()) //Game will unpause in this packet
                unpausing = true;
            return GUIState.DISCONNECTION;
        }

        if(JsonUtils.isGamePaused(jo))
            return GUIState.PAUSE;

        if(JsonUtils.isNotPlayerTurn(jo, playerId))
            return GUIState.WAIT_ACTION;

        if(MatchInfo.getInstance().isGamePaused())
            return resetState(jo);

        if(boughtCard) {
            boughtCard = false;

            if("INFLUENCE_ADD_TWO".equals(activeCardName))
                return GUIState.WAIT_RESPONSE;


            if("IGNORE_TOWERS".equals(activeCardName)) {
                activeCardName = "";
                return resetState(jo);
            }

            waitForActivatedCard = true;
            return GUIState.SET_CARD_PARAMS;
        }

        if("INFLUENCE_ADD_TWO".equals(activeCardName)) {
            activeCardName = "";
            return resetState(jo);
        }

        if(activatedCard) {
            activeCardName = "";
            activatedCard = false;
            return resetState(jo);
        }

        switch(matchInfo.getStateType()) {
            case PLANNING:
                if(JsonUtils.areDifferentStates(jo, TurnState.PLANNING))
                    return resetState(jo);

                if(!playedPlanning) {
                    playedPlanning = true;
                    return GUIState.PLANNING;
                }

                return GUIState.WAIT_RESPONSE;
            case STUDENTS:
                int numMovedStudents = jo.get("numMovedStudents").getAsInt();
                playedPlanning = false;

                if(JsonUtils.areDifferentStates(jo, TurnState.STUDENTS))
                    return resetState(jo);

                if(matchInfo.getNumMovedStudents() != numMovedStudents
                        && numMovedStudents < MatchInfo.getInstance().getMaxMovableStudents())
                    return GUIState.MOVE_STUDENT;


                return GUIState.WAIT_RESPONSE;
            case MOTHER_NATURE:
                if(JsonUtils.areDifferentStates(jo, TurnState.MOTHER_NATURE))
                    return resetState(jo);

                return GUIState.WAIT_RESPONSE;
            case CLOUD:
                if(JsonUtils.areDifferentStates(jo, TurnState.CLOUD) && (jo.get("numMovedStudents").getAsInt() == 0)) {
                    return resetState(jo);
                }

                return GUIState.WAIT_RESPONSE;
            default:
                return GUIState.WAIT_RESPONSE;
        }
    }

    /**
     * Returns the base <code>GUIState</code> for each <code>TurnState</code> the game can be found in.
     *
     * @param jo the last packet received from the server.
     * @return the <code>GUIState</code> for the current game state.
     */
    public static GUIState resetState(JsonObject jo) {
        switch (JsonUtils.getTurnState(jo)) {
            case PLANNING:
                return GUIState.PLANNING;
            case STUDENTS:
                return GUIState.MOVE_STUDENT;
            case MOTHER_NATURE:
                return GUIState.MOVE_MN;
            case CLOUD:
                return GUIState.CLOUD;
            default:
                return GUIState.WAIT_RESPONSE;
        }
    }
}
