package it.polimi.ingsw.view.cli.viewStates;

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
    public void printCLIPrompt(boolean shouldPrint) {
        if(!shouldPrint) return;

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

    /**
     * Returns a <code>String</code> representing the choice menu in case of expert mode.
     *
     * @param movementSubject <code>String</code> correspondent to the subject of the action (student, MN)
     *
     * @return <code>String</code> representing the choice menu in case of expert mode
     */
    private String printCLICardMenu(String movementSubject) {
        StringBuilder stringBuilder = new StringBuilder();

        if(!cardBought) {
            stringBuilder.append("Choose your action:\n");
            stringBuilder.append("1) Buy character card\n");
            stringBuilder.append("2) Move " + movementSubject);

            return stringBuilder.toString();
        }

        if(!isCardSelected) {
            return "Select the card to be bought: [0,1,2]";
        }

        if(numStudentsToSwap == 0 && "EXCHANGE_STUDENTS".equals(game.getCharacterCards().get(boughtCardIndex).getName())) {
            return "How many students do you want to swap?";
        }

        if(!cardActivated) {
            activeCard = game.getActiveCharacterCard();
            return printCLICardActivationParameters();
        }
        return "";
    }

    /**
     * Returns the <code>String</code> representing the students color choice.
     *
     * @return <code>String</code> representing the students color choice.
     */
    protected String printCLIStudentColorSelection() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[" + AnsiCodes.BLUE_TEXT + "B" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.GREEN_TEXT + "G" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.PURPLE_TEXT + "P" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.RED_TEXT + "R" + AnsiCodes.RESET + ", ");
        stringBuilder.append(AnsiCodes.YELLOW_TEXT + "Y" + AnsiCodes.RESET + "]");

        return stringBuilder.toString();
    }

    /**
     * Return a String representing the action of setting the parameters of a character card, based on the type of card.
     *
     * @return <code>String</code> representing the action of set the parameters of a character card, based on the
     * type of card
     */
    private String printCLICardActivationParameters() {
        StringBuilder stringBuilder = new StringBuilder();

        switch(activeCard.getName()) {
            case "MOVE_TO_ISLAND":
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

    /**
     * /**
     * Manages the input received by the user and handles the menu choice in case of expert mode.
     *
     * @param input Input received from Input Stream
     * @param isStudentsPhase Boolean that indicates whether is in students phase or not
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLICardMenu(String input, boolean isStudentsPhase) {
        String error;

        if(!cardBought) {
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
                    isMovingStudents = true;
                    return "manage students";
                }
                isMovingMN = true;
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

            int playerCoins = game.getPlayerByID(playerID).getNumCoins();
            int cardCost = game.getCharacterCards().get(boughtCardIndex).getCost();
            if(playerCoins < cardCost) {
                cardBought = false;
                isCardSelected = false;
                error = "Not enough coins to buy this card!";
                appendBuffer(error);
                return error;
            }

            if("EXCHANGE_STUDENTS".equals(game.getCharacterCards().get(boughtCardIndex).getName())) {
                isCardSelected = true;
                return "";
            }

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

        if(numStudentsToSwap == 0 && "EXCHANGE_STUDENTS".equals(game.getCharacterCards().get(boughtCardIndex).getName())) {
            manageCLINumStudentsToSwap(input, validNumOfStudentsToExchange);

            int numStudentsInDining = 0;
            for(Color c : Color.values()) {
                numStudentsInDining += game.getBoard().getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(c);
            }
            if(numStudentsInDining < numStudentsToSwap) {
                appendBuffer("Not enough students in dining room!");
                cardBought = false;
                isCardSelected = false;
                return "";
            }

            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.BUY_CHARACTER_CARD)
                            .setIndex(boughtCardIndex)
                            .serialize().toString()
            );

            setInteractionComplete(true);
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

    /**
     * Manages the input received by the user and handles the action of setting parameters of a character card,
     * based on the type of card.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLICardActivationParameters(String input) throws InterruptedException {
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
                if(numStudentsToSwap == 0 && !fromOriginSwapCompleted) {
                    return manageCLINumStudentsToSwap(input, validNumOfStudentsToSwap);
                }
                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap-1) {
                        return manageCLIFromCardSwap(input);
                    }
                    error = manageCLIFromCardSwap(input);
                    if(!error.equals("")) {
                        return error;
                    }
                    fromOriginSwapCompleted = true;
                    studentsSwapped = 0;
                    return "";
                }
                if(studentsSwapped < numStudentsToSwap-1) {
                    manageCLIFromEntranceSwap(input);
                    return "";
                }
                error = manageCLIFromEntranceSwap(input);
                if(!error.equals("")) {
                    return error;
                }
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

                if(!fromOriginSwapCompleted) {
                    if(studentsSwapped < numStudentsToSwap-1) {
                        return manageCLIFromEntranceSwap(input);
                    }
                    error = manageCLIFromEntranceSwap(input);
                    if(!error.equals("")) {
                        return error;
                    }
                    fromOriginSwapCompleted = true;
                    colorSelected = null;
                    studentsSwapped = 0;
                    return "";
                }
                if(studentsSwapped < numStudentsToSwap-1) {
                    return manageCLIFromDiningSwap(input);
                }
                error = manageCLIFromDiningSwap(input);
                if(!error.equals("")) {
                    return error;
                }
                studentsSwapped = 0;
                fromOriginSwapCompleted = false;

                cardParams.setPlayerID(playerID)
                        .setFromOrigin(entranceStudents)
                        .setFromDestination(diningStudents);

                notifyCardParameters(cardParams);
                notifyCardActivation();

                entranceStudents = new StudentGroup();
                diningStudents = new StudentGroup();
                break;
            case "RETURN_TO_BAG":
                error = manageCLIStudentColor(input);
                if(!error.equals("")) {
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

    /**
     * Manages the input received by the user handling the number of students to swap.
     *
     * @param input Input received from Input Stream
     * @param validInputs List of Int that represent the valid input that the user can submit
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLINumStudentsToSwap(String input, List<Integer> validInputs) {
        String error = StringUtils.checkInteger(input, validInputs);
        if(!error.equals("")) {
            appendBuffer(error);
            return error;
        }
        numStudentsToSwap = Integer.parseInt(input);
        return "";
    }

    /**
     * Manages the input received by the user handling the students swap from the card.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLIFromCardSwap(String input) {
        String error;
        if (cardColorSelected == null) {
            error = manageCLICardStudentColorSwap(input);
            if (!error.equals("")) {
                return error;
            }
            cardStudents.addByColor(cardColorSelected, 1);
            studentsSwapped++;
            cardColorSelected = null;
            return "";
        }
        return "";
    }

    /**
     * Manages the input received by the user handling the students swap from the entrance.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLIFromEntranceSwap(String input) {
        String error;
        if (colorSelected == null) {
            error = manageCLIEntranceStudentColorSwap(input);
            if(!error.equals("")) {
                return error;
            }
            entranceStudents.addByColor(colorSelected, 1);
            studentsSwapped++;
            colorSelected = null;
            return "";
        }
        return "";
    }

    /**
     * Manages the input received by the user handling the students swap from the dining.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLIFromDiningSwap(String input) {
        String error;
        if (colorDiningSelected == null) {
            error = manageCLIDiningStudentColorSwap(input);
            if(!error.equals("")) {
                return error;
            }
            diningStudents.addByColor(colorDiningSelected, 1);
            studentsSwapped++;
            colorDiningSelected = null;
            return "";
        }
        return "";
    }

    /**
     * Manages the input received by the user handling the students color selection for the card.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
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

    /**
     * Manages the input received by the user handling the students color selection for the card in a swap.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    protected String manageCLICardStudentColorSwap(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
            switch(input.toUpperCase()) {
                case "B":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.BLUE) -
                            cardStudents.getByColor(Color.BLUE) > 0) {
                        cardColorSelected = Color.BLUE;
                    }
                    break;
                case "G":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.GREEN) -
                            cardStudents.getByColor(Color.GREEN) > 0) {
                        cardColorSelected = Color.GREEN;
                    }

                    break;
                case "P":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.PINK) -
                            cardStudents.getByColor(Color.PINK) > 0) {
                        cardColorSelected = Color.PINK;
                    }
                    break;
                case "R":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.RED) -
                            cardStudents.getByColor(Color.RED) > 0) {
                        cardColorSelected = Color.RED;
                    }
                    break;
                case "Y":
                    if(((StudentGroupDecorator) activeCard).getStudentsByColor(Color.YELLOW) -
                            cardStudents.getByColor(Color.YELLOW) > 0) {
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

    /**
     * Manages the input received by the user handling the students color selection for the entrance.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
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
                appendBuffer(error);
                return error;
            }
            return "";
        }
        error = "That was not a valid choice! Try again! - entrance student color";
        appendBuffer(error);
        return error;
    }

    /**
     * Manages the input received by the user handling the students color selection for the entrance in a swap.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLIEntranceStudentColorSwap(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
            Board board= game.getBoard();
            switch(input.toUpperCase()) {
                case "B":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.BLUE) -
                            entranceStudents.getByColor(Color.BLUE) > 0) {
                        colorSelected = Color.BLUE;
                    }
                    break;
                case "G":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.GREEN) -
                            entranceStudents.getByColor(Color.GREEN) > 0) {
                        colorSelected = Color.GREEN;
                    }
                    break;
                case "P":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.PINK) -
                            entranceStudents.getByColor(Color.PINK) > 0) {
                        colorSelected = Color.PINK;
                    }
                    break;
                case "R":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.RED) -
                            entranceStudents.getByColor(Color.RED) > 0) {
                        colorSelected = Color.RED;
                    }
                    break;
                case "Y":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInEntranceByColor(Color.YELLOW) -
                            entranceStudents.getByColor(Color.YELLOW) > 0) {
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

    /**
     * Manages the input received by the user handling the students color selection for the dining in a swap.
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
    private String manageCLIDiningStudentColorSwap(String input) {
        String error;

        if(validColors.contains(input.toUpperCase())) {
            Board board= game.getBoard();
            switch(input.toUpperCase()) {
                case "B":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.BLUE) -
                            diningStudents.getByColor(Color.BLUE) > 0) {
                        colorDiningSelected = Color.BLUE;
                    }
                    break;
                case "G":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.GREEN) -
                            diningStudents.getByColor(Color.GREEN) > 0) {
                        colorDiningSelected = Color.GREEN;
                    }

                    break;
                case "P":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.PINK) -
                            diningStudents.getByColor(Color.PINK) > 0) {
                        colorDiningSelected = Color.PINK;
                    }
                    break;
                case "R":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.RED) -
                            diningStudents.getByColor(Color.RED) > 0) {
                        colorDiningSelected = Color.RED;
                    }
                    break;
                case "Y":
                    if(board.getSchoolByPlayerID(playerID).getNumStudentsInDiningRoomByColor(Color.YELLOW) -
                            diningStudents.getByColor(Color.YELLOW) > 0) {
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

    /**
     * Manages the input received by the user handling the students color selection (Used for some type of cards).
     *
     * @param input Input received from Input Stream
     *
     * @return <code>String</code> that is the result of the action
     */
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

    /**
     * Notifies the parameters of a card.
     * @param cardParams <code>CardParameters</code> to be set.
     */
    private void notifyCardParameters(CardParameters cardParams) {
        notify(
                new RequestParameters()
                        .setCommandType(CommandType.SET_CARD_PARAMETERS)
                        .setIndex(boughtCardIndex)
                        .setCardParams(cardParams)
                        .serialize().toString()
        );
    }

    /**
     * Notifies the activation of a card.
     */
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
