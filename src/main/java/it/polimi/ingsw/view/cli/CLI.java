package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.viewStates.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLI extends View {
     private final static Scanner scanner = new Scanner(System.in);

     public CLI(ViewState viewState) {
          super(viewState);
     }

     public void handleInteraction() {
          do {
               synchronized(this){
                    getViewState().printCLIPrompt(true);
                    AnsiConsole.sysOut().println(AnsiCodes.CLS.code + AnsiCodes.HOME + getViewState().print() + getViewState().getBuffer());
               }
               String input = scanner.nextLine().trim();

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

     public static Connection handleBinding(Client client) {
          do {
               AnsiConsole.sysOut().println("Insert the IP address of the server : (or Press X to close the game)");
               try {
                    String ip = scanner.nextLine().trim();

                    if(ip.equals("X")) {
                         break;
                    }

                    AnsiConsole.sysOut().println("Insert the port :");

                    int port = Integer.parseInt(scanner.nextLine().trim());

                    return new ServerConnection(new Socket(ip, port), client);
                    //return new ServerConnection(new Socket("localhost", 12345), client);
               } catch(IOException | SecurityException | IllegalArgumentException e) {
                    AnsiConsole.sysOut().println("The combination IP/Port didn't work! Try again!");
               } catch(InputMismatchException e) {
                    AnsiConsole.sysOut().println("That was not a valid input! Try again!");
               } catch(Exception e) {
                    AnsiConsole.sysOut().println("Something went wrong! Try again!");
               }
          } while(true);

          return null;
     }
}
