package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.*;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.helpers.StudentBag;

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
         **/
        for(Player player : game.getPlayers()) {
            game.getBoard().addSchool(player);
            if(matchInfo.isExpertMode()) {
                game.giveCoinToPlayer(player.getID());
            }
        }

        StudentBag islandBag = new StudentBag(10);
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
            if(!school.getOwner().isTowerHolder()) return;

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

            if(currentNumProfessors < maxNumProfessors) {
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
