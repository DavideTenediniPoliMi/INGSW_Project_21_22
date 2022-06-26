package it.polimi.ingsw.view.gui;

/**
 * Enum class representing the states in which the GUI can be. To each state corresponds a different response from the
 * graphic interface.
 */
public enum GUIState {
    DISCONNECTION,
    REPEAT_ACTION,
    WAIT_RESPONSE,
    WAIT_ACTION,
    END_GAME,
    SET_CARD_PARAMS,
    PLANNING,
    MOVE_STUDENT,
    MOVE_MN,
    CLOUD,
    PAUSE
}
