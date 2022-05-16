package it.polimi.ingsw.view.cli;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.view.viewStates.ViewState;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLI {
     private ViewState viewState;
     private static final Scanner scanner = new Scanner(System.in);

     public CLI(ViewState viewState) {
          this.viewState = viewState;
     }

     private void setViewState(ViewState viewState) {
          this.viewState = viewState;
     }

     public boolean nextState(JsonObject jo) {
          MatchInfo matchInfo = MatchInfo.getInstance();
          Game game = Game.getInstance();

          switch(matchInfo.getStateType()) {
               case PLANNING:
               case STUDENTS:
               case MOTHER_NATURE:
               case CLOUD:
                    return true;
          }

          return false;
     }

     public void handleInteraction() {
          do {
               AnsiConsole.sysOut().println(viewState.print() + viewState.printCLIPrompt());

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
