package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.List;

public class StudentViewState extends ExpertViewState {
    Game game = Game.getInstance();
    MatchInfo matchInfo = MatchInfo.getInstance();
    private boolean isColorSelected = false;
    private boolean isDestinationSelected = false;
    private boolean isDestinationIsland = false;
    private Color colorSelected = null;
    private final List<String> validColors = List.of("B", "G", "P", "R", "Y");
    private final List<Integer> validIslandIndexes = List.of(0,1,2,3,4,5,6,7,8,9,10,11);
    public StudentViewState(ViewState oldViewState) {
        super(oldViewState);
    }
    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        super.printCLIPrompt();
        StringBuilder stringBuilder = new StringBuilder();

        if(!isColorSelected) {
            stringBuilder.append("Select the student you want to move from your entrance: ");
            stringBuilder.append("[" + AnsiCodes.BLUE_BACKGROUND_BRIGHT + "B" + AnsiCodes.RESET + ", ");
            stringBuilder.append(AnsiCodes.GREEN_BACKGROUND_BRIGHT + "G" + AnsiCodes.RESET + ", ");
            stringBuilder.append(AnsiCodes.PURPLE_BACKGROUND_BRIGHT + "P" + AnsiCodes.RESET + ", ");
            stringBuilder.append(AnsiCodes.RED_BACKGROUND_BRIGHT + "R" + AnsiCodes.RESET + ", ");
            stringBuilder.append(AnsiCodes.YELLOW_BACKGROUND_BRIGHT + "Y" + AnsiCodes.RESET + "]");

            appendBuffer(stringBuilder.toString());
            return;
        }

        if(!isDestinationSelected) {
            stringBuilder.append("Select the destination:\n");
            stringBuilder.append("1) Dining room\n");
            stringBuilder.append("2) Island");

            appendBuffer(stringBuilder.toString());
            return;
        }

        if(isDestinationIsland) {
           appendBuffer("Select the island where you want to move your student: [index of island]\n");
        }
    }

    @Override
    public String manageCLIInput (String input) {
        appendBuffer(input);
        String error;

        if(!super.manageCLIInput(input).equals("")) {
            if(!isColorSelected) {
                isColorSelected = true;
                if(validColors.contains(input.toUpperCase())) {
                    int playerID = matchInfo.getCurrentPlayerID();
                    Board board= game.getBoard();
                    switch(input.toUpperCase()) {
                        case "B":
                            if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.BLUE) > 0) {
                                colorSelected = Color.BLUE;
                            }
                            break;
                        case "G":
                            if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.GREEN) > 0) {
                                colorSelected = Color.GREEN;
                            }

                            break;
                        case "P":
                            if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.PINK) > 0) {
                                colorSelected = Color.PINK;
                            }
                            break;
                        case "R":
                            if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.RED) > 0) {
                                colorSelected = Color.RED;
                            }
                            break;
                        case "Y":
                            if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.YELLOW) > 0) {
                                colorSelected = Color.YELLOW;
                            }
                            break;
                    }
                    if(colorSelected == null) {
                        error = "No such student of that color!";
                        return error;
                    }
                }
                return "";
            }

            if(!isDestinationSelected) {
                isDestinationSelected = true;
                if(input.equalsIgnoreCase("Dining room")) {
                    notify(
                            new RequestParameters()
                                    .setCommandType(CommandType.TRANSFER_STUDENT_TO_DINING_ROOM)
                                    .setColor(colorSelected)
                                    .serialize().toString()
                    );
                    setInteractionComplete(true);
                    return "";
                }
                if(input.equalsIgnoreCase("Island")) {
                    isDestinationIsland = true;
                    return "";
                }
            }

            if(isDestinationIsland) {
                error = StringUtils.checkInteger(input, validIslandIndexes);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }
                int islandIndex = Integer.parseInt(input);
                notify(
                        new RequestParameters()
                                .setCommandType(CommandType.TRANSFER_STUDENT_TO_ISLAND)
                                .setIndex(islandIndex)
                                .setColor(colorSelected)
                                .serialize().toString()
                );
                setInteractionComplete(true);
                return "";
            }
        }
        return "That was not a valid choice! Try again!";
    }
}
