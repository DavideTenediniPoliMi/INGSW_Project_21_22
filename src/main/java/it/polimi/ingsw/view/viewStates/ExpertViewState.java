package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.characters.StudentGroupDecorator;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentGroup;
import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;
import it.polimi.ingsw.view.cli.AnsiCodes;

import java.util.List;

@SuppressWarnings({"StringBufferReplaceableByString", "StringConcatenationInsideStringBufferAppend"})
public class ExpertViewState extends GameViewState {
    protected final Game game = Game.getInstance();
    protected final MatchInfo matchInfo = MatchInfo.getInstance();
    private final List<Integer> validCardIndexes = List.of(0,1,2);
    protected final List<Integer> validMenuChoice = List.of(1,2);
    protected final List<Integer> validIslandIndexes = List.of(0,1,2,3,4,5,6,7,8,9,10,11);
    private final List<Integer> validNumOfStudentsToSwap = List.of(1,2,3);
    private final List<Integer> validNumOfStudentsToExchange = List.of(1,2);
    protected final List<String> validColors = List.of("B", "G", "P", "R", "Y");
    protected boolean isStudentsPhase = true;
    protected boolean isMovingStudents = false;
    protected boolean isMovingMN = false;
    protected boolean choiceSelected = false;
    protected int menuChoice = 0;

    private boolean cardBought = false;
    private boolean isCardSelected = false;
    CharacterCard activeCard = null;
    private int boughtCardIndex;
    private boolean cardActivated = false;

    protected Color colorSelected = null;
    private Color cardColorSelected = null;
    private Color colorDiningSelected = null;

    private int studentsSwapped = 0;
    private int numStudentsToSwap = 0;
    private boolean fromOriginSwapCompleted = false;
    private StudentGroup entranceStudents = new StudentGroup();
    private StudentGroup diningStudents = new StudentGroup();
    private StudentGroup cardStudents = new StudentGroup();

    private boolean isLastActionBuyCharacterCard = false;

    public ExpertViewState(ViewState oldViewState) {
        super(oldViewState);
    }

    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        System.out.println("inside expert");
        setInteractionComplete(false);

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
        System.out.println("inside print menu");
        StringBuilder stringBuilder = new StringBuilder();

        if(!cardBought) {
            stringBuilder.append("Choose your action:\n");
            stringBuilder.append("1) Buy character card\n");
            stringBuilder.append("2) Move " + movementSubject);

            return stringBuilder.toString();
        }
        System.out.println("between");
        if(!isCardSelected) {
            return ("Select the card to be bought: [0,1,2]");
        }

