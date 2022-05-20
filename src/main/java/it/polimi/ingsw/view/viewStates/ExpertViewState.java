package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.List;

@SuppressWarnings({"StringBufferReplaceableByString", "StringConcatenationInsideStringBufferAppend"})
public class ExpertViewState extends GameViewState {
    Game game = Game.getInstance();
    private final MatchInfo matchInfo = MatchInfo.getInstance();
    private final List<Integer> validCardIndexes = List.of(0,1,2);
    private final List<Integer> validMenuChoice = List.of(1,2);
    protected final List<Integer> validIslandIndexes = List.of(0,1,2,3,4,5,6,7,8,9,10,11);
    private final List<Integer> validNumOfStudentsToSwap = List.of(1,2,3);
    private final List<Integer> validNumOfStudentsToExchange = List.of(1,2);
    protected final List<String> validColors = List.of("B", "G", "P", "R", "Y");
    protected boolean isStudentsPhase = true;
    protected boolean isMovingStudents = false;
    protected boolean isMovingMN = false;

    private boolean cardBought = false;
    private boolean isCardSelected = false;
    CharacterCard activeCard = null;
    private int boughtCardIndex;

    protected Color colorSelected;
    private Color cardColorSelected;
    private Color selectedColor;
    private Color colorDiningSelected;

    private int studentsSwapped = 0;
    private int numStudentsToSwap = 0;
    private boolean fromOriginSwapCompleted = false;
    private StudentGroup entranceStudents = new StudentGroup();
    private StudentGroup diningStudents = new StudentGroup();
    private StudentGroup cardStudents = new StudentGroup();

    public ExpertViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        setInteractionComplete(false);
        if(!matchInfo.isExpertMode()) {
            return;
        }
        if(isStudentsPhase) {
            if(!isMovingStudents) {
                appendBuffer(printCLICardMenu("student"));
            }
            return;
        }

