package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.List;
import java.util.Optional;

/**
 * Subcontroller to handle all actions relative to:
 * <ul>
 *     <li>Moving Mother Nature</li>
 *     <li>Influence calc</li>
 *     <li>Conquering Islands</li>
 *     <li>Merging Islands</li>
 * </ul>
 */
public class IslandController {
    /**
     * Sole constructor
     */
    public IslandController() {
    }

    /**
     * Moves Mother Nature the specified amount of steps (clockwise). After moving proceeds to calculate the new owner
     * of the <code>Island</code>.
     *
     * @param steps the amount of steps.
     */
    public void moveMN(int steps) {
        Game game = Game.getInstance();

        game.moveMN(steps);

        MatchInfo.getInstance().setMNMoved(true);

        TowerColor oldMostInfluentialTeam = getOldMostInfluentialTeam();
        TowerColor newMostInfluentialTeam = getMostInfluentialTeam();

        if(oldMostInfluentialTeam == newMostInfluentialTeam) return;

        int newOwnerTowerHolderID = game.getTowerHolderIDOf(newMostInfluentialTeam);
        game.removeTowersFrom(newOwnerTowerHolderID, getNumTowersOnIsland());

        if(oldMostInfluentialTeam != null) {
            int oldOwnerTowerHolderID = game.getTowerHolderIDOf(oldMostInfluentialTeam);
            game.addTowersTo(oldOwnerTowerHolderID, getNumTowersOnIsland());
        }

        game.conquerIsland(newMostInfluentialTeam);
        mergeIslands();
    }

    /**
     * Returns the most influential <code>TowerColor</code> on the <code>Island</code> where Mother Nature is currently
     * on.
     *
     * @return The color of the most influential team.
     */
    private TowerColor getMostInfluentialTeam() {
        TowerColor oldMostInfluential = getOldMostInfluentialTeam();
        TowerColor mostInfluentialTeam = null;
        int maxInfluenceScore = 0;

        for(TowerColor teamColor: TowerColor.values()) {
            int teamScore = getInfluenceOf(teamColor);

            if(teamScore > maxInfluenceScore ||
                    (teamScore == maxInfluenceScore && teamColor == oldMostInfluential)) { // in case of tie we keep the current team
                mostInfluentialTeam = teamColor;
                maxInfluenceScore = teamScore;
            }
        }

        return mostInfluentialTeam;
    }

    /**
     * Returns the influence of the specified <code>TowerColor</code> on the <code>Island</code> where Mother Nature is
     * currently on.
     *
     * @param teamColor the color of the team.
     * @return The influence score of the team.
     */
    protected int getInfluenceOf(TowerColor teamColor) {
        Game game = Game.getInstance();
        Board board = game.getBoard();

        Island targetIsland = board.getIslandAt(board.getMNPosition());
        ProfessorTracker professorOwners = board.getProfessorOwners();

        int teamScore = 0;

        for(Color c: Color.values()) {
            int ownerID = professorOwners.getOwnerIDByColor(c);

            if(ownerID != -1) {
                TowerColor professorOwnerTeamColor = game.getPlayerByID(ownerID).getTeamColor();

                if(teamColor == professorOwnerTeamColor) {
                    teamScore += targetIsland.getNumStudentsByColor(c);
                }
            }
        }

        if(getOldMostInfluentialTeam() == teamColor) {
            teamScore += targetIsland.getNumIslands();
        }

        return teamScore;
    }

    /**
     * Attempts to merge the <code>Island</code> where Mother Nature is currently on with the ones on the left and/or
     * right.
     */
    private void mergeIslands() {
        Game game = Game.getInstance();
        Board board = game.getBoard();

        int targetIslandIndex = board.getMNPosition();
        TowerColor targetTeamColor = board.getIslandAt(targetIslandIndex).getTeamColor();

        int rightIslandIndex = (targetIslandIndex + 1) % board.getNumIslands();
        if(board.getIslandAt(rightIslandIndex).getTeamColor() == targetTeamColor) {
            game.mergeIslands(targetIslandIndex, rightIslandIndex);
            targetIslandIndex = board.getMNPosition();
        }

        int leftIslandIndex = (targetIslandIndex > 0) ? targetIslandIndex - 1 : board.getNumIslands() - 1;
        if(board.getIslandAt(leftIslandIndex).getTeamColor() == targetTeamColor) {
            game.mergeIslands(leftIslandIndex, targetIslandIndex);
        }
    }

    /**
     * Returns the last most influential team on the <code>Island</code> where Mother Nature is currently on.
     *
     * @return <code>TowerColor</code> of the last most influential team.
     */
    private TowerColor getOldMostInfluentialTeam() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        int newMNPosition = board.getMNPosition();

        return board.getIslandAt(newMNPosition).getTeamColor();
    }

    /**
     * Returns the amount of towers on the <code>Island</code> where Mother Nature is currently on.
     *
     * @return Amount of towers.
     */
    private int getNumTowersOnIsland() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        int newMNPosition = board.getMNPosition();

        return board.getIslandAt(newMNPosition).getNumIslands();
    }
}
