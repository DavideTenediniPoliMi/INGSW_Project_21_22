package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.cli.AnsiCodes;

public class LobbyViewState extends ViewState {
    MatchInfo matchInfo;
    Lobby lobby;

    public LobbyViewState(ViewState oldViewState) {
        super(oldViewState);

        matchInfo = MatchInfo.getInstance();
        lobby = Lobby.getLobby();
    }

    @Override
    public String print(boolean... params) {
        if(matchInfo.getSelectedNumPlayer() == 0) {
            return "No lobby found! Create one or wait for a friend to create one to join!";
        }

        StringBuilder stringBuilder = new StringBuilder();

        // HEADER STATS
        stringBuilder.append("Players connected : " + lobby.getPlayers().size() + " / " + matchInfo.getSelectedNumPlayer() + "  ||  ")
                .append("Expert mode : ");

        if(matchInfo.isExpertMode()) {
            stringBuilder.append(AnsiCodes.GREEN_TEXT + "ON" + AnsiCodes.RESET);
        } else {
            stringBuilder.append(AnsiCodes.RED_TEXT + "OFF" + AnsiCodes.RESET);
        }

        stringBuilder.append("\n");

        // PLAYER LIST
        stringBuilder.append("Players : \n");

        if(lobby.getPlayers().size() == 0) {
            stringBuilder.append("No player in this lobby yet!");
        }

        for(Player p : lobby.getPlayers()) {
            stringBuilder.append("Player " + p.getID() + " : ")
                    .append(p.getName() + "  ;  ")
                    .append(p.getCardBack() == null ? printChoosingStatus() : p.getCardBack().toString()) // TODO toString or print on CB
                    .append("  ;  ")
                    .append(p.getTeamColor() == null ? printChoosingStatus() : p.getTeamColor().print())
                    .append("  ;  ")
                    .append(" ready : ");

            if(lobby.isReady(p.getID())) {
                stringBuilder.append(AnsiCodes.GREEN_BACKGROUND_BRIGHT + " V " + AnsiCodes.RESET);
            } else {
                stringBuilder.append(AnsiCodes.RED_BACKGROUND_BRIGHT + " X " + AnsiCodes.RESET);
            }

            stringBuilder.append("  ;\n");
        }

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    private String printChoosingStatus() {
        return AnsiCodes.BLACK_TEXT + "choosing..." + AnsiCodes.RESET;
    }

    @Override
    public String printCLIPrompt() {
        return null;
    }

    @Override
    public String manageCLIInput(String input) {
        return null;
    }
}
