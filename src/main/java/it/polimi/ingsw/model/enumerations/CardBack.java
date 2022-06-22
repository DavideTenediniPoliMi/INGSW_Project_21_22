package it.polimi.ingsw.model.enumerations;

import javafx.scene.image.Image;

/**
 * Class representing the 4 different types of card backs
 */
public enum CardBack {
    WIZARD1("/images/MAGO_1.jpg"),
    WIZARD2("/images/MAGO_2.jpg"),
    WIZARD3("/images/MAGO_3.jpg"),
    WIZARD4("/images/MAGO_4.jpg");

    CardBack(String path) {
        this.path = path;
    }

    private final String path;

    /**
     * Returns an image corresponding to the card back
     *
     * @return an <code>Image</code> corresponding to the card back
     */
    public Image getImage() {
        return new Image(path);
    }
}
