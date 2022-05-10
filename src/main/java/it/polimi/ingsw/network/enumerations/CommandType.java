package it.polimi.ingsw.network.enumerations;

/**
 * Enum class representing the different types of messages. Each instance has a <code>boolean</code> representing
 * if the <code>Command</code> has to be executed while in-game.
 */
public enum CommandType {
    HANDSHAKE(false),
    CREATE_GAME(false),
    PLAY_CARD(true),
    BUY_CHARACTER_CARD(true),
    SET_CARD_PARAMETERS(true),
    ACTIVATE_CARD(true),
    TRANSFER_STUDENT_TO_ISLAND(true),
    TRANSFER_STUDENT_TO_DINING_ROOM(true),
    MOVE_MN(true),
    COLLECT_FROM_CLOUD(true),
    SKIP_TURN(true),
    JOIN(false),
    SEL_CARDBACK(false),
    SEL_TOWERCOLOR(false),
    READY_UP(false),
    DISCONNECT(true),
    RECONNECT(true);

    CommandType(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public final boolean isInGame;
}
