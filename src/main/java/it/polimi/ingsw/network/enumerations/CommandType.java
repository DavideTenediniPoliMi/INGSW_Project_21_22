package it.polimi.ingsw.network.enumerations;

/**
 * Enum class representing the different types of messages. Each instance has a <code>boolean</code> representing
 * if the <code>Command</code> has to be executed while in-game.
 */
public enum CommandType {
    HANDSHAKE(false, true),
    CREATE_LOBBY(false, true),
    PLAY_CARD(true, false),
    BUY_CHARACTER_CARD(true, false),
    SET_CARD_PARAMETERS(true, false),
    ACTIVATE_CARD(true, false),
    TRANSFER_STUDENT_TO_ISLAND(true, false),
    TRANSFER_STUDENT_TO_DINING_ROOM(true, false),
    MOVE_MN(true, false),
    COLLECT_FROM_CLOUD(true, false),
    SKIP_TURN(true, false),
    JOIN(false, false),
    SEL_CARDBACK(false, false),
    SEL_TOWERCOLOR(false, false),
    READY_UP(false, false),
    DISCONNECT(true, true),
    RECONNECT(true, true);

    CommandType(boolean isInGame, boolean isUrgent) {
        this.isInGame = isInGame;
        this.isUrgent = isUrgent;
    }

    public final boolean isInGame;
    public final boolean isUrgent;
}
