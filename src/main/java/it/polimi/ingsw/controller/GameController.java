package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.round.*;
import it.polimi.ingsw.exceptions.game.IllegalActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.board.School;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {
    private RoundStateController roundStateController;
    private final MatchInfo matchInfo;

    public GameController() {
        matchInfo = MatchInfo.getInstance();
    }

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

    private void setState(RoundStateController newState) {
        roundStateController = newState;
    }

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

    private void checkEndConditionAfterRound() {
        Game game = Game.getInstance();

        // END CONDITION 3 : PLAYERS HAVE NO MORE ASSISTANT CARDS
        // END CONDITION 4 : THERE ARE NO MORE STUDENTS TO DRAW FROM THE BAG
        if(!game.getPlayerByID(0).getPlayableCards().isEmpty() && !game.isStudentBagEmpty()) return;

        checkWinner();
    }

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

    private void declareWinner(TowerColor teamColor) {
        // TODO
    }

    private void declareTie(List<TowerColor> teamColors) {
        // TODO
    }
}
