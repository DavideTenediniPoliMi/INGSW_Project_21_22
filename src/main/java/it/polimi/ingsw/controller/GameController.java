package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.*;
import it.polimi.ingsw.controller.subcontrollers.CharacterCardController;
import it.polimi.ingsw.controller.subcontrollers.DiningRoomExpertController;
import it.polimi.ingsw.controller.subcontrollers.IslandExpertController;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.exceptions.game.BadParametersException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.game.NotCurrentPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.network.parameters.CardParameters;
import it.polimi.ingsw.network.parameters.ActionRequestParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class representing the main controller. Handles:
 * <ul>
 *     <li>Lobby and Game creation</li>
 *     <li>Round state management</li>
 *     <li>End-Game checks</li>
 *     <li>Winner declaration and game ending</li>
 * </ul>
 */
public class GameController {
    private RoundStateController roundStateController;
    private GameStatus status;
    private final MatchInfo matchInfo;
    private final Game game;

    /**
     * Sole constructor for <code>GameController</code>.
     */
    public GameController() {
        matchInfo = MatchInfo.getInstance();
        game = Game.getInstance();
        status = GameStatus.LOBBY;
    }

    /**
     * Requests the specified action during this game. The request is automatically filtered if the <code>Player</code>
     * is not the current <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code> requesting an action.
     * @param requestParams the parameters of the request.
     */
    public synchronized void requestAction(int playerID, ActionRequestParameters requestParams) throws EriantysException {
        if(playerID != matchInfo.getCurrentPlayerID()){
            throw new NotCurrentPlayerException(playerID);
        }
        forwardRequest(requestParams);
    }

    /**
     * Forwards the request further in the <code>RoundStateController</code>.
     *
     * @param requestParams the parameters of the requested action.
     */
    private void forwardRequest(ActionRequestParameters requestParams) throws EriantysException, EriantysRuntimeException {
        int cardIndex, islandIndex, numSteps, cloudIndex;
        Color c;
        CardParameters cardParams;
        switch (requestParams.getActionType()){
            case PLAY_CARD:
                /*
                 * index: The index of the Assistant Card to play
                 */
                cardIndex = requestParams.getIndex();
                roundStateController.playCard(cardIndex);
                break;
            case TRANSFER_STUDENT_TO_ISLAND:
                /*
                 * index: The index of the recipient Island
                 * color: The color of the student to move
                 */
                islandIndex = requestParams.getIndex();
                c = requestParams.getColor();
                roundStateController.transferStudentToIsland(islandIndex, c);
                break;
            case TRANSFER_STUDENT_TO_DINING_ROOM:
                /*
                 * color: The color of the student to move
                 */
                c = requestParams.getColor();
                roundStateController.transferStudentToDiningRoom(c);
                break;
            case MOVE_MN:
                /*
                 * index: Number of steps of MN movement
                 */
                numSteps = requestParams.getIndex();
                roundStateController.moveMN(numSteps);
                break;
            case COLLECT_FROM_CLOUD:
                /*
                 * index: Index of the cloud to collect from
                 */
                cloudIndex = requestParams.getIndex();
                roundStateController.collectFromCloud(cloudIndex);
                break;
            case BUY_CHARACTER_CARD:
                /*
                 * index: Index of the CharacterCard to buy
                 */
                cardIndex = requestParams.getIndex();
                roundStateController.buyCharacterCard(cardIndex);
                break;
            case SET_CARD_PARAMETERS:
                /*
                 * index: Index of the CharacterCard to set parameters to (Validation)
                 * Parameters: CardParameters object containing the parameters to set
                 */
                cardIndex = requestParams.getIndex();
                cardParams = requestParams.getCardParams();
                if(game.getCharacterCards().indexOf(game.getActiveCharacterCard()) != cardIndex){
                    throw new BadParametersException("Given ID does not match the active CharacterCard's ID");
                }
                roundStateController.setCardParameters(cardParams);
                break;
            case ACTIVATE_CARD:
                roundStateController.activateCard();
                break;
            default:
                throw new BadParametersException("No such action: " + requestParams.getActionType());
        }
        nextState();
    }

    /**
     * Creates a new Game with the players in the lobby.
     */
    private void createGame() {
        /*
         * Game, Board and Islands are already instantiated
         * (1) Instantiate Schools
         * (2) Bind schools
         * (3) Add 10 students to Islands
         * (4) Instantiate Clouds
         * (5e) Instantiate CharacterCards
         * (6e) Give out coins
         **/for(Player player : game.getPlayers()) {
            game.addSchool(player.getID());
            game.giveStudentsTo(player.getID(), matchInfo.getInitialNumStudents());
            game.addTowersTo(player.getID(), matchInfo.getMaxTowers());
            if(matchInfo.isExpertMode()) {
                game.giveCoinToPlayer(player.getID());
            }
        }

        StudentBag islandBag = new StudentBag(2);
        for(int i = 0; i < 10; i++){
            game.addInitialStudentToIsland(i, islandBag.drawStudents(1));
        }

        game.createClouds(game.getPlayers().size());

        if(matchInfo.isExpertMode()) {
            Random r = new Random();
            for(int i = 0; i < 3; i++){
                int cardID = r.nextInt(CharacterCards.values().length);
                game.instantiateCharacterCard(cardID);
            }
        }

        game.placeMNAt(new Random().nextInt(game.getBoard().getNumIslands()));

        matchInfo.setStateType(TurnState.PLANNING);

        roundStateController = new RoundStateController(new IslandExpertController(new CharacterCardController())
                , new DiningRoomExpertController());
        setState(new PlanningStateController(roundStateController));

        roundStateController.defineFirstPlayOrder();
    }

