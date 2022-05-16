package it.polimi.ingsw.view.cli;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.view.viewStates.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLI {
     private ViewState viewState;
     private int playerID;
     private static final Scanner scanner = new Scanner(System.in);

     public CLI(ViewState viewState) {
          this.viewState = viewState;
     }

     private void setViewState(ViewState viewState) {
          this.viewState = viewState;
     }

     public boolean nextState(JsonObject jo) {
          MatchInfo matchInfo = MatchInfo.getInstance();

          if(!jo.has("matchInfo")) {
               return checkForCharacterCards(jo);
          }

          jo = jo.get("matchInfo").getAsJsonObject();

          if(!isPlayerTurn(jo))
               return true;

          switch(matchInfo.getStateType()) {
               case PLANNING:
                    if(areDifferentStates(jo, TurnState.PLANNING)) {
                         resetTurnState(jo);
                         return true;
                    }

                    return false;
               case STUDENTS:
                    if(areDifferentStates(jo, TurnState.STUDENTS)) {
                         resetTurnState(jo);
                         return true;
                    }

                    if(matchInfo.getNumMovedStudents() != jo.get("numMovedStudents").getAsInt()) {
                         setViewState(new ExpertViewState(new StudentViewState(viewState)));
                         return true;
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
                         return true;
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

     private boolean isPlayerTurn(JsonObject jo) {
          if(jo.get("playOrder").getAsJsonArray().size() == 0 ||
                  jo.get("playOrder").getAsJsonArray().get(0).getAsInt() != playerID) {
               setViewState(new GameViewState(viewState));
               return false;
          }
          return true;
     }

     private void resetTurnState(JsonObject jo) {
          switch(getTurnState(jo)) {
               case PLANNING:
                    setViewState(new PlanningViewState(viewState));
                    break;
               case STUDENTS:
                    setViewState(new ExpertViewState(new StudentViewState(viewState)));
                    break;
               case MOTHER_NATURE:
                    setViewState(new ExpertViewState(new MNViewState(viewState)));
                    break;
               case CLOUD:
                    setViewState(new CloudViewState(viewState));
                    break;
               default:
                    setViewState(new GameViewState(viewState));
          }
     }

     private boolean areDifferentStates(JsonObject jo, TurnState state) {
          return !getTurnState(jo).equals(state);
     }

     private TurnState getTurnState(JsonObject jo) {
          return TurnState.valueOf(jo.get("stateType").getAsString());
     }

     public void handleInteraction() {
          do {
               AnsiConsole.sysOut().println(viewState.print() + viewState.getBuffer());

               String error = viewState.manageCLIInput(scanner.nextLine());

               if (error != null)
                    AnsiConsole.sysOut().println(error);
          } while(!viewState.isInteractionComplete());
     }

     public static Connection handleBinding(Client client) {
          do {
               AnsiConsole.sysOut().println("Insert the IP address of the server : (or Press X to close the game)");
               String ip = scanner.nextLine();

               if(ip.equals("X")) break;

               AnsiConsole.sysOut().println("Insert the port :");
               int port = scanner.nextInt();
               scanner.nextLine();

               try {
                    return new ServerConnection(new Socket(ip, port), client);
               } catch(IOException | SecurityException | IllegalArgumentException e) {
                    AnsiConsole.sysOut().println("The combination IP/Port didn't work! Try again!");
               } catch(Exception e) {
                    AnsiConsole.sysOut().println("Something went wrong! Try again!" + e.getMessage());
               }
          } while(true);

          return null;
     }
}
