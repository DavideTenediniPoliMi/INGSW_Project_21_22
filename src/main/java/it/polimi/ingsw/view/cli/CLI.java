package it.polimi.ingsw.view.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.ServerConnectionCLI;
import it.polimi.ingsw.view.viewStates.*;
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
               if(!checkForCharacterCards(jo)) {
                    return false;
               }

               System.out.println("SAW CHARACTER CARDS");

               activeCardName = getActiveCardName(jo);

               if(!activeCardName.equals("")) {
                    if(waitForActivatedCard) {
                         System.out.println("WAS WAITING");
                         waitForActivatedCard = false;
                         activatedCard = true;
                    } else {
                         System.out.println("BOUGHT CARD");
                         boughtCard = true;
                    }
               }

               return false;
          }

          jo = jo.get("matchInfo").getAsJsonObject();

          if(isGameOver(jo)) {
               setViewState(new EndGameViewState(getViewState()));
               return true;
          }

          if(hasPlayerReconnected(jo)) {
               return false;
          }

          if(isGamePaused(jo)) {
               setViewState(new GameViewState(getViewState()));
               return false;
          }

          if(!isPlayerTurn(jo))
               return false;

          if(MatchInfo.getInstance().isGamePaused()) {
               resetTurnState(jo);
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
                    resetTurnState(jo);
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
               resetTurnState(jo);
               return true;
          }

          if(activatedCard) {
               System.out.println("ACTIVATED CARD FROM USER");
               activeCardName = "";
               activatedCard = false;
               resetTurnState(jo);
               return true;
          }

          System.out.println("DIDN'T BUY CARDS");

          switch(matchInfo.getStateType()) {
               case PLANNING:
                    if(areDifferentStates(jo, TurnState.PLANNING)) {
                         resetTurnState(jo);
                         return true;
                    }

                    if(isPlayerTurn(jo) && !playedPlanning) {
                         playedPlanning = true;
                         resetTurnState(jo);
                         return true;
                    }

                    return false;
               case STUDENTS:
                    int numMovedStudents = jo.get("numMovedStudents").getAsInt();
                    playedPlanning = false;

                    if(areDifferentStates(jo, TurnState.STUDENTS)) {
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
                    if(areDifferentStates(jo, TurnState.MOTHER_NATURE)) {
                         resetTurnState(jo);
                         return true;
                    }

                    return false;
               case CLOUD:
                    if(areDifferentStates(jo, TurnState.CLOUD)) {
                         resetTurnState(jo);
                         return jo.get("numMovedStudents").getAsInt() == 0;
                    }

                    return false;
               default:
                    return false;
          }
     }

     private boolean checkForCharacterCards(JsonObject jo) {
          MatchInfo matchInfo = MatchInfo.getInstance();
          return jo.has("characterCards") &&
                  matchInfo.getCurrentPlayerID() == playerID &&
                  !matchInfo.getStateType().equals(TurnState.PLANNING) &&
                  !matchInfo.getStateType().equals(TurnState.CLOUD);
     }

     private String getActiveCardName(JsonObject jo) {
          JsonArray jsonArray = jo.get("characterCards").getAsJsonArray();
          for (JsonElement je : jsonArray) {
               String name = je.getAsJsonObject().get("name").getAsString();
               CharacterCard c = CharacterCards.valueOf(name).instantiate();
               c.deserialize(je.getAsJsonObject());

               if(c.isActive())
                    return c.getName();
          }
          return "";
     }

     private boolean isPlayerTurn(JsonObject jo) {
          if(jo.get("playOrder").getAsJsonArray().size() == 0 ||
                  jo.get("playOrder").getAsJsonArray().get(0).getAsInt() != playerID) {
               setViewState(new GameViewState(getViewState()));
               return false;
          }
          return true;
     }

     private boolean isGameOver(JsonObject jo) {
          if(jo.has("gameOver")) {
               return jo.get("gameOver").getAsBoolean();
          }
          return false;
     }

     private boolean hasPlayerReconnected(JsonObject jo) {
          return MatchInfo.getInstance().getNumPlayersConnected() != jo.get("numPlayersConnected").getAsInt();
     }

     private boolean isGamePaused(JsonObject jo) {
          if(jo.has("gamePaused")) {
               return jo.get("gamePaused").getAsBoolean();
          }
          return false;
     }

     public void resetTurnState(JsonObject jo) {
          switch(getTurnState(jo)) {
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

     private boolean areDifferentStates(JsonObject jo, TurnState state) {
          return !getTurnState(jo).equals(state);
     }

     private TurnState getTurnState(JsonObject jo) {
          return TurnState.valueOf(jo.get("stateType").getAsString());
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
