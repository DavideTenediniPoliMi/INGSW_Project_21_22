package it.polimi.ingsw.controller.subcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.ProfessorTracker;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.List;
import java.util.Optional;

public class IslandController {
    public IslandController() {
    }

    public void moveMN(int steps) {
        Game game = Game.getInstance();

        game.moveMN(steps);

        TowerColor oldMostInfluentialTeam = getOldMostInfluentialTeam();
        TowerColor newMostInfluentialTeam = getMostInfluentialTeam();

        if(oldMostInfluentialTeam == newMostInfluentialTeam) return;

        int newOwnerTowerHolderID = getTowerHolderIDOf(newMostInfluentialTeam);
        game.removeTowersFrom(newOwnerTowerHolderID, getNumTowersOnIsland());

        if(oldMostInfluentialTeam != null) {
            int oldOwnerTowerHolderID = getTowerHolderIDOf(oldMostInfluentialTeam);
            game.addTowersTo(oldOwnerTowerHolderID, getNumTowersOnIsland());
        }

        game.conquerIsland(newMostInfluentialTeam);
        mergeIslands();
    }

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

    private TowerColor getOldMostInfluentialTeam() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        int newMNPosition = board.getMNPosition();

        return board.getIslandAt(newMNPosition).getTeamColor();
    }

    private int getNumTowersOnIsland() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        int newMNPosition = board.getMNPosition();

        return board.getIslandAt(newMNPosition).getNumIslands();
    }

    private int getTowerHolderIDOf(TowerColor teamColor) {
        List<Player> players = Game.getInstance().getPlayers();

        Optional<Player> result = players.stream()
                .filter((player) -> (player.isTowerHolder() && player.getTeamColor() == teamColor))
                .findAny();

        if(result.isEmpty()) return -1;
        return result.get().getID();
    }
}
