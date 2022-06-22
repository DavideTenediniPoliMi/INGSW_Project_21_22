package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.utils.Printable;
import it.polimi.ingsw.view.cli.AnsiCodes;
import javafx.scene.image.Image;

/**
 * Class to hold the different colors for towers in the game.
 */
public enum TowerColor implements Printable<String> {
    BLACK("/images/black_tower.png"),
    WHITE("/images/white_tower.png"),
    GREY("/images/grey_tower.png");

    TowerColor(String path) {
        this.path = path;
    }

    private final String path;

    /**
     * Returns an image corresponding to the tower color
     *
     * @return an <code>Image</code> corresponding to the tower color
     */
    public Image getImage() {
        return new Image(path);
    }

    @Override
    public String print(boolean...params) {
        return AnsiCodes.getTeamColor(this) + " T " + AnsiCodes.RESET;
    }
}
