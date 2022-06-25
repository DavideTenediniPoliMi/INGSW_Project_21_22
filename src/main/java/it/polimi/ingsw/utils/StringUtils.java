package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains various methods that help the CLI to build strings.
 */
public class StringUtils {
    /**
     * Concatenates the strings in the list <code>before</code> to the ones in the list <code>after</code>.
     * <code>Starting</code> indicated the row of before that should start concatenating with the first row of after.
     * <code>Spaces</code> indicates the number of white spaces that there will be in between the two blocks.
     * Every line in before should have the same length, as every row in after should.
     *
     * @param before the list of strings that should be on the left.
     * @param after the list of strings to concatenate to the right.
     * @param starting the number of rows of offset from the 2 lists.
     * @param spaces the number of white spaces.
     * @return the newly constructed list of concatenated strings.
     */
    public static List<String> insertAfter(List<String> before, List<String> after, int starting, int spaces) {
        if(after == null || after.size() == 0) {
            return insertSpacesAfter(before, spaces);
        }

        //ALWAYS ASSUME THAT EVERY ROW OF BEFORE HAS THE SAME LENGTH (SAME FOR AFTER)
        List<String> result = new ArrayList<>();
        int lengthBefore = before.get(0).length();

        for(int i = 0; i < before.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder(before.get(i));

            stringBuilder.append(" ".repeat(spaces));

            if(i < starting || i >= starting + after.size()) {
                stringBuilder.append(" ".repeat(after.get(0).length()));
            } else {
                stringBuilder.append(after.get(i - starting));
            }

            result.add(stringBuilder.toString());
        }

        if(starting + after.size() > before.size()) {
            for (int i = before.size(); i < starting + after.size(); i++) {
                result.add(" ".repeat(lengthBefore + spaces) + after.get(i - starting));
            }
        }

        return result;
    }

    /**
     * Inserts a certain number of white spaces after every string of the list.
     *
     * @param before the list of strings
     * @param spaces the number of white spaces.
     * @return the list with the modified strings.
     */
    public static List<String> insertSpacesAfter(List<String> before, int spaces) {
        List<String> result = new ArrayList<>();

        for(String s : before) {
            result.add(s + " ".repeat(spaces));
        }

        return result;
    }

    /**
     * Transforms a list of strings in a single String with new line characters in between each String.
     *
     * @param list the list of Strings.
     * @return the string of the concatenated strings.
     */
    public static String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String s: list) {
            stringBuilder.append(s).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Checks if a string is a number, and if it is checks if that number is inside a set of valid Inputs.
     *
     * @param input the string
     * @param validInputs the set of valid integer inputs
     * @return true if the input satisfies the number and valid input constraints, false otherwise.
     */
    public static String checkInteger(String input, List<Integer> validInputs) {
        try {
            int temp = Integer.parseInt(input);
            if(!validInputs.contains(temp)) {
                return "That is a number, but not a valid one! Try again!";
            }
            return "";
        } catch(NumberFormatException e) {
            return "That is not a number! Try again!";
        }
    }
}
