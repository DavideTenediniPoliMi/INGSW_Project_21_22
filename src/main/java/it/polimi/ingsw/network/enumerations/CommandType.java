package it.polimi.ingsw.network.enumerations;

/**
 * Enum class representing the different types of messages while a game is being played.
 */
public enum CommandType {
    PLAY_CARD(true),
    BUY_CHARACTER_CARD(true),
    SET_CARD_PARAMETERS(true),
    ACTIVATE_CARD(true),
    TRANSFER_STUDENT_TO_ISLAND(true),
    TRANSFER_STUDENT_TO_DINING_ROOM(true),
    MOVE_MN(true),
    COLLECT_FROM_CLOUD(true),
    JOIN(false),
    SEL_CARDBACK(false),
    SEL_TOWERCOLOR(false),
    READY_UP(false),
    DISCONNECT(true);

    CommandType(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public final boolean isInGame;
}
