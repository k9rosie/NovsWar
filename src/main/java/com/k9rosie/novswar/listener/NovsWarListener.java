package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.event.NovsWarJoinGameEvent;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.event.NovsWarNewGameEvent;
import com.k9rosie.novswar.event.NovsWarScoreModifyEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsScore;
import com.k9rosie.novswar.model.NovsTeam;

import com.k9rosie.novswar.model.NovsWorld;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
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
    public void onNovsWarScoreModify(NovsWarScoreModifyEvent event) {
        NovsScore score = event.getNovsScore();
        Game game = novswar.getGameHandler().getGame();
        int maxScore = game.getGamemode().getMaxScore();
        if (score.getScore() >= maxScore) {
            game.endGame();
        }
    }
    
    /**
     * New Game Event
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarNewGame(NovsWarNewGameEvent event) {
    	//Update all NovsInfoSigns with new round information	
    	for(Sign sign : novswar.getNovsWorldCache().getActiveSigns()) {
    		NovsWorld world = novswar.getGameHandler().getGame().getWorld();
			Gamemode gamemode = novswar.getGameHandler().getGame().getGamemode();
			novswar.getGameHandler().getGame().getGameHandler().updateInfoSign(world, gamemode);
    	}
    }
    
    /**
     * Join Game Event
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarJoinGame(NovsWarJoinGameEvent event) {
    	//Update all NovsInfoSigns with in-game player count information	
    	for(Sign sign : novswar.getNovsWorldCache().getActiveSigns()) {
    		int playerCount = novswar.getGameHandler().getGame().getGamePlayers().size();
    		novswar.getGameHandler().updatePlayers(playerCount);
    	}
    }
    
    /**
     * Decides whether or not to rebalance the teams when a player leaves one.
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarLeaveTeamModify(NovsWarLeaveTeamEvent event) {
    	Game game = event.getGame();
    	if(event.isCancelled() == false) {
    		//Count the number of players still in-game
    		int inGamePlayerCount = novswar.getGameHandler().getGame().getGamePlayers().size();
    		
    		//Update all NovsInfoSigns with in-game player count information	
        	for(Sign sign : novswar.getNovsWorldCache().getActiveSigns()) {
				novswar.getGameHandler().updatePlayers(inGamePlayerCount);
        	}

    		//Assess in-game players
    		if(game.getGameState().equals(GameState.PRE_GAME) || game.getGameState().equals(GameState.DURING_GAME)) {
    			if(inGamePlayerCount == 0) {
        			System.out.println("There are no in-game players. Starting new round.");
        			game.nextGame(game.getWorld());
        		} else {
        			System.out.println("A player left the game");
            		if(game.checkPlayerCount()==false) { //if there are not enough players
            			System.out.println("There are not enough players");
            			switch (game.getGameState()) {
                    	case PRE_GAME :
                    		game.waitForPlayers();
                    		break;
                    	case DURING_GAME :
                    		Bukkit.broadcastMessage("There are not enough players to continue the round.");
                    		game.pauseGame();
                    		break;
                		default :
                			break;
                    	}
            		} else { //if there are enough players, check for imbalance
            			int largestImbalance = novswar.getNovsConfigCache().getConfig("core").getInt("core.game.largest_team_imbalance");
            			if(largestImbalance == 0) {
            				//re-balancing is disabled
            				return;
            			} else {
            				//Determine player counts for each team
            				boolean imbalanceFound = false;
            				for(NovsTeam teamA : novswar.getNovsTeamCache().getTeams()) {
            					for(NovsTeam teamB : novswar.getNovsTeamCache().getTeams()) {
            						if(!teamA.equals(teamB) && 
            						  Math.abs(teamA.getPlayers().size() - teamB.getPlayers().size()) >= largestImbalance) {
            							imbalanceFound = true;
            						}
            					}
            				}
            				if(imbalanceFound) {
            					novswar.getGameHandler().getGame().balanceTeams();
            				}
            			}
            		}
        		}
    		}
    	}
    }
}
