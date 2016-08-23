package com.k9rosie.novswar.game;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarEndGameEvent;
import com.k9rosie.novswar.event.NovsWarJoinGameEvent;
import com.k9rosie.novswar.event.NovsWarNewGameEvent;
import com.k9rosie.novswar.event.NovsWarPlayerAssistEvent;
import com.k9rosie.novswar.event.NovsWarPlayerDeathEvent;
import com.k9rosie.novswar.event.NovsWarPlayerKillEvent;
import com.k9rosie.novswar.event.NovsWarPlayerRespawnEvent;
import com.k9rosie.novswar.event.NovsWarTeamVictoryEvent;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.Messages;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private GameState gameState;
    private boolean paused;
    private ArrayList<NovsTeam> enabledTeams;
    private HashMap<NovsPlayer, DeathTimer> deathTimers;
    private NovsWar novsWar;
    private GameTimer gameTimer;
    private GameScoreboard scoreboard;
    private BallotBox ballotBox;
    
    private int rounds;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        enabledTeams = new ArrayList<NovsTeam>();
        deathTimers = new HashMap<NovsPlayer, DeathTimer>();
        gameState = GameState.WAITING_FOR_PLAYERS;
        paused = false;
        novsWar = gameHandler.getNovsWarInstance();
        gameTimer = new GameTimer(this);
        scoreboard = new GameScoreboard(this);
        ballotBox = new BallotBox(novsWar);
        rounds = gamemode.getRounds();
    }

    public void initialize() {
    	//Create default team
        NovsTeam defaultTeam = novsWar.getNovsTeamCache().getDefaultTeam();
        Team defaultScoreboardTeam = scoreboard.createScoreboardTeam(defaultTeam);
        defaultTeam.setScoreboardTeam(defaultScoreboardTeam);

        //Populate the list 'enabledTeams' with valid NovsTeam objects
        List<String> enabledTeamNames = novsWar.getNovsConfigCache().getConfig("worlds").getStringList("worlds."+world.getBukkitWorld().getName()+".enabled_teams");
        for (String validTeam : enabledTeamNames) {
            for (NovsTeam team : novsWar.getNovsTeamCache().getTeams()) {
                if (validTeam.equalsIgnoreCase(team.getTeamName())) {
                	team.setScoreboardTeam(scoreboard.createScoreboardTeam(team));
                	enabledTeams.add(team);
                }
            }
        }
        for (NovsTeam team : enabledTeams) {
        	team.getNovsScore().setScore(0);	//Resets all team's scores
        }
        for (NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
        	player.setTeam(defaultTeam); // NovsPlayer now has private NovsTeam var
        	player.setSpectating(false); //remove from spectator mode
        	player.getSpectatorObservers().clear(); //clear spectators
            player.getBukkitPlayer().teleport(novsWar.getNovsWorldCache().getLobbyWorld().getTeamSpawnLoc(defaultTeam));
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            player.getBukkitPlayer().setHealth(19);//force a health change to update the health status objective
            player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
            player.getBukkitPlayer().setFoodLevel(20);
        }
        scoreboard.initialize();
        world.respawnBattlefields();
        NovsWarNewGameEvent event = new NovsWarNewGameEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        updateInfoSigns();
        waitForPlayers();
    }

    /**
     * onEndTimer()
     * Controls the next state of the game when the timer ends
     */
    public void onEndTimer() {
    	switch(gameState) {
            case PRE_GAME:
                startGame();
                break;
            case DURING_GAME:
                endGame();
                break;
            case POST_GAME:
                NovsWorld nextMap;
                if(novsWar.getNovsConfigCache().getConfig("core").getBoolean("core.voting.enabled") == true) {
                    nextMap = ballotBox.tallyResults();
                }
                else {
                    nextMap = ballotBox.nextWorld(world);
                }
                if(nextMap == null) {
                    nextMap = world;
                    ChatUtil.printDebug("There was a problem getting the next NovsWorld. Using previous world.");
                }
                gameHandler.newGame(nextMap);
                break;
            default:
                break;
            }
    }

    public void waitForPlayers() {
        gameState = GameState.WAITING_FOR_PLAYERS;
        gameTimer.stopTimer();
        scoreboard.setSidebarTitle("Waiting for players");
    }

    public void preGame() {
        gameState = GameState.PRE_GAME;
        int gameTime = novsWar.getNovsConfigCache().getConfig("core").getInt("core.game.pre_game_timer");
        gameTimer.stopTimer();
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
    }

    public void startGame() {
        gameState = GameState.DURING_GAME;
        world.openIntermissionGates();
        int gameTime = gamemode.getGameTime();
        gameTimer.stopTimer();
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
        ChatUtil.sendBroadcast("Starting Round");
    }

    public void pauseGame() {
    	paused = true;
    	ChatUtil.sendBroadcast("Pausing Round");
        world.closeIntermissionGates();
        for(NovsPlayer player : novsWar.getNovsPlayerCache().getGamePlayers()) {
        	if(player.isDead()) {
        		respawn(player);
        	} else {
        		player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(player.getTeam()));
        	}
        }
        gameTimer.pauseTimer();
        clockTick(); //Update the scoreboard with paused info
    }

    public void unpauseGame() {
        if (!paused) {
            return;
        }
        gameTimer.startTimer();
        ChatUtil.sendBroadcast("Resuming Round");
        world.openIntermissionGates();
    }

    /**
     * endGame()
     * Controls the team victory message and end-game stats. Respawns dead players.
     */
    public void endGame() {
        NovsWarEndGameEvent event = new NovsWarEndGameEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            gameState = GameState.POST_GAME;
            //Respawns all dead players and tp's alive players to their team spawns
            for(NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
            	//Check if the player is in-game (not on default lobby team)
            	NovsTeam defaultTeam = novsWar.getNovsTeamCache().getDefaultTeam();
            	if(player.getTeam().equals(defaultTeam)==false) {
            		if(player.isDead()) {
                		respawn(player);
                	} else {
                		player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(player.getTeam()));
                	}
                } else {
                	//Player is on default team
                	if(player.isSpectating()) {
                    	//Return the player to the lobby
                    	player.setSpectating(false); //must occur BEFORE gamemode change
                    	player.getBukkitPlayer().teleport(novsWar.getNovsWorldCache().getLobbyWorld().getTeamSpawns().get(defaultTeam));
                        player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
            //Determine winning teams
            ArrayList<NovsTeam> winners = winningTeams();
            System.out.println(winners.size());
            if (winners.size() == 1) {
                NovsTeam winner = winners.get(0);
                //Display victory message for all players, given single victor
                for(NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
                	SendTitle.sendTitle(player.getBukkitPlayer(), 0, 20*4, 20, " ", winner.getColor()+winner.getTeamName()+" §fwins!");
                }
            } else if (winners.size() > 1) {
                StringBuilder teamList = new StringBuilder();
                for (int i = 0; i < winners.toArray().length; i++) {
                    NovsTeam team = (NovsTeam) winners.toArray()[i];
                    teamList.append(team.getColor()+team.getTeamName());
                    if (i != winners.toArray().length-1) {
                        teamList.append(ChatColor.GRAY+", ");
                    }
                }
                //Display victory message for all players, given multiple victors
                for(NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
                	SendTitle.sendTitle(player.getBukkitPlayer(), 0, 20*4, 20, " ", teamList.toString() + " §fwin!");
                }
            } else { //no winners (all teams scored 0)
            	for(NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
                	SendTitle.sendTitle(player.getBukkitPlayer(), 0, 20*4, 20, " ", "§fDraw");
                }
            }
            for(NovsTeam winner : winners) {
            	NovsWarTeamVictoryEvent invokeEvent = new NovsWarTeamVictoryEvent(winner, this);
                Bukkit.getPluginManager().callEvent(invokeEvent);
            }
            //Stats generation
            for (NovsTeam team : winners) {
                for (NovsPlayer player : team.getPlayers()) {
                    player.getStats().incrementWins();
                }
            }
            for (NovsTeam team : enabledTeams) {
                for (NovsPlayer player : team.getPlayers()) {
                    player.getStats().incrementGamesPlayed();
                }
            }
            world.closeIntermissionGates();
            //world.respawnBattlefields(); //Redundant - the battlefield is respawned in the pre-game
            int gameTime = novsWar.getNovsConfigCache().getConfig("core").getInt("core.game.post_game_timer");
            gameTimer.stopTimer();
            gameTimer.setTime(gameTime);
            gameTimer.startTimer();
        	Bukkit.getScheduler().scheduleSyncDelayedTask(novsWar.getPlugin(), new Runnable() {
                @Override
                public void run() {
                	//Remove victory message
                	for(NovsPlayer player : novsWar.getNovsPlayerCache().getPlayers().values()) {
                		SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                	}
                	if(rounds <= 1) {
                		//This was the final round. Prompt voting.
                		if(novsWar.getNovsConfigCache().getConfig("core").getBoolean("core.voting.enabled") == true) {
                            ballotBox.castVotes();
                        }
                	} else {
                		//Start a new round
                    	rounds--;
                    	for (NovsTeam team : enabledTeams) {
                        	team.getNovsScore().setScore(0);	//Resets all team's scores
                        }
                    	novsWar.getNovsTeamCache().rotateTeams();
                    	preGame();
                    } 
                }
            }, 20*4);
        }
    }

    /**
     * Determines the team(s) with the highest score
     * @return ArrayList of NovsTeams with highest score
     */
    public ArrayList<NovsTeam> winningTeams() {
        ArrayList<NovsTeam> winningTeams = new ArrayList<NovsTeam>();
        int topScore = 0;
        NovsTeam topTeam = enabledTeams.get(0); //arbitrarily initialize topTeam as team 0
        //Find the team with the highest score
        for (NovsTeam team : enabledTeams) {
            if (team.getNovsScore().getScore() > topScore) {
            	topScore = team.getNovsScore().getScore();
            	topTeam = team;
            }
        }
        if(topScore != 0) {
        	winningTeams.add(topTeam);
            //Find other teams that are tied with the top team
            for (NovsTeam team : enabledTeams) {
            	if(team.equals(topTeam) == false && team.getNovsScore().getScore() == topScore) {
            		winningTeams.add(team);
            	}
            }
        }
        return winningTeams;
    }

    public void clockTick() {
        String secondsString = Integer.toString(gameTimer.getSeconds());
        String minutesString = Integer.toString(gameTimer.getMinutes());
        String gameStateString = "";

        switch (gameState) {
        case PRE_GAME :
        	gameStateString = ChatColor.GRAY + "Setting up: ";
        	break;
        case DURING_GAME :
        	gameStateString = "";
        	break;
        case POST_GAME :
        	gameStateString = ChatColor.GRAY + "Post game: ";
        	break;
    	default :
    		gameStateString = "";
    		break;
        }
        if (paused) {
            gameStateString = ChatColor.GRAY + "Game Paused ";
        }
        if (gameTimer.getSeconds() < 10) {
            secondsString = "0" + Integer.toString(gameTimer.getSeconds());
        } else if (gameTimer.getSeconds() <= 0) {
            secondsString = "00";
        }
        if (gameTimer.getMinutes() < 10) {
            minutesString = "0" + Integer.toString(gameTimer.getMinutes());
        } else if (gameTimer.getMinutes() <= 0) {
            minutesString = "00";
        }
        scoreboard.setSidebarTitle(gameStateString + ChatColor.GREEN + minutesString + ":" + secondsString);
    }

    /**
     * Checks player count on teams
     * @return True if there are the minimum required players in-game, else false
     */
    public boolean checkPlayerCount() {
        int numPlayers = 0;
        int required = novsWar.getNovsConfigCache().getConfig("core").getInt("core.game.minimum_players");
        for (NovsTeam team : enabledTeams) {
            numPlayers += team.getPlayers().size();
        }
        if (numPlayers >= required) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Produces death messages, evaluates stats, assists, schedules death (spectating) and calls events
     * @param victim
     * @param attacker
     * @param isArrowDeath
     */
    public void killPlayer(NovsPlayer victim, NovsPlayer attacker, boolean isArrowDeath) {
        //Generate death message
        String deathMessage;
        if(attacker != null) {
        	//There is a valid attacker
        	//Evaluate statistics
        	if (isArrowDeath) {
                deathMessage = Messages.SHOT_MESSAGE.toString();
                attacker.getStats().incrementArrowKills();
                victim.getStats().incrementArrowDeaths();
            } else {
                deathMessage = Messages.KILL_MESSAGE.toString();
                attacker.getStats().incrementKills();
                victim.getStats().incrementDeaths();
            }
            deathMessage = deathMessage.replace("%killed_tcolor%", victim.getTeam().getColor().toString())
                    .replace("%killed%", victim.getBukkitPlayer().getDisplayName())
                    .replace("%killer_tcolor%", attacker.getTeam().getColor().toString())
                    .replace("%killer%", attacker.getBukkitPlayer().getDisplayName());
        } else {
        	//There is no attacker
        	deathMessage = Messages.DEATH_MESSAGE.toString();
            deathMessage = deathMessage.replace("%player_tcolor%", victim.getTeam().getColor().toString())
            		.replace("%player%", victim.getBukkitPlayer().getDisplayName());
        }
        //Print death message to all players
        for (NovsPlayer p : novsWar.getNovsPlayerCache().getPlayers().values()) {
            if (p.canSeeDeathMessages()) {
                p.getBukkitPlayer().sendMessage(deathMessage);
            }
        }
    	//Evaluate assists
        NovsPlayer assistAttacker = victim.getAssistAttacker(attacker);
        victim.clearAttackers();
        //Schedule death spectating
        scheduleDeath(victim, attacker, gamemode.getDeathTime());
        //Event calls
        ChatUtil.printDebug("...Calling events");
        if(attacker != null) { //if there is an attacker, invoke kill event
	        NovsWarPlayerKillEvent invokeEvent = new NovsWarPlayerKillEvent(attacker, victim, attacker.getTeam(), victim.getTeam(), this);
	        Bukkit.getPluginManager().callEvent(invokeEvent);
        } else { //if there isn't an attacker, increment suicides
        	victim.getStats().incrementSuicides();
        	NovsWarPlayerDeathEvent invokeEvent = new NovsWarPlayerDeathEvent(victim, victim.getTeam(), this);
	        Bukkit.getPluginManager().callEvent(invokeEvent);
        }
        if(assistAttacker != null) {
            NovsWarPlayerAssistEvent invokeEvent_1 = new NovsWarPlayerAssistEvent(assistAttacker, victim, assistAttacker.getTeam(), victim.getTeam(), this);
            Bukkit.getPluginManager().callEvent(invokeEvent_1);
        }
    }
    
    private void scheduleDeath(NovsPlayer player, NovsPlayer spectatorTarget, int seconds) {
        //Player bukkitPlayer = player.getBukkitPlayer();
        player.setDeath(true);
        player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
        player.getBukkitPlayer().setFoodLevel(20);
        for(PotionEffect effect : player.getBukkitPlayer().getActivePotionEffects()) {
        	player.getBukkitPlayer().removePotionEffect(effect.getType());
        }
        player.getBukkitPlayer().getWorld().playEffect(player.getBukkitPlayer().getLocation(), Effect.SMOKE, 30, 2);
        player.getBukkitPlayer().getWorld().playSound(player.getBukkitPlayer().getLocation(), Sound.ENTITY_WITCH_DEATH, 5, 0.5f);
        
        ChatUtil.printDebug("..."+player.getBukkitPlayer().getName()+" died and has observers: ");
        //Set each observer for this player to a new target
        for(NovsPlayer observer : player.getSpectatorObservers()) {
        	ChatUtil.printDebug("    "+observer.getBukkitPlayer().getName());
        	nextSpectatorTarget(observer);
        }
        //Clear this player's observer list
        player.getSpectatorObservers().clear();
        player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
        //If there is an attacker, set spectator target.
        if (spectatorTarget != null) {
        	ChatUtil.printDebug("...There is an attacker");
        	setSpectatorTarget(player, spectatorTarget);
        } else {
        	//Check if there are available spectator targets
            ChatUtil.printDebug("...There is NO attacker");
            nextSpectatorTarget(player);
        }
        DeathTimer timer = new DeathTimer(this, seconds, player);
        timer.startTimer();
        deathTimers.put(player, timer);
    }

    public void deathTick(NovsPlayer player) {
        DeathTimer timer = deathTimers.get(player);
        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 2000, 0, " ", "Respawn in " + Integer.toString(timer.getSeconds()) + "...");
    }

    public void respawn(NovsPlayer player) {
        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
        DeathTimer timer = deathTimers.get(player);
        timer.stopTimer();
        deathTimers.remove(player);
        player.getBukkitPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(scoreboard.getSidebarTitle());

        if (player.isDead()) {
            NovsTeam team = player.getTeam();
            player.setDeath(false);
            //Remove this player from their target's observer list
            Player currentTarget = (Player) player.getBukkitPlayer().getSpectatorTarget();
            if(currentTarget != null) {
            	if (novsWar.getNovsPlayerCache().getPlayers().get(currentTarget).getSpectatorObservers().remove(player)==false) {
            		ChatUtil.printDebug("Failed to remove "+player.getBukkitPlayer().getName()+" from observer list");
            	}
            }
            player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(team));
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            //Invoke Event
            NovsWarPlayerRespawnEvent invokeEvent = new NovsWarPlayerRespawnEvent(player, this);
	        Bukkit.getPluginManager().callEvent(invokeEvent);
        }
    }

    public void joinGame(NovsPlayer player) {
        
        boolean canJoinInProgress = novsWar.getNovsConfigCache().getConfig("core").getBoolean("core.game.join_in_progress");

        if (!canJoinInProgress && (gameState.equals(GameState.DURING_GAME) || gameState.equals(GameState.POST_GAME))) {
            ChatUtil.sendNotice(player, Messages.CANNOT_JOIN_GAME.toString());
            return;
        }
        //Invoke event
        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()==false) {
        	novsWar.getNovsTeamCache().assignPlayerTeam(player);
        }
        
        if (checkPlayerCount()) {
        	switch (gameState) {
        	case WAITING_FOR_PLAYERS :
        		preGame();
        		break;
    		default :
    			break;
        	}
        } else {
        	int minimum = novsWar.getNovsConfigCache().getConfig("core").getInt("core.game.minimum_players");
        	String message = Messages.NOT_ENOUGH_PLAYERS.toString().replace("%minimum%", Integer.toString(minimum));
            ChatUtil.sendBroadcast(message);
        }

        if (paused) {
            unpauseGame();
        }
    }
    
    public void nextGame(NovsWorld world) {
    	if(gameTimer.getTaskID() != 0) { //if there is a running timer
			System.out.println("Stopped timer");
			gameTimer.stopTimer();
		}
		gameHandler.newGame(world);
    }
    
    /**
     * Sets the spectator target of observer. If there is an invalid target, dead players tp back
     * to their spawns and spectators are forced out of spectate mode.
     * @param observer
     * @param target
     */
    public void setSpectatorTarget(NovsPlayer observer, NovsPlayer target) {
    	if(target != null) {
	    	ChatUtil.printDebug("Setting "+observer.getBukkitPlayer().getName()+"'s target to "+target.getBukkitPlayer().getName());
	    	observer.getBukkitPlayer().teleport(target.getBukkitPlayer().getLocation());
	    	observer.getBukkitPlayer().setSpectatorTarget(target.getBukkitPlayer());
	    	target.getSpectatorObservers().add(observer);
	    	ChatUtil.sendNotice(observer, "Spectating "+target.getBukkitPlayer().getName());
    	} else {
    		ChatUtil.printDebug(observer.getBukkitPlayer().getName()+" is setting an invalid target");
    		//Reset spectator target
    		observer.getBukkitPlayer().setSpectatorTarget(null);
    		if(observer.isSpectating()) {
    			//player is spectating, return them to lobby
    			quitSpectating(observer);
    		} else {
    			//Player is dead, tp them to their spawn
    			observer.getBukkitPlayer().teleport(world.getTeamSpawnLoc(observer.getTeam()));
    		}
    	}
    }
    
    /**
     * Iterates through all in-game players and sets the spectator target to the new player.
     * @param observer
     */
    public void nextSpectatorTarget(NovsPlayer observer) {
    	NovsPlayer target = null;
    	if(observer.isDead() || observer.isSpectating()) {
	    	ChatUtil.printDebug(observer.getBukkitPlayer().getName()+" is switching spectator targets");
	    	NovsPlayer currentTarget = novsWar.getNovsPlayerCache().getPlayers().get(observer.getBukkitPlayer().getSpectatorTarget());
	    	ArrayList<NovsPlayer> inGamePlayers = novsWar.getNovsPlayerCache().getGamePlayers();
	        inGamePlayers.remove(observer);	//Remove this player from the options of spectator targets
	        int index = inGamePlayers.indexOf(currentTarget); //if currentTarget is null, index will get -1
	        int nextIndex = index + 1; //If currentTarget is null, nextIndex will be 0

	        boolean foundValidTarget = false;
	        if(inGamePlayers.size() > 0) {
		        int watchdog = 0;
		        while(foundValidTarget == false) {
		        	if(nextIndex >= inGamePlayers.size()) {
		                nextIndex = 0;
		            }
		        	NovsPlayer potentialTarget = inGamePlayers.get(nextIndex);
		        	if(potentialTarget.isDead()==false) {
		        		target = potentialTarget;
		        		foundValidTarget = true;
		        	}
		        	if(watchdog >= inGamePlayers.size()){
	                    ChatUtil.printDebug("Could not find valid spectator target");
		        		break;
		        	}
		        	watchdog++;
		        	nextIndex++;
		        }
	        }
	        
	        if(foundValidTarget) {
                ChatUtil.printDebug("...New target is "+target.getBukkitPlayer().getName());
	        } else {	
	        	ChatUtil.printDebug("WARNING: nextSpectatorTarget could not find a valid target for player "+observer.getBukkitPlayer().getName());
	        }
	        setSpectatorTarget(observer, target);
	        
    	} else {
            ChatUtil.printDebug("WARNING: Attempted to call nextSpectatorTarget on an alive/non-spectating player");
    	}
    }
    
    /**
     * Removes a lobby player from spectate mode
     * @param spectator
     */
    public void quitSpectating(NovsPlayer spectator) {
    	if(spectator.isSpectating()) {
    		spectator.setSpectating(false); //must occur BEFORE gamemode change
    		spectator.getBukkitPlayer().teleport(novsWar.getNovsWorldCache().getLobbyWorld().getTeamSpawns().get(novsWar.getNovsTeamCache().getDefaultTeam()));
    		spectator.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            ChatUtil.sendBroadcast(spectator.getBukkitPlayer().getName()+" stopped spectating.");
    	} else {
    		ChatUtil.printDebug("WARNING: Attempted to call quitSpectating on a non-spectating player");
    	}
    }
    
    public void updateInfoSigns() {
    	ChatUtil.printDebug("Updating info signs...");
    	int required = novsWar.getNovsConfigCache().getConfig("core").getInt("core.game.minimum_players");
        for (Sign sign : novsWar.getNovsWorldCache().getActiveSigns()) {
        	ChatUtil.printDebug(sign.getLocation().toString());
            sign.setLine(0,  "§2Map Info");
        	sign.setLine(1, world.getName());
            sign.setLine(2, gamemode.getGamemodeName());
            sign.setLine(3, novsWar.getNovsPlayerCache().getGamePlayers().size() + "/"+required+" players");
            sign.update(true);
        }
    }
    
    //Getters
    
    public GameScoreboard getScoreboard() {
        return scoreboard;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public BallotBox getBallotBox() {
    	return ballotBox;
    }
    
    public ArrayList<NovsTeam> getTeams() {
    	return enabledTeams;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }
    
    public GameState getGameState() {
    	return gameState;
    }
    
    public NovsWorld getWorld() {
    	return world;
    }
    
    public boolean isPaused() {
        return paused;
    }
}
