package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static List<String> insertAfter(List<String> before, List<String> after, int starting, int spaces) {
        if(after == null) {
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

    public static List<String> insertSpacesAfter(List<String> before, int spaces) {
        List<String> result = new ArrayList<>();

        for(int i = 0; i < before.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder(before.get(i));

            stringBuilder.append(" ".repeat(spaces));

            result.add(stringBuilder.toString());
        }

        return result;
    }

    public static String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String s: list) {
            stringBuilder.append(s + "\n");
        }

        return stringBuilder.toString();
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
