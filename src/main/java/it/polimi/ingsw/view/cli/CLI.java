package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.view.views.View;
import it.polimi.ingsw.view.views.LobbyView;
import org.fusesource.jansi.AnsiConsole;

public class CLI {
        public static void main(String[] args){
            Lobby lobby = Lobby.getLobby();
            View lobbyView = new LobbyView();
            MatchInfo matchInfo = MatchInfo.getInstance();

            matchInfo.setUpGame(3,false);
            lobby.addPlayer(0, "Paolo");
            lobby.selectTeam(0, TowerColor.WHITE);
            lobby.selectCardBack(0, CardBack.CB_1);
            lobby.setReadyStatus(0, true);
            lobby.addPlayer(1, "Pietro");


            AnsiConsole.systemInstall();
            AnsiConsole.sysOut().println(AnsiCodes.CLS);
            AnsiConsole.sysOut().println(AnsiCodes.HOME);

            AnsiConsole.sysOut().print(lobbyView.print());

            AnsiConsole.systemInstall();
        }
}
