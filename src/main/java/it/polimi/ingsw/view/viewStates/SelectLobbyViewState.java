package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectLobbyViewState extends LobbyViewState {
    private final Lobby lobby = Lobby.getLobby();
    private List<String> validCardBacks = new ArrayList<>();
    private List<String> validTeams = new ArrayList<>();
    private final List<Integer> validNumbers = new ArrayList<>(List.of(0,1,2));
    private boolean isLastActionSetCardBack;
    private CardBack cardBack;
    private TowerColor team;


    public SelectLobbyViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        if(cardBack == null) {
            //it finds card backs already picked
            List<String> pickedCardBacks = new ArrayList<>();
            for(Player p : lobby.getPlayers()) {
                if(p.getCardBack() != null)
                    pickedCardBacks.add(p.getCardBack().toString());
            }
            validCardBacks = Stream.of(CardBack.values()).map((cb) -> cb.name()).collect(Collectors.toList());
            validCardBacks.removeAll(pickedCardBacks);

            appendBuffer("Choose your card back: " + validCardBacks.toString());
            return;
        }


        if(team == null) {
            //it finds teams already picked
            List<String> pickedTeams = new ArrayList<>();
            for(Player p : lobby.getPlayers()) {
                if(p.getTeamColor() != null)
                    pickedTeams.add(p.getTeamColor().toString());
            }
            validTeams = Stream.of(TowerColor.values()).map((tc) -> tc.name()).collect(Collectors.toList());
            validTeams.removeAll(pickedTeams);

            appendBuffer("Choose your team: " + validTeams.toString());
            return;
        }

        appendBuffer("Choose your next action: \n" +
                        "0) Set ready \n" +
                        "1) Change your card back \n" +
                        "2) Change your team");
    }

    @Override
    public String manageCLIInput (String input) {
        appendBuffer(input);
        String error;

        if(validCardBacks.contains(input)) {
            cardBack = CardBack.valueOf(input);
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_CARDBACK)
                            .setCardBack(cardBack)
                            .serialize().toString()
            );
            isLastActionSetCardBack = true;
            setInteractionComplete(true);
            return "";
        }

        if(validTeams.contains(input)) {
            team = TowerColor.valueOf(input);
            input = input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_TOWERCOLOR)
                            .setTowerColor(team)
                            .serialize().toString()
            );
            isLastActionSetCardBack = false;
            setInteractionComplete(true);
            return "";
        }

        if(cardBack != null && team != null) {
            error = StringUtils.checkInteger(input, validNumbers);
            if (!error.equals("")) {
                appendBuffer(error);
                return error;
            }

            switch (Integer.parseInt(input)) {
                case 0:
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.READY_UP)
                                    .setReady(true)
                                    .serialize().toString()
                    );
                    isLastActionSetCardBack = true;
                    break;
                case 1:
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.UNSEL_CARDBACK)
                                    .serialize().toString()
                    );
                    isLastActionSetCardBack = false;
                    break;
                case 2:
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.UNSEL_TOWERCOLOR)
                                    .serialize().toString()
                    );
                    break;
            }
            setInteractionComplete(true);
            return "";
        }

        return "That was not a valid choice! Try again!";
    }

    @Override
    public void resetInteraction() {
        if(isLastActionSetCardBack)
            cardBack = null;
        else
            team = null;
    }
}
