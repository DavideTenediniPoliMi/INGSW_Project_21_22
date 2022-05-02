package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.model.enumerations.CardBack;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.RequestParameters;

/**
 * Class implementing the factory pattern for <code>Command</code> classes.
 */
public class CommandFactory {

    private final GameController gameController;
    private final LobbyController lobbyController;
    private final int playerID;

    /**
     * Sole constructor for <code>CommandFactory</code>.
     *
     * @param playerID the ID of the <code>Player</code> bound to this <code>CommandFactory</code>.
     * @param lobbyController the <code>LobbyController</code> instance for this game.
     * @param gameController the <code>GameController</code> instance for this game.
     */
    public CommandFactory(int playerID, LobbyController lobbyController, GameController gameController) {
        this.gameController = gameController;
        this.lobbyController = lobbyController;
        this.playerID = playerID;
    }

    /**
     * Creates a <code>Command</code> from the specified <code>RequestParameters</code>.
     *
     * @param params the <code>RequestParameters</code> from a received message.
     * @return a <code>Command</code> instance for the specified parameters.
     * @throws BadParametersException if any parameter for the requested <code>Command</code> is invalid.
     */
    public Command createCommand(RequestParameters params) throws BadParametersException {
        /*
         * General nullcheck before creating a new Command
         */
        if(params == null) {
            throw new BadParametersException("RequestParamers is null");
        }else if(lobbyController == null) {
            throw new BadParametersException("LobbyController is null");
        }else if(gameController == null) {
            throw new BadParametersException("GameController is null");
        }else if(playerID < 0) {
            throw new BadParametersException("Player ID is invalid (" + playerID + ")");
        }

        switch(params.getCommandType()) {
            /*
             * Lobby-related commands
             */
            case JOIN:
                /* Params needed:
                 *  - PlayerID
                 *  - Name
                 */
                String name = params.getName();
                if(name != null && name.length() > 0) {
                    return new LobbyJoinCommand(playerID, name, lobbyController);
                }
                throw new BadParametersException("JOIN command - Given params:" +
                        " (playerID: " + playerID + ", Name: " + name + ")");

            case SEL_TOWERCOLOR:
                /* Params needed:
                 *  - PlayerID
                 *  - TowerColor
                 */
                TowerColor team = params.getTowerColor();
                if(team != null) {
                    return new LobbySelectTeamCommand(playerID, team, lobbyController);
                }
                throw new BadParametersException("SELECT TEAM command - Given params:" +
                        " (playerID: " + playerID + ", Team: " + team + ")");

            case SEL_CARDBACK:
                /* Params needed:
                 *  - PlayerID
                 *  - CardBack
                 */
                CardBack cardBack = params.getCardBack();
                if(cardBack != null) {
                    return new LobbySelectCBCommand(playerID, cardBack, lobbyController);
                }
                throw new BadParametersException("SELECT CARDBACK command - Given params:" +
                        " (playerID: " + playerID + ", CardBack: " + cardBack + ")");

            case READY_UP:
                /* Params needed:
                 *  - PlayerID
                 *  - Ready
                 */
                boolean ready = params.isReady();
                return new LobbyReadyStatusCommand(playerID, ready, lobbyController);

            /*
             * Game-related commands
             */
            case PLAY_CARD:
                /* Params needed:
                 *  - CardIndex
                 */
                int cardIndex = params.getIndex();
                if(cardIndex >= 0) {
                    return new PlayCardCommand(cardIndex, gameController);
                }
                throw new BadParametersException("PLAY CARD command - Given params:" +
                        " (cardIndex: " + cardIndex +")");
            case TRANSFER_STUDENT_TO_ISLAND:
                /* Params needed:
                 *  - IslandIndex
                 *  - Color
                 */
                int islandIndex = params.getIndex();
                Color color = params.getColor();
                if(islandIndex >= 0 && color != null) {
                    return new TransferToIslandCommand(islandIndex, color, gameController);
                }
                throw new BadParametersException("TRANSFER TO ISLAND command - Given params:" +
                        " (islandIndex: " + islandIndex + ", color:" + color + ")");
            case TRANSFER_STUDENT_TO_DINING_ROOM:
                /* Params needed:
                 *  - Color
                 */
                color = params.getColor();
                if(color != null) {
                    return new TransferToDiningCommand(color, gameController);
                }
                throw new BadParametersException("TRANSFER TO DINING command - Given params:" +
                        " (color: null)");
            case MOVE_MN:
                /* Params needed:
                 *  - DestIndex
                 */
                int destIndex = params.getIndex();
                if(destIndex >= 0) {
                    return new MoveMNCommand(destIndex, gameController);
                }
                throw new BadParametersException("MOVE MN command - Given params:" +
                        " (destIndex: " + destIndex + ")");
            case BUY_CHARACTER_CARD:
                /* Params needed:
                 *  - CardIndex
                 */
                cardIndex = params.getIndex();
                if(cardIndex >= 0) {
                    return new BuyCharacterCardCommand(cardIndex, gameController);
                }
                throw new BadParametersException("BUY CHARACTER CARD command - Given params:" +
                        " (cardIndex: " + cardIndex + ")");
            case SET_CARD_PARAMETERS:
                /* Params needed:
                 *  - CardIndex
                 *  - CardParameters
                 */
                cardIndex = params.getIndex();
                CardParameters cardParams = params.getCardParams();
                if(cardIndex >= 0 && cardParams != null) {
                    return new SetCardParametersCommand(cardIndex, cardParams, gameController);
                }
                throw new BadParametersException("SET PARAMETERS command - Given params:" +
                        " (cardIndex: " + cardIndex + ", cardParams:" + cardParams + ")");
            case ACTIVATE_CARD:
                return new ActivateCharacterCardCommand(gameController);
            case COLLECT_FROM_CLOUD:
                /* Params needed:
                 *  - CloudIndex
                 */
                int cloudIndex = params.getIndex();
                if(cloudIndex >= 0) {
                    return new CollectCloudCommand(cloudIndex, gameController);
                }
                throw new BadParametersException("COLLECT CLOUD command - Given params:" +
                        " (cloudIndex: " + cloudIndex + ")");
            case DISCONNECT:
                return null;
            default:
                throw new BadParametersException("No such action: " + params.getCommandType());
        }
    }
}
