package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.*;
import it.polimi.ingsw.controller.subcontrollers.*;
import it.polimi.ingsw.exceptions.EriantysException;
import it.polimi.ingsw.exceptions.EriantysRuntimeException;
import it.polimi.ingsw.exceptions.game.GameNotStartedException;
import it.polimi.ingsw.exceptions.game.GamePausedException;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.exceptions.game.NotCurrentPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.network.commands.Command;

import java.util.*;
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
    private final int TIME_OUT = 1; // Game timeout in minutes
    private RoundStateController roundStateController;
    private final MatchInfo matchInfo;
    private final Game game;
    private final Lobby lobby;
    private boolean paused;
    private Timer timer;

    /**
     * Sole constructor for <code>GameController</code>.
     */
    public GameController() {
        matchInfo = MatchInfo.getInstance();
        game = Game.getInstance();
        lobby = Lobby.getLobby();
        paused = false;
    }

    /**
     * Requests the specified command during this game. The request is automatically filtered if the <code>Player</code>
     * is not the current <code>Player</code>.
     *
     * @param playerID the ID of the <code>Player</code> requesting a command.
     */
    public synchronized void requestCommand(int playerID, Command command) throws EriantysException, EriantysRuntimeException {
        if(!matchInfo.getGameStatus().equals(GameStatus.IN_GAME)){
            throw new GameNotStartedException();
        }
        if(playerID != matchInfo.getCurrentPlayerID()){
            throw new NotCurrentPlayerException(playerID);
        }
        if(paused) {
            throw new GamePausedException();
        }
        command.execute();
        nextState();
    }

    public void tryCreatingGame() {
        if(Lobby.getLobby().checkLobbyIsReady()) {
            createGame();
        }
    }

    /**
     * Creates a new Game with the players in the lobby.
     */
    protected void createGame() {
        for(Player player : lobby.getPlayers()) {
            game.addPlayer(player);
            game.addSchool(player.getID());
            game.giveStudentsTo(player.getID(), matchInfo.getInitialNumStudents());
            game.addTowersTo(player.getID(), matchInfo.getMaxTowers());
        }

        game.placeMNAt(new Random().nextInt(game.getBoard().getNumIslands()));

        StudentBag islandBag = new StudentBag(2);
        int MNPosition = game.getBoard().getMNPosition();
        for(int i = 0; i < 12; i++){
            if(i != MNPosition && i != (MNPosition + 6) % game.getBoard().getNumIslands())
                game.addInitialStudentToIsland(i, islandBag.drawStudents(1));
        }

        game.createClouds(game.getPlayers().size());

        IslandController islandController;
        DiningRoomController diningRoomController;

        if(matchInfo.isExpertMode()) {
            game.giveInitialCoin();
            islandController = new IslandExpertController(new CharacterCardController());
            diningRoomController = new DiningRoomExpertController();

            List<Integer> cardsID = new ArrayList<>();
            Random r = new Random();
            for(int i = 0; i < 3; i++){
                int cardID = r.nextInt(CharacterCards.values().length);
                while(cardsID.contains(cardID))
                    cardID = r.nextInt((CharacterCards.values().length));
                game.instantiateCharacterCard(cardID);
                cardsID.add(cardID);
            }
        }else {
            islandController = new IslandController();
            diningRoomController = new DiningRoomController();
        }

        roundStateController = new RoundStateController(islandController, diningRoomController);
        setState(new PlanningStateController(roundStateController));

        roundStateController.defineFirstPlayOrder();
        matchInfo.setStateType(TurnState.PLANNING);
        matchInfo.setGameStatus(GameStatus.IN_GAME);
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
                if(matchInfo.hasMNMoved()) {
                    checkEndConditionAfterMN();
                    setState(new CloudStateController(roundStateController));
                    matchInfo.setMNMoved(false);
                }
                break;
            case CLOUD:
                matchInfo.removePlayer();

                matchInfo.resetNumMovedStudents();
                if(matchInfo.getNumPlayersStillToAct() == 0) {
                    roundStateController.definePlayOrder();

                    checkEndConditionAfterRound();
                    Game.getInstance().resetCards();

                    setState(new PlanningStateController(roundStateController));
                } else {
                    setState(new StudentsStateController(roundStateController));
                }
                roundStateController.clearEffects();
                break;
        }
        matchInfo.notifyMatchInfo();
    }

    /**
     * Sets the new state for the current round.
     *
     * @param newState the new <code>RoundStateController</code>.
     */
    protected void setState(RoundStateController newState) {
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
        if(everybodyHasCards() && !game.isStudentBagEmpty()) return;

        checkWinner();
    }

    private boolean everybodyHasCards() {
        for(Player p: game.getPlayers()) {
            if(p.getPlayableCards().isEmpty())
                return false;
        }

        return true;
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

            if(owner != null && owner.getTeamColor() == teamColor) numProfessors++;
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

    public synchronized void handleReconnection(int playerID) {
        game.reconnectPlayer(playerID);
        matchInfo.setNumPlayersConnected(matchInfo.getNumPlayersConnected() + 1);

        if(timer != null) {
            // Cancel running timer
            timer.cancel();
            timer.purge();

            timer = null;
            paused = false;
        } else {
            requestTimeout();
        }
    }

    public synchronized void handleDisconnection(int playerID) {
        game.disconnectPlayer(playerID);
        matchInfo.setNumPlayersConnected(matchInfo.getNumPlayersConnected() - 1);

        requestTimeout();
    }

    private void requestTimeout() {
        if(matchInfo.getNumPlayersConnected() == 1) {
            paused = true;

            // Start timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    forceDeclareWinner();
                }
            }, TIME_OUT * 60 * 1000); //Conversion to milliseconds
        }
    }

    private void forceDeclareWinner() {
        Optional<Player> lastPlayer = game.getPlayers().stream()
                .filter(Player::isConnected)
                .findAny();

        lastPlayer.ifPresent(player -> declareWinner(player.getTeamColor()));
    }

    public RoundStateController getRoundStateController() {
        return roundStateController;
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public void loadSavedState() {
        if(!matchInfo.getGameStatus().equals(GameStatus.IN_GAME))
            return;

        IslandController islandController;
        DiningRoomController diningRoomController;

        if(matchInfo.isExpertMode()) {
            islandController = new IslandExpertController(new CharacterCardController());
            diningRoomController = new DiningRoomExpertController();
        } else {
            islandController = new IslandController();
            diningRoomController = new DiningRoomController();
        }

        roundStateController = new RoundStateController(islandController, diningRoomController);

        switch(matchInfo.getStateType()) {
            case PLANNING:
                roundStateController = new PlanningStateController(roundStateController);
                break;
            case STUDENTS:
                roundStateController = new StudentsStateController(roundStateController);
                break;
            case MOTHER_NATURE:
                roundStateController = new MNStateController(roundStateController);
                break;
            case CLOUD:
                roundStateController = new CloudStateController(roundStateController);
                break;
            default:
                System.err.println("Unable to load round information, going into planning state");
                roundStateController = new PlanningStateController(roundStateController);
                break;
        }
    }
}