        if(!cardActivated) {
            activeCard = game.getActiveCharacterCard();
            return printCLICardActivationParameters();
        }
        return "";
    }

    protected String printCLIStudentColorSelection() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[" + AnsiCodes.BLUE_TEXT + "B" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.GREEN_TEXT + "G" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.PURPLE_TEXT + "P" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.RED_TEXT + "R" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.YELLOW_TEXT + "Y" + AnsiCodes.RESET + "]");

        return stringBuilder.toString();
    }

    private String printCLICardActivationParameters() {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(activeCard);

        System.out.println("before switch");
        switch(activeCard.getName()) {
            case "MOVE_TO_ISLAND":
                System.out.println("inside card");
                if(cardColorSelected == null) {
                    stringBuilder.append("Select the student you want to move from the card to an island: ");
                    stringBuilder.append(printCLIStudentColorSelection());
                    return stringBuilder.toString();
                }
                return "Select the island on which you want to move your student: [index of island]";
            case "MOVE_TO_DINING_ROOM":
                stringBuilder.append("Select the student you want to move from the card to your dining room: ");
                stringBuilder.append(printCLIStudentColorSelection());

                return stringBuilder.toString();
            case "POOL_SWAP":
                if(numStudentsToSwap == 0 && !fromOriginSwapCompleted) {
                    return "How many students do you want to swap?";
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
                if(numStudentsToSwap == 0 && !fromOriginSwapCompleted) {
                    return "How many students do you want to swap?";
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
        return "";
    }

    @Override
    public String manageCLIInput (String input) {
        appendBuffer(input);

        if(matchInfo.isExpertMode() && !choiceSelected) {
            if (isStudentsPhase) {
                if (!isMovingStudents) {
                    return manageCLICardMenu(input, true);
                }
                return "students";
            }

            if (!isMovingMN) {
                return manageCLICardMenu(input, false);
            }
        }

        return "";
    }

    private String manageCLICardMenu(String input, boolean isStudentsPhase) {
        String error;

        if(!cardBought) {
            System.out.println("not card bought");
            error = StringUtils.checkInteger(input, validMenuChoice);
            if(!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            menuChoice = Integer.parseInt(input);
            if (menuChoice == 2) {
                choiceSelected = true;
                isLastActionBuyCharacterCard = false;
                if (isStudentsPhase) {
                    System.out.println("manage Students");
                    isMovingStudents = true;
                    return "manage students";
                }
                isMovingMN = true;
                System.out.println("manage MN");
                return "manage MN";
            }
            isLastActionBuyCharacterCard = true;
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

            if(game.getCharacterCards().get(boughtCardIndex).getName().equals("INFLUENCE_ADD_TWO")) {
                CardParameters cardParameters = new CardParameters()
                        .setBoostedTeam(game.getPlayerByID(playerID).getTeamColor())
                        .setSelectedColor(Color.RED);   //not needed

                notifyCardParameters(cardParameters);
                setInteractionComplete(true);
                isCardSelected = true;
                cardActivated = true;
                return "";
            }

            setInteractionComplete(true);
            isCardSelected = true;
            return "";
        }
        if(!cardActivated) {
            try {
                return manageCLICardActivationParameters(input);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    private String manageCLICardActivationParameters(String input) throws InterruptedException {
        String error;
        CardParameters cardParams = new CardParameters();
        System.out.println("before manage switch");
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
                int islandAbsoluteIndex = game.getBoard().getOriginalIndexOf(Integer.parseInt(input));
                int islandIndex = game.getBoard().getIndexOfMultiIslandContainingIslandOfIndex(islandAbsoluteIndex);

                cardParams.setFromOrigin(new StudentGroup(cardColorSelected,1))
                        .setPlayerID(playerID)
                        .setIslandIndex(islandIndex)
                        .setFromDestination(new StudentGroup());    //not needed

                cardColorSelected = null;

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "MOVE_TO_DINING_ROOM":
                error = manageCLICardStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }

                cardParams.setFromOrigin(new StudentGroup(cardColorSelected,1))
                        .setPlayerID(playerID)
                        .setIslandIndex(0)      //not needed
                        .setFromDestination(new StudentGroup());    //not needed

                cardColorSelected = null;

                notifyCardParameters(cardParams);
                notifyCardActivation();
                break;
            case "POOL_SWAP":
                System.out.println("start pool swap - numStudentsToSwap: " + numStudentsToSwap);
                if(numStudentsToSwap == 0 && !fromOriginSwapCompleted) {
                    return manageCLINumStudentsToSwap(input, validNumOfStudentsToSwap);
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap-1) {
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
                    if (cardColorSelected == null) {
                        error = manageCLICardStudentColor(input);
                        if (!error.equals("")) {
                            appendBuffer(error);
                            return error;
                        }
                        cardStudents.addByColor(cardColorSelected, 1);
                        cardColorSelected = null;
                        fromOriginSwapCompleted = true;
                        studentsSwapped = 0;
                        return "";
                    }
                }
                if(studentsSwapped < numStudentsToSwap-1) {
                    manageCLIFromEntranceSwap(input);
                    return "";
                }
                manageCLIFromEntranceSwap(input);
                studentsSwapped = 0;
                fromOriginSwapCompleted = false;

                cardParams.setFromOrigin(cardStudents)
                        .setPlayerID(playerID)
                        .setFromDestination(entranceStudents)
                        .setIslandIndex(0);     //not needed

                notifyCardParameters(cardParams);
                notifyCardActivation();

                cardStudents = new StudentGroup();
                entranceStudents = new StudentGroup();
                break;
            case "IGNORE_COLOR":
                error = manageCLIStudentColor(input);
                if(!error.equals("")) {
                    appendBuffer(error);
                    return error;
                }

                cardParams.setSelectedColor(colorSelected)
                        .setBoostedTeam(TowerColor.BLACK);      //not needed

                colorSelected = null;

                notifyCardParameters(cardParams);
                break;
            case "EXCHANGE_STUDENTS":
                if(numStudentsToSwap == 0 && !fromOriginSwapCompleted) {
                    return manageCLINumStudentsToSwap(input, validNumOfStudentsToExchange);
                }

                int numStudentsInDining = 0;
                for(Color c : Color.values()) {
                    numStudentsInDining += game.getBoard().getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
                }
                if(numStudentsInDining < numStudentsToSwap) {
                    appendBuffer("Not enough students in dining room!");
                    cardBought = false;
                    isCardSelected = false;
                    cardActivated = false;
                    return "";
                }

                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap) {
                        manageCLIFromEntranceSwap(input);
                    }
                    fromOriginSwapCompleted = true;
                    colorSelected = null;
                    studentsSwapped = 0;
                }
                if(studentsSwapped < numStudentsToSwap) {
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
                studentsSwapped = 0;
                colorSelected = null;
                colorDiningSelected = null;
                entranceStudents = new StudentGroup();
                diningStudents = new StudentGroup();

                cardParams.setPlayerID(playerID)
                        .setFromOrigin(entranceStudents)
                        .setFromDestination(diningStudents)
                        .setPlayerID(0);        //not needed

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
        cardActivated = true;
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
        System.out.println("manage num swap - numStudentsToSwap: " + numStudentsToSwap);
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
        return "";
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
                appendBuffer(error);
                return error;
            }
            return "";
        }
        error = "That was not a valid choice! Try again! - cardStudentColor";
        appendBuffer(error);
        return error;
    }

    protected String manageCLIEntranceStudentColor(String input) {
        System.out.println("manage entrance");
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
                appendBuffer(error);
                return error;
            }
            return "";
        }
        error = "That was not a valid choice! Try again! - entrance student color";
        appendBuffer(error);
        return error;
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
                appendBuffer(error);
                return error;
            }
            return "";
        }
        error = "That was not a valid choice! Try again! - dining student color";
        appendBuffer(error);
        return error;
    }

    private String manageCLIStudentColor(String input) {
        if(validColors.contains(input.toUpperCase())) {
            switch(input.toUpperCase()) {
                case "B":
                    colorSelected = Color.BLUE;
                    break;
                case "G":
                    colorSelected = Color.GREEN;
                    break;
                case "P":
                    colorSelected = Color.PINK;
                    break;
                case "R":
                    colorSelected = Color.RED;
                    break;
                case "Y":
                    colorSelected = Color.YELLOW;
                    break;
            }
            return "";
        }
        String error = "That was not a valid choice! Try again! - student color";
        appendBuffer(error);
        return error;
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

    @Override
    public void resetInteraction() {
        if(isLastActionBuyCharacterCard) {
            cardBought = false;
        }
    }
}