    /**
     * Transitions the current round into its next state.
     *
     * @throws IllegalActionException If trying to define the new order of play in a wrong state.
     */
    private void nextState() throws IllegalActionException {
        switch(matchInfo.getStateType()) {
            case PLANNING:
                matchInfo.removePlayer();

                if(matchInfo.getNumPlayersStillToAct() == 0) {
                    roundStateController.definePlayOrder();
                    setState(new StudentsStateController(roundStateController));
                }
                break;
            case STUDENTS:
                if(matchInfo.getNumMovedStudents() == matchInfo.getMaxMovableStudents()) {
                    setState(new MNStateController(roundStateController));
                }
                break;
            case MOTHER_NATURE:
                checkEndConditionAfterMN();
                setState(new CloudStateController(roundStateController));
                break;
            case CLOUD:
                matchInfo.removePlayer();

                if(matchInfo.getNumPlayersStillToAct() == 0) {
                    roundStateController.definePlayOrder();

                    checkEndConditionAfterRound();
                    Game.getInstance().resetCards();
                    roundStateController.clearEffects();

                    setState(new PlanningStateController(roundStateController));
                } else {
                    setState(new StudentsStateController(roundStateController));
                }
                break;
        }
    }

    /**
     * Sets the new state for the current round.
     *
     * @param newState the new <code>RoundStateController</code>.
     */
    private void setState(RoundStateController newState) {
        roundStateController = newState;
    }

    /**
     * Checks end-game conditions that apply after Mother Nature movement.
     */
    private void checkEndConditionAfterMN() {
        Game game = Game.getInstance();
        List<School> schools = game.getBoard().getSchools();

        // END CONDITION 1 : A PLAYER BUILDS ALL OF THEIR TOWERS
        for(School school: schools) {
            if(school.getNumTowers() == 0 && school.getOwner().isTowerHolder()) {
                declareWinner(school.getOwner().getTeamColor());
                return;
            }
        }

        // END CONDITION 2 : ONLY 3 GROUP OF ISLANDS REMAIN
        if(game.getBoard().getNumIslands() > 3) return;

        checkWinner();
    }

    /**
     * Checks end-game conditions that apply after the end of a round.
     */
    private void checkEndConditionAfterRound() {
        Game game = Game.getInstance();

        // END CONDITION 3 : PLAYERS HAVE NO MORE ASSISTANT CARDS
        // END CONDITION 4 : THERE ARE NO MORE STUDENTS TO DRAW FROM THE BAG
        if(!game.getPlayerByID(0).getPlayableCards().isEmpty() && !game.isStudentBagEmpty()) return;

        checkWinner();
    }

    /**
     * Defines the game winner.
     */
    private void checkWinner() {
        List<School> schools = Game.getInstance().getBoard().getSchools();

        List<School> schoolsWithLessTowers = new ArrayList<>();
        int minNumTowers = matchInfo.getMaxTowers();

        for(School school: schools) {
            if(!school.getOwner().isTowerHolder()) continue;

            int currentNumTowers = school.getNumTowers();

            if(currentNumTowers < minNumTowers) {
                minNumTowers = currentNumTowers;
                schoolsWithLessTowers.clear();
            }

            if(currentNumTowers == minNumTowers) {
                schoolsWithLessTowers.add(school);
            }
        }

        if(schoolsWithLessTowers.size() == 1) {
            declareWinner(schoolsWithLessTowers.get(0).getOwner().getTeamColor());
            return;
        }

        List<TowerColor> teamColorsTying = schoolsWithLessTowers.stream()
                .map((school -> school.getOwner().getTeamColor()))
                .collect(Collectors.toList());
        List<TowerColor> teamColorsWithMoreProfessors = new ArrayList<>();
        int maxNumProfessors = 0;

        for(TowerColor teamColor: teamColorsTying) {
            int currentNumProfessors = getNumProfessorsOf(teamColor);

            if(currentNumProfessors > maxNumProfessors) {
                maxNumProfessors = currentNumProfessors;
                teamColorsWithMoreProfessors.clear();
            }

            if(currentNumProfessors == maxNumProfessors) {
                teamColorsWithMoreProfessors.add(teamColor);
            }
        }

        if(teamColorsWithMoreProfessors.size() > 1) {
            declareTie(teamColorsWithMoreProfessors);
            return;
        }

        declareWinner(teamColorsWithMoreProfessors.get(0));
    }

    /**
     * Returns the amount of professors that the specified <code>TowerColor</code> has.
     *
     * @param teamColor the color of the team.
     * @return The amount of professors.
     */
    private int getNumProfessorsOf(TowerColor teamColor) {
        Game game = Game.getInstance();
        ProfessorTracker professorOwners = game.getBoard().getProfessorOwners();
        int numProfessors = 0;

        for(Color c: Color.values()) {
            int ownerID = professorOwners.getOwnerIDByColor(c);
            Player owner = game.getPlayerByID(ownerID);

            if(owner.getTeamColor() == teamColor) numProfessors++;
        }

        return numProfessors;
    }

    /**
     * Sets the specified <code>TowerColor</code> as the winner.
     *
     * @param teamColor the winner of the game.
     */
    private void declareWinner(TowerColor teamColor) {
        matchInfo.declareWinner(teamColor);
    }

    /**
     * Declares a tie between the specified <code>TowerColor</code>s.
     *
     * @param teamColors the list of teams that tied.
     */
    private void declareTie(List<TowerColor> teamColors) {
        matchInfo.declareTie(teamColors);
    }
}
