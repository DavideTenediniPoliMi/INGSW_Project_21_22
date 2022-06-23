package it.polimi.ingsw.view.cli;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.ServerConnectionCLI;
import it.polimi.ingsw.utils.JsonUtils;
import it.polimi.ingsw.view.cli.viewStates.*;
import it.polimi.ingsw.view.gui.GUIState;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLI {
     private final static Scanner scanner = new Scanner(System.in);
     private ViewState viewState;
     private int playerID;
     private String name;
     private boolean playedPlanning;
     private boolean boughtCard;
     private boolean waitForActivatedCard;
     private String activeCardName;
     private boolean activatedCard;
     private boolean waitingForPlayer;
     protected boolean exiting;

     public CLI(ViewState viewState) { this.viewState = viewState; }

     public void setViewState(ViewState viewState) { this.viewState = viewState; }

     public ViewState getViewState() { return viewState; }

     public void setPlayerID(int playerID) {
          this.playerID = playerID;
          viewState.setPlayerID(playerID);
     }

     public int getPlayerID() {
          return playerID;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getName() {
          return name;
     }

     public boolean nextState(JsonObject jo) {
          MatchInfo matchInfo = MatchInfo.getInstance();

          if(jo.has("error")) {
               resetInteraction(jo.get("error").getAsString());
               return true;
          }

          if(!jo.has("matchInfo")) {
               if(JsonUtils.isNotCharCardJSON(jo, playerID)) {
                    if(waitingForPlayer && jo.has("players")) {
                         waitingForPlayer = false;
                         if(matchInfo.getCurrentPlayerID() == playerID) {
                              setCurrentViewState();
                              return true;
                         }
                    }
                    return false;
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

               return false;
          }

          jo = jo.get("matchInfo").getAsJsonObject();

          if(JsonUtils.isGameOver(jo)) {
               setViewState(new EndGameViewState(getViewState()));
               return true;
          }

          if(JsonUtils.hasPlayerReconnected(jo)) {
               waitingForPlayer = true;
               return false;
          }

          if(JsonUtils.isGamePaused(jo)) {
               setViewState(new GameViewState(getViewState()));
               return false;
          }

          if(JsonUtils.isNotPlayerTurn(jo, playerID)) {
               setViewState(new GameViewState(getViewState()));
               return false;
          }

          if(MatchInfo.getInstance().isGamePaused()) {
               resetTurnState(jo);
               return true;
          }

          if(boughtCard) {
               boughtCard = false;

               if("INFLUENCE_ADD_TWO".equals(activeCardName)) {
                    return false;
               }

               if("IGNORE_TOWERS".equals(activeCardName)) {
                    resetTurnState(jo);
                    activeCardName = "";
                    return true;
               }

               waitForActivatedCard = true;
               return true;
          }

          if("INFLUENCE_ADD_TWO".equals(activeCardName)) {
               activeCardName = "";
               resetTurnState(jo);
               return true;
          }

          if(activatedCard) {
               activeCardName = "";
               activatedCard = false;
               resetTurnState(jo);
               return true;
          }

          switch(matchInfo.getStateType()) {
               case PLANNING:
                    if(JsonUtils.areDifferentStates(jo, TurnState.PLANNING)) {
                         resetTurnState(jo);
                         return true;
                    }

                    if(!playedPlanning) {
                         playedPlanning = true;
                         resetTurnState(jo);
                         return true;
                    }

                    return false;
               case STUDENTS:
                    int numMovedStudents = jo.get("numMovedStudents").getAsInt();
                    playedPlanning = false;

                    if(JsonUtils.areDifferentStates(jo, TurnState.STUDENTS)) {
                         resetTurnState(jo);
                         return true;
                    }

                    if(matchInfo.getNumMovedStudents() != numMovedStudents) {
                         if(numMovedStudents < MatchInfo.getInstance().getMaxMovableStudents()) {
                              setViewState(new StudentViewState(getViewState()));
                              return true;
                         }
                    }

                    return false;
               case MOTHER_NATURE:
                    if(JsonUtils.areDifferentStates(jo, TurnState.MOTHER_NATURE)) {
                         resetTurnState(jo);
                         return true;
                    }

                    return false;
               case CLOUD:
                    if(JsonUtils.areDifferentStates(jo, TurnState.CLOUD)) {
                         resetTurnState(jo);
                         return jo.get("numMovedStudents").getAsInt() == 0;
                    }

                    return false;
               default:
                    return false;
          }
     }

     public void resetTurnState(JsonObject jo) {
          switch (JsonUtils.getTurnState(jo)) {
               case PLANNING:
                    setViewState(new PlanningViewState(getViewState()));
                    break;
               case STUDENTS:
                    setViewState(new StudentViewState(getViewState()));
                    break;
               case MOTHER_NATURE:
                    setViewState(new MNViewState(getViewState()));
                    break;
               case CLOUD:
                    setViewState(new CloudViewState(getViewState()));
                    break;
               default:
                    setViewState(new GameViewState(getViewState()));
          }
     }

     private void setCurrentViewState() {
          switch (MatchInfo.getInstance().getStateType()) {
               case PLANNING:
                    setViewState(new PlanningViewState(getViewState()));
                    break;
               case STUDENTS:
                    setViewState(new StudentViewState(getViewState()));
                    break;
               case MOTHER_NATURE:
                    setViewState(new MNViewState(getViewState()));
                    break;
               case CLOUD:
                    setViewState(new CloudViewState(getViewState()));
                    break;
               default:
                    setViewState(new GameViewState(getViewState()));
          }
     }

     public void resetInteraction(String error) {
          getViewState().resetInteraction();
          getViewState().appendBuffer(error);
          getViewState().setInteractionComplete(false);
     }

     public void handleInteractionAsFirst() {
          playedPlanning = true;
          handleInteraction();
     }

     public void handleInteraction() {
          do {
               getViewState().printCLIPrompt(true);
               AnsiConsole.sysOut().println(AnsiCodes.CLS.code + AnsiCodes.HOME + getViewState().print() + getViewState().getBuffer());
               String input = scanner.nextLine().trim();

               if(getViewState().isInteractionComplete())
                    return;

               String error = getViewState().manageCLIInput(input);
               if (!"".equals(error))
                    AnsiConsole.sysOut().println(error);
          } while(!getViewState().isInteractionComplete());
     }

     public void displayState() {
          getViewState().printCLIPrompt(false);
          AnsiConsole.sysOut().println(AnsiCodes.CLS.code + AnsiCodes.HOME + getViewState().print() + getViewState().getBuffer());
     }

     public void handleHandshake() {
          do {
               getViewState().printCLIPrompt(true);
               AnsiConsole.sysOut().println(AnsiCodes.CLS.code + AnsiCodes.HOME + getViewState().getBuffer());

               setName(scanner.nextLine().trim());

               String error = getViewState().manageCLIInput(getName());

               if (!"".equals(error))
                    AnsiConsole.sysOut().println(error);
          } while(!getViewState().isInteractionComplete());
     }

     public void loadStartingScreen() {
          AnsiConsole.systemInstall();

          AnsiConsole.sysOut().print(AnsiCodes.CLS + "" + AnsiCodes.HOME);
          AnsiConsole.sysOut().println("Welcome to Eriantys!");
     }

     public Connection handleBinding() {
          do {
               AnsiConsole.sysOut().println("Insert the IP address of the server : (or Press X to close the game)");
               try {
                    String ip = scanner.nextLine().trim();

                    if(ip.equals("X")) {
                         exiting = true;
                         break;
                    }

                    AnsiConsole.sysOut().println("Insert the port :");

                    int port = Integer.parseInt(scanner.nextLine().trim());

                    return new ServerConnectionCLI(new Socket(ip, port));
                    //return new ServerConnectionCLI(new Socket("localhost", 12345), client);
               } catch(IOException | SecurityException | IllegalArgumentException e) {
                    AnsiConsole.sysOut().println("The combination IP/Port didn't work! Try again!");
               } catch(InputMismatchException e) {
                    AnsiConsole.sysOut().println("That was not a valid input! Try again!");
               } catch(Exception e) {
                    AnsiConsole.sysOut().println("Something went wrong, please restart the game");
                    exiting = true;
               }
          } while(true);

          return null;
     }

     public void loadClosingScreen() {
          AnsiConsole.sysOut().println("Thanks for playing Eriantys!");

          AnsiConsole.systemUninstall();
     }

     public boolean isExiting() {
          return exiting;
     }
}
