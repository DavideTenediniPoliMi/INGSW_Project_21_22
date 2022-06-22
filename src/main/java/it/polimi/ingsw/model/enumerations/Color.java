package it.polimi.ingsw.model.enumerations;

import javafx.scene.image.Image;

/**
 * Class to hold the different student colors available in the game.
 */
public enum Color {
    BLUE("/images/student_blue.png"),
    GREEN("/images/student_green.png"),
    PINK("/images/student_pink.png"),
    RED("/images/student_red.png"),
    YELLOW("/images/student_yellow.png");

    public static final int NUM_COLORS = 5;

    Color(String pathStud) {
        this.pathStud = pathStud;
    }
    private final String pathStud;

    /**
     * Returns an image corresponding to the student color
     *
     * @return an <code>Image</code> corresponding to the student color
     */
    public Image getImage() {
        return new Image(pathStud);
    }
}