        if(!isMovingMN) {
            appendBuffer(printCLICardMenu("mother nature"));
        }
    }

    private String printCLICardMenu(String movementSubject) {
        StringBuilder stringBuilder = new StringBuilder();

        if(!cardBought) {
            stringBuilder.append("Choose your action:\n");
            stringBuilder.append("1) Buy character card\n");
            stringBuilder.append("2) Move " + movementSubject + "\n");

            return stringBuilder.toString();
        }

        if(!isCardSelected) {
            return ("Select the card to be bought: [0,1,2]");
        }

        return printCLICardActivationParameters();
    }

    protected String printCLIStudentColorSelection() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[" + AnsiCodes.BLUE_BACKGROUND_BRIGHT + "B" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.GREEN_BACKGROUND_BRIGHT + "G" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.PURPLE_BACKGROUND_BRIGHT + "P" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.RED_BACKGROUND_BRIGHT + "R" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.YELLOW_BACKGROUND_BRIGHT + "Y" + AnsiCodes.RESET + "]\n");

        return stringBuilder.toString();
    }

    private String printCLICardActivationParameters() {
        StringBuilder stringBuilder = new StringBuilder();

        switch(activeCard.getName()) {
            case "MOVE_TO_ISLAND":
                if(cardColorSelected == null) {
                    stringBuilder.append("Select the student you want to move from the card to an island: ");
                    stringBuilder.append(printCLIStudentColorSelection());
                    return stringBuilder.toString();
                }
                return "Select the island where you want to move your student: [index of island]";
            case "MOVE_TO_DINING":
                stringBuilder.append("Select the student you want to move from the card to your dining room: ");
                stringBuilder.append(printCLIStudentColorSelection());

                return stringBuilder.toString();
            case "POOL_SWAP":
                if(studentsSwapped == 0 && !fromOriginSwapCompleted) {
                    return "How many students do you want to swap?\n";
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap) {
                        if(cardColorSelected == null) {
                            stringBuilder.append("Select the student you want to swap from the card: ");
                            stringBuilder.append(printCLIStudentColorSelection());

                            return stringBuilder.toString();
                        }
                    }
                }
                if(studentsSwapped < numStudentsToSwap) {
                    stringBuilder.append("Select the student you want to swap from the entrance: ");
                    stringBuilder.append(printCLIStudentColorSelection());

                    return stringBuilder.toString();
                }
                break;
            case "IGNORE_COLOR":
                stringBuilder.append("Select the color to ignore: ");
                stringBuilder.append(printCLIStudentColorSelection());

                return stringBuilder.toString();
            case "EXCHANGE_STUDENTS":
                if(studentsSwapped == 0 && !fromOriginSwapCompleted) {
                    return "How many students do you want to swap?\n";
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap) {
                        if(cardColorSelected == null) {
                            stringBuilder.append("Select the student you want to swap from the entrance: ");
                            stringBuilder.append(printCLIStudentColorSelection());

                            return stringBuilder.toString();
                        }
                    }
                }
                if(studentsSwapped < numStudentsToSwap) {
                    stringBuilder.append("Select the student you want to swap from the dining room: ");
                    stringBuilder.append(printCLIStudentColorSelection());

                    return stringBuilder.toString();
                }
                break;
            case "RETURN_TO_BAG":
                stringBuilder.append("Select the color of the students to be removed: ");
                stringBuilder.append(printCLIStudentColorSelection());

                return stringBuilder.toString();
        }
        return null;
    }

    @Override
    public String manageCLIInput (String input) {
        appendBuffer(input);
        String error;

        if(!matchInfo.isExpertMode())
            return "Game is not in Expert Mode";

        if(isStudentsPhase) {
            if(!isMovingStudents) {
                return manageCLICardMenu(input, true);
            }
        }

        if(!isMovingMN) {
            return manageCLICardMenu(input, false);
        }

        return "";
    }

    private String manageCLICardMenu(String input, boolean isStudentsPhase) {
        String error;

        if(!cardBought) {
            error = StringUtils.checkInteger(input, validMenuChoice);
            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            int choice = Integer.parseInt(input);
            if(choice == 2) {
                if(isStudentsPhase) {
                    isMovingStudents = true;
                    return "manage students";
                }
                isMovingMN = true;
                return "manage MN";
            }
            cardBought = true;
            return "";
        }
        if(!isCardSelected) {
            error = StringUtils.checkInteger(input, validCardIndexes);
            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }

            boughtCardIndex = Integer.parseInt(input);

            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.BUY_CHARACTER_CARD)
                            .setIndex(boughtCardIndex)
                            .serialize().toString()
            );

            isCardSelected = true;
            return "";
        }
        activeCard = game.getActiveCharacterCard();
        return manageCLICardActivationParameters(input);
    }

    private String manageCLICardActivationParameters(String input) {
        String error;
        CardParameters cardParams = new CardParameters();

        switch (activeCard.getName()) {
            case "MOVE_TO_ISLAND":
                if(cardColorSelected == null) {
                    return manageCLICardStudentColor(input);
                }
                error = StringUtils.checkInteger(input, validIslandIndexes);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }
                int islandIndex = game.getBoard().getOriginalIndexOf(Integer.parseInt(input));

                cardParams.setFromOrigin(new StudentGroup(cardColorSelected,1))
                        .setIslandIndex(islandIndex);

                cardColorSelected = null;

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "MOVE_TO_DINING":
                error = manageCLICardStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }

                cardParams.setFromOrigin(new StudentGroup(cardColorSelected,1))
                        .setPlayerID(playerID);

                cardColorSelected = null;

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "POOL_SWAP":
                if(studentsSwapped == 0 && !fromOriginSwapCompleted) {
                    return manageCLINumStudentsToSwap(input, validNumOfStudentsToSwap);
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap) {
                        if (cardColorSelected == null) {
                            error = manageCLICardStudentColor(input);
                            if (!error.equals("")) {
                                appendBuffer(error);
                                return error;
                            }
                            cardStudents.addByColor(cardColorSelected, 1);
                            studentsSwapped++;
                            cardColorSelected = null;
                            return "";
                        }
                    }
                    fromOriginSwapCompleted = true;
                }
                if(studentsSwapped < numStudentsToSwap-1) {
                    return manageCLIFromEntranceSwap(input);
                }
                error = manageCLIEntranceStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }
                entranceStudents.addByColor(colorSelected, 1);

                cardParams.setFromOrigin(cardStudents)
                        .setPlayerID(playerID)
                        .setFromDestination(entranceStudents);

                studentsSwapped = 0;
                cardColorSelected = null;
                entranceStudents = new StudentGroup();
                cardStudents = new StudentGroup();

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "INFLUENCE_ADD_TWO":
                cardParams.setBoostedTeam(game.getPlayerByID(playerID).getTeamColor());

                notifyCardParameters(cardParams);
                break;
            case "IGNORE_COLOR":
                error = manageCLIStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }

                cardParams.setSelectedColor(selectedColor);

                selectedColor = null;

                notifyCardParameters(cardParams);
            case "EXCHANGE_STUDENTS":
                if(studentsSwapped == 0 && !fromOriginSwapCompleted) {
                    return manageCLINumStudentsToSwap(input, validNumOfStudentsToExchange);
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap) {
                        manageCLIFromEntranceSwap(input);
                    }
                    fromOriginSwapCompleted = true;
                }
                if(studentsSwapped < numStudentsToSwap-1) {
                    if (colorDiningSelected == null) {
                        error = manageCLIDiningStudentColor(input);
                        if(!error.equals("")) {
                            appendBuffer(error);
                            return error;
                        }
                        diningStudents.addByColor(colorDiningSelected, 1);
                        studentsSwapped++;
                        colorDiningSelected = null;
                        return "";
                    }
                }
                error = manageCLIDiningStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }
                diningStudents.addByColor(colorDiningSelected, 1);

                cardParams.setPlayerID(playerID)
                        .setFromOrigin(entranceStudents)
                        .setFromDestination(diningStudents);

                studentsSwapped = 0;
                colorSelected = null;
                colorDiningSelected = null;
                entranceStudents = new StudentGroup();
                diningStudents = new StudentGroup();

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "RETURN_TO_BAG":
                error = manageCLIStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }
                cardParams.setSelectedColor(colorSelected);

                colorSelected = null;

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
        }
        setInteractionComplete(true);
        return "";
    }

    private String manageCLINumStudentsToSwap(String input, List<Integer> validInputs) {
        String error = StringUtils.checkInteger(input, validInputs);
        if(!error.equals("")) {
            appendBuffer(error);
            return error;
        }
        numStudentsToSwap = Integer.parseInt(input);
        return "";
    }

    private String manageCLIFromEntranceSwap(String input) {
        String error;
        if (colorSelected == null) {
            error = manageCLIEntranceStudentColor(input);
            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            entranceStudents.addByColor(colorSelected, 1);
            studentsSwapped++;
            colorSelected = null;
            return "";
        }
        return null;
    }

    protected String manageCLICardStudentColor(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
            switch(input.toUpperCase()) {
                case "B":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.BLUE) > 0) {
                        cardColorSelected = Color.BLUE;
                    }
                    break;
                case "G":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.GREEN) > 0) {
                        cardColorSelected = Color.GREEN;
                    }

                    break;
                case "P":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.PINK) > 0) {
                        cardColorSelected = Color.PINK;
                    }
                    break;
                case "R":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.RED) > 0) {
                        cardColorSelected = Color.RED;
                    }
                    break;
                case "Y":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.YELLOW) > 0) {
                        cardColorSelected = Color.YELLOW;
                    }
                    break;
            }
            if(cardColorSelected == null) {
                error = "No such student of that color!";
                return error;
            }
            return "";
        }
        return "That was not a valid choice! Try again!";
    }

    protected String manageCLIEntranceStudentColor(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
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
            return "";
        }
        return "That was not a valid choice! Try again!";
    }

    private String manageCLIDiningStudentColor(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
            Board board= game.getBoard();
            switch(input.toUpperCase()) {
                case "B":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.BLUE) > 0) {
                        colorDiningSelected = Color.BLUE;
                    }
                    break;
                case "G":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.GREEN) > 0) {
                        colorDiningSelected = Color.GREEN;
                    }

                    break;
                case "P":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.PINK) > 0) {
                        colorDiningSelected = Color.PINK;
                    }
                    break;
                case "R":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.RED) > 0) {
                        colorDiningSelected = Color.RED;
                    }
                    break;
                case "Y":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.YELLOW) > 0) {
                        colorDiningSelected = Color.YELLOW;
                    }
                    break;
            }
            if(colorDiningSelected == null) {
                error = "No such student of that color!";
                return error;
            }
            return "";
        }
        return "That was not a valid choice! Try again!";
    }

    private String manageCLIStudentColor(String input) {
        if(validColors.contains(input.toUpperCase())) {
            switch(input.toUpperCase()) {
                case "B":
                    selectedColor = Color.BLUE;
                    break;
                case "G":
                    selectedColor = Color.GREEN;
                    break;
                case "P":
                    selectedColor = Color.PINK;
                    break;
                case "R":
                    selectedColor = Color.RED;
                    break;
                case "Y":
                    selectedColor = Color.YELLOW;
                    break;
            }
            return "";
        }
        return "That was not a valid choice! Try again!";
    }

    private void notifyCardParameters(CardParameters cardParams) {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.SET_CARD_PARAMETERS)
                        .setIndex(boughtCardIndex)
                        .setCardParams(cardParams)
                        .serialize().toString()
        );
    }

    private void notifyCardActivation() {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.ACTIVATE_CARD)
                        .serialize().toString()
        );
    }
}
