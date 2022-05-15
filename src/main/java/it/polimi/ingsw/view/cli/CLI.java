package it.polimi.ingsw.view.cli;

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

     public void setView(ViewState viewState) {
          this.viewState = viewState;
     }

     public static Connection handleBinding(Client client) {
          AnsiConsole.sysOut().print(AnsiCodes.CLS + "" + AnsiCodes.HOME);
          AnsiConsole.sysOut().println("Welcome to Eriantys!");

          do {
               AnsiConsole.sysOut().println("Insert the IP address of the server : (Type X to close the game)");
               String ip = scanner.nextLine();

               if(ip.equals("X")) break;

               AnsiConsole.sysOut().println("Insert the port :");
               int port = scanner.nextInt();
               scanner.nextLine();

               try {
                    return new ServerConnection(new Socket(ip, port), client);
               } catch(IOException e) {
                    AnsiConsole.sysOut().println("The combination IP/Port didn't work! Try again!");
               } catch(Exception e) {
                    AnsiConsole.sysOut().println("Something went wrong! Try again!");
               }
          } while(true);

          return null;
     }
}
