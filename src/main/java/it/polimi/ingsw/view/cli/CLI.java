package it.polimi.ingsw.view.cli;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.TurnState;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.ServerConnectionCLI;
import it.polimi.ingsw.utils.JsonUtils;
import it.polimi.ingsw.view.cli.viewStates.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that handles the state machine for the CLI visualization and management
 */
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
     private boolean waitingForPlayerRecon, waitingForPlayerDC;
     protected boolean exiting, skipping;

     public CLI(ViewState viewState) { this.viewState = viewState; }

     /**
      * Sets the specified <code>ViewState</code>.
      *
      * @param viewState <code>ViewState</code> that has to be set
      */
     public void setViewState(ViewState viewState) { this.viewState = viewState; }

     /**
      * Returns the current <code>ViewState</code>.
      *
      * @return Current <code>ViewState</code>
      */
     public ViewState getViewState() { return viewState; }

     /**
      * Set player ID.
      *
      * @param playerID ID of the player to be set
      */
     public void setPlayerID(int playerID) {
          this.playerID = playerID;
          viewState.setPlayerID(playerID);
     }

     /**
      * Returns the ID of the player correspondent to this CLI.
      *
      * @return ID of the player correspondent to this CLI
      */
     public int getPlayerID() {
          return playerID;
     }

     /**
      * Sets the specified name.
      *
      * @param name Name to be set
      */
     public void setName(String name) {
          this.name = name;
     }

     /**
      * Returns the name of the player correspondent to this CLI.
      *
      * @return Name of the player correspondent to this CLI
      */
     public String getName() {
          return name;
     }

     /**
      * Sets the new <code>ViewState</code> based on the current state and the interaction that has been performed.
      *
      * @param jo <code>JsonObject</code> that contains the info sent by the <code>ServerConnection</code>
      *
      * @return <code>true</code> if the interaction needs to be handled, <code>false</code> if the view only has to
      * display the content
      */
     public boolean nextState(JsonObject jo) {
          if(skipping) {
               if(JsonUtils.areDifferentStates(jo, MatchInfo.getInstance().getStateType())
                    && JsonUtils.getTurnState(jo).equals(TurnState.PLANNING)) {
                    skipping = false;
                    setViewState(new GameViewState(viewState)); //Reset old viewState
               }
               else return false;
          }
          MatchInfo matchInfo = MatchInfo.getInstance();

          if(jo.has("error")) {
               resetInteraction(jo.get("error").getAsString());
               return true;
          }

          if(!jo.has("matchInfo")) {
               if(JsonUtils.isNotCharCardJSON(jo, playerID)) {
                    if(waitingForPlayerDC && jo.has("players")) {
                         waitingForPlayerDC = false;
                    }
                    if(waitingForPlayerRecon && jo.has("players")) {
                         waitingForPlayerRecon = false;
                         if(matchInfo.getCurrentPlayerID() == playerID && matchInfo.isGamePaused()) {
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

          if(JsonUtils.hasPlayerDisconnected(jo)) {
               waitingForPlayerDC = true;
               return false;
          }

          if(JsonUtils.hasPlayerReconnected(jo)) {
               waitingForPlayerRecon = true;
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

     /**
      * Resets the <code>ViewState</code>.
      *
      * @param jo <code>JsonObject</code> that contains the infos about the previous state
      */
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

     /**
      * Sets the current <code>ViewState</code>.
      */
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

     /**
      * Resets the interaction if an error during an action occurs.
      *
      * @param error <code>String</code> correspondent to the error
      */
     public void resetInteraction(String error) {
          getViewState().resetInteraction();
          getViewState().appendBuffer(error);
          getViewState().setInteractionComplete(false);
     }

     /**
      * Handles the first interaction.
      */
     public void handleFirstInteraction() {
          if(shouldSkip()) {
               skipping = true;
               setViewState(new GameViewState(viewState));
               displayState();
               return;
          }
          playedPlanning = true;
          handleInteraction();
     }

     /**
      * Returns whether this CLI should skip the current round.
      *
      * @return <code>true</code> if this CLI has to skip the current round.
      */
     public boolean shouldSkip() {
          return (Game.getInstance().getPlayerByID(playerID).getSelectedCard() != null
                  && Game.getInstance().getPlayerByID(playerID).getSelectedCard().equals(Card.CARD_AFK)
                  && !MatchInfo.getInstance().getStateType().equals(TurnState.PLANNING));
     }

     /**
      * Handles interactions by printing the CLI prompt, waiting for the user input and managing it.
      */
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

     /**
      * Prints the <code>ViewState</code> and its buffer.
      */
     public void displayState() {
          getViewState().printCLIPrompt(false);
          AnsiConsole.sysOut().println(AnsiCodes.CLS.code + AnsiCodes.HOME + getViewState().print() + getViewState().getBuffer());
     }

     /**
      * Handles the interaction of handshaking.
      */
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

     /**
      * Prints the initial message of welcome.
      */
     public void loadStartingScreen() {
          AnsiConsole.systemInstall();

          AnsiConsole.sysOut().print(AnsiCodes.CLS + "" + AnsiCodes.HOME);
          AnsiConsole.sysOut().println("Welcome to Eriantys!");
     }

     /**
      * Handles the interaction of connecting to the server.
      *
      * @return The <code>ServerConnection</code> created if the connection has been created successfully,
      * <code>null</code> otherwise.
      */
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

     /**
      * Prints the final message.
      */
     public void loadClosingScreen() {
          AnsiConsole.sysOut().println("Thanks for playing Eriantys!");

          AnsiConsole.systemUninstall();
     }

     /**
      * Returns <code>boolean</code> that indicates whether the player is exiting the game.
      *
      * @return <code>boolean</code> that indicates whether the player is exiting the game
      */
     public boolean isExiting() {
          return exiting;
     }

     /**
      * Returns whether this CLI has to skip the current round
      *
      * @return <code>true</code> if this CLI has to skip this round.
      */
     public boolean isSkipping() {
          return skipping;
     }
}
