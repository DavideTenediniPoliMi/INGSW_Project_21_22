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
    public void printCLIPrompt(boolean shouldPrint) {
        if(!shouldPrint) return;

        setInteractionComplete(false);
        if(cardBack == null) {
            //it finds card backs already picked
            List<String> pickedCardBacks = new ArrayList<>();
            for(Player p : lobby.getPlayers()) {
                if(p.getCardBack() != null)
                    pickedCardBacks.add(p.getCardBack().toString());
            }
            validCardBacks = Stream.of(CardBack.values()).map(Enum::name).collect(Collectors.toList());
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
            validTeams = Stream.of(TowerColor.values()).map(Enum::name).collect(Collectors.toList());
            validTeams.removeAll(pickedTeams);

            if(MatchInfo.getInstance().getSelectedNumPlayer() != 3) {
                validTeams.remove("GREY");
            }

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

        if(validCardBacks.contains(input.toUpperCase())) {
            input = input.toUpperCase();
            cardBack = CardBack.valueOf(input);
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_CARDBACK)
                            .setIndex(playerID)
                            .setCardBack(cardBack)
                            .serialize().toString()
            );
            isLastActionSetCardBack = true;
            setInteractionComplete(true);
            return "";
        }

        if(validTeams.contains(input.toUpperCase())) {
            input = input.toUpperCase();
            team = TowerColor.valueOf(input);
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.SEL_TOWERCOLOR)
                            .setIndex(playerID)
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
                                    .setIndex(playerID)
                                    .serialize().toString()
                    );
                    break;
                case 1:
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.UNSEL_CARDBACK)
                                    .setIndex(playerID)
                                    .serialize().toString()
                    );
                    cardBack = null;
                    isLastActionSetCardBack = true;
                    break;
                case 2:
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.UNSEL_TOWERCOLOR)
                                    .setIndex(playerID)
                                    .serialize().toString()
                    );
                    team = null;
                    isLastActionSetCardBack = false;
                    break;
            }
            setInteractionComplete(true);
            return "";
        }

        error = "That was not a valid choice! Try again!";
        appendBuffer(error);
        return error;
    }

    @Override
    public void resetInteraction() {
        if(isLastActionSetCardBack)
            cardBack = null;
        else
            team = null;
    }
}
