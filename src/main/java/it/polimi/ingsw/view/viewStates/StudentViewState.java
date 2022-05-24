package it.polimi.ingsw.view.viewStates;

import it.polimi.ingsw.network.enumerations.CommandType;
import it.polimi.ingsw.network.parameters.RequestParameters;
import it.polimi.ingsw.utils.StringUtils;

public class StudentViewState extends ExpertViewState {
    private boolean isDestinationSelected = false;
    private boolean isDestinationIsland = false;
    public StudentViewState(ViewState oldViewState) {
        super(oldViewState);
    }
    @Override
    public String print(boolean... params) {
        return super.print(params);
    }

    @Override
    public void printCLIPrompt() {
        StringBuilder stringBuilder = new StringBuilder();
        isStudentsPhase = true;
        if(matchInfo.isExpertMode() && !choiceSelected && menuChoice != 2) {
            super.printCLIPrompt();
            return;
        }
        if (colorSelected == null) {
            stringBuilder.append("Select the student you want to move from your entrance: ");
            stringBuilder.append(printCLIStudentColorSelection());

            appendBuffer(stringBuilder.toString());
            return;
        }

        if (!isDestinationSelected) {
            appendBuffer("Choose the destination: [Dining room, Island]");
            return;
        }

        if (isDestinationIsland) {
            appendBuffer("Select the island on which you want to move your student: [index of island]");
        }

    }

    @Override
    public String manageCLIInput (String input) {
        String error;

        if(matchInfo.isExpertMode() && !choiceSelected && menuChoice != 2)
            return super.manageCLIInput(input);

        appendBuffer(input);
        if(colorSelected == null) {
            return manageCLIEntranceStudentColor(input);
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
                isMovingStudents = false;
                return "";
            }
            if(input.equalsIgnoreCase("Island")) {
                isDestinationIsland = true;
                return "";
            }
            error = "That was not a valid choice! Try again!";
            appendBuffer(error);
            return error;
        }

        if(isDestinationIsland) {
            error = StringUtils.checkInteger(input, validIslandIndexes);
            if (!error.equals("")) {
                appendBuffer(error);
                return error;
            }
            int islandIndex = game.getBoard().getOriginalIndexOf(Integer.parseInt(input));
            notify(
                    new RequestParameters()
                            .setCommandType(CommandType.TRANSFER_STUDENT_TO_ISLAND)
                            .setIndex(islandIndex)
                            .setColor(colorSelected)
                            .serialize().toString()
            );
            setInteractionComplete(true);
            isMovingStudents = false;
            return "";
        }
        return "";
    }

    @Override
    public void resetInteraction() {
        super.resetInteraction();
        colorSelected = null;
        isDestinationSelected = false;
        isDestinationIsland = false;
    }
}
