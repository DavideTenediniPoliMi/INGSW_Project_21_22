package it.polimi.ingsw.model.enumerations;

import javafx.scene.image.Image;

/**
 * Class to hold the different student colors available in the game.
 */
public enum Color {
    BLUE("/images/teacher_blue.png","/images/student_blue.png"),
    GREEN("/images/teacher_green.png","/images/student_green.png"),
    PINK("/images/teacher_pink.png","/images/student_pink.png"),
    RED("/images/teacher_red.png","/images/student_red.png"),
    YELLOW("/images/teacher_yellow.png","/images/student_yellow.png");

    public static final int NUM_COLORS = 5;

    Color(String pathProf, String pathStud) {
        this.pathProf = pathProf;
        this.pathStud = pathStud;
    }

    private final String pathProf;
    private final String pathStud;

    public Image getProfImage() {
        return new Image(pathProf);
    }

    public Image getStudImage() {
        return new Image(pathStud);
    }
}
