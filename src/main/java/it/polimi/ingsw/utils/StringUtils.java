package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static List<String> insertAfter(List<String> before, List<String> after) {
        return new ArrayList<>();
    }

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
