package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.event.NovsWarJoinTeamEvent;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.event.NovsWarNewGameEvent;
import com.k9rosie.novswar.event.NovsWarScoreModifyEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.team.NovsScore;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NovsWarListener implements Listener {
    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public NovsWarListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarNewGame(NovsWarNewGameEvent event) {
    	novswar.getWorldManager().updateSigns(event.getGame());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarJoinTeam(NovsWarJoinTeamEvent event) {
        novswar.getWorldManager().updateSigns(event.getGame());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarLeaveTeam(NovsWarLeaveTeamEvent event) {
    	Game game = event.getGame();
    	if (event.isCancelled() == false) {
    		// Count the number of players still in-game
    		int inGamePlayerCount = novswar.getGameHandler().getGame().getInGamePlayers().size();
    		
    		// Update all NovsInfoSigns with in-game player count information
            novswar.getWorldManager().updateSigns(event.getGame());

    		// Assess in-game players
    		if (game.getGameState().equals(GameState.PRE_GAME) || game.getGameState().equals(GameState.DURING_GAME)) {
    			if (inGamePlayerCount == 0) {
        			game.nextGame(game.getWorld()); // restart the game if everyone leaves
        		} else {
            		if (game.checkPlayerCount() == false) { // if there are not enough players
            			switch (game.getGameState()) {
                            case PRE_GAME:
                                game.waitForPlayers();
                                break;
                            case DURING_GAME:
                                Bukkit.broadcastMessage("There are not enough players to continue the round.");
                                game.pauseGame();
                                break;
                            default:
                                break;
                    	}
            		} else { // if there are enough players, check for imbalance
            			int largestImbalance = novswar.getConfigManager().getCoreConfig().getGameLargestTeamImbalance();

            			if (largestImbalance <= 0) {
            				// re-balancing is disabled
            				return;
            			} else {
            				// Determine player counts for each team
            				boolean imbalanceFound = false;
            				for (NovsTeam teamA : novswar.getGameHandler().getGame().getTeams()) {
            					for (NovsTeam teamB : novswar.getGameHandler().getGame().getTeams()) {
            						if (!teamA.equals(teamB) && Math.abs(teamA.getTeamState().getPlayers().size() - teamB.getTeamState().getPlayers().size()) >= largestImbalance) {
            							imbalanceFound = true;
            						}
            					}
            				}

            				if (imbalanceFound) {
            					novswar.getGameHandler().getGame().balanceTeams();
            				}
            			}
            		}
        		}
    		}
    	}
    }
}
