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
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.team.TeamState;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.player.PlayerState;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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
    private NovsWar novsWar;
    private GameTimer gameTimer;
    private GameScoreboard scoreboard;
    private BallotBox ballotBox;
    private ArrayList<NovsTeam> enabledTeams;
    
    private int rounds;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        gameState = GameState.WAITING_FOR_PLAYERS;
        paused = false;
        novsWar = gameHandler.getNovsWarInstance();
        gameTimer = new GameTimer(this);
        scoreboard = new GameScoreboard(this);
        ballotBox = new BallotBox(novsWar);
        enabledTeams = new ArrayList<>();
        rounds = gamemode.getRounds();
    }

    public void initialize() {
        novsWar.getTeamManager().clearTeamStates(); // clear team states for next game

    	// Create TeamState data for default team
        NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
        Team defaultScoreboardTeam = scoreboard.createScoreboardTeam(defaultTeam);
        defaultTeam.setTeamState(new TeamState(this, defaultTeam, defaultScoreboardTeam));

        // populate enabledTeams list
        String[] list = novsWar.getConfigManager().getWorldsConfig().getWorldData().get(world.getBukkitWorld().getName()).getEnabledTeams();
        for (String teamName : list) {
            enabledTeams.add(novsWar.getTeamManager().getTeams().get(teamName));
        }

        // create new TeamStates for every team
        for (NovsTeam team : novsWar.getTeamManager().getTeams().values()) {
            Team scoreboardTeam = scoreboard.createScoreboardTeam(team);
            TeamState teamState = new TeamState(this, team, scoreboardTeam);
            team.setTeamState(teamState);
        }

        // create new PlayerStates for every team
        for (NovsPlayer player : novsWar.getPlayerManager().getNovsPlayers()) {
            PlayerState playerState = new PlayerState(this, player, defaultTeam);
            player.setPlayerState(playerState);
        }

        // set the score depending on the gamemode's score type
        for (NovsTeam team : enabledTeams) {
            switch (gamemode.getScoreType()) {
                case ASCENDING:
                    team.getNovsScore().setScore(0);
                case DESCENDING:
                    team.getNovsScore().setScore(gamemode.getMaxScore());
            }
        }

        // teleport players to lobby spawn, set their health and food
        for (NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
            player.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawnLoc(defaultTeam));
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.getBukkitPlayer().setFoodLevel(20);
        }

        scoreboard.initialize();
        world.respawnBattlefields();
        NovsWarNewGameEvent event = new NovsWarNewGameEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        novsWar.getWorldManager().updateInfoSigns(this);
        waitForPlayers();
    }

    /**
     * endTimer()
     * Controls the next state of the game when the timer ends
     */
    public void endTimer() {
    	switch(gameState) {
            case PRE_GAME:
                startGame();
                break;
            case DURING_GAME:
                endGame();
                break;
            case POST_GAME:
                postGame();
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
        int gameTime = novsWar.getConfigManager().getConfig("core").getInt("core.game.pre_game_timer");
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
        for(NovsPlayer player : novsWar.getPlayerManager().getGamePlayers()) {
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

            // Respawns all dead players and tp's alive players to their team spawns
            for (NovsPlayer player : getInGamePlayers()) {
                if (player.getPlayerState().isDead()) {
                    player.getPlayerState().respawn();
                } else {
                    player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(player.getPlayerState().getTeam()));
                }
                /*
                } else {
                	//Player is on default team
                	if(player.isSpectating()) {
                    	//Return the player to the lobby
                    	player.setSpectating(false); //must occur BEFORE gamemode change
                    	player.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
                        player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                }*/
            }


            // Determine winning teams
            ArrayList<NovsTeam> winners = winningTeams();
            if (winners.size() == 1) { // if there's only one winning team
                NovsTeam winner = winners.get(0);

                // Display victory message for all players, given single victor
                novsWar.getPlayerManager().broadcastSubtitle(winner.getColor()+winner.getTeamName()+" §fwins!");

            } else if (winners.size() > 1) { // if multiple teams have won (tie)

                StringBuilder teamList = new StringBuilder();
                for (int i = 0; i < winners.toArray().length; i++) {
                    NovsTeam team = (NovsTeam) winners.toArray()[i];
                    teamList.append(team.getColor()+team.getTeamName());
                    if (i != winners.toArray().length-1) {
                        teamList.append(ChatColor.GRAY+", ");
                    }
                }

                // Display victory message for all players, given multiple victors
                novsWar.getPlayerManager().broadcastSubtitle(teamList.toString() + " §ftie!");

            } else { // no winners (all teams scored 0)
            	for(NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
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
            int gameTime = novsWar.getConfigManager().getConfig("core").getInt("core.game.post_game_timer");
            gameTimer.stopTimer();
            gameTimer.setTime(gameTime);
            gameTimer.startTimer();
        	Bukkit.getScheduler().scheduleSyncDelayedTask(novsWar.getPlugin(), new Runnable() {
                @Override
                public void run() {
                	//Remove victory message
                	for(NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
                		SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                	}
                	if(rounds <= 1) {
                		//This was the final round. Prompt voting.
                		if(novsWar.getConfigManager().getConfig("core").getBoolean("core.voting.enabled") == true) {
                            ballotBox.castVotes();
                        }
                	} else {
                		//Start a new round
                    	rounds--;
                    	for (NovsTeam team : enabledTeams) {
                            switch (gamemode.getScoreType()) {
                                case ASCENDING:
                                    team.getNovsScore().setScore(0);
                                case DESCENDING:
                                    team.getNovsScore().setScore(gamemode.getMaxScore());
                            }
                        }
                    	novsWar.getTeamManager().rotateTeams();
                    	preGame();
                    } 
                }
            }, 20*4);
        }
    }

    public void postGame() {
        NovsWorld nextMap;
        if (novsWar.getConfigManager().getCoreConfig().getVotingEnabled() == true) {
            nextMap = ballotBox.tallyResults();
        } else {
            nextMap = ballotBox.nextWorld(world);
        }
        gameHandler.newGame(nextMap);
    }

    /**
     * Determines the team(s) with the highest score
     * @return ArrayList of NovsTeams with highest score
     */
    public ArrayList<NovsTeam> winningTeams() {
        ArrayList<NovsTeam> winningTeams = new ArrayList<>();
        int topScore = 0;

        NovsTeam topTeam = null;
        // Find the team with the highest score
        for (NovsTeam team : enabledTeams) {
            if (team.getTeamState().getScore().value() > topScore) {
            	topScore = team.getTeamState().getScore().value();
            	topTeam = team;
            }
        }

        if (topScore != 0) {
        	winningTeams.add(topTeam);
            //Find other teams that are tied with the top team
            for (NovsTeam team : enabledTeams) {
            	if(team.equals(topTeam) == false && team.getTeamState().getScore().value() == topScore) {
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
        int required = novsWar.getConfigManager().getConfig("core").getInt("core.game.minimum_players");
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
        for (NovsPlayer p : novsWar.getPlayerManager().getPlayers().values()) {
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
	        NovsWarPlayerKillEvent invokeEvent = new NovsWarPlayerKillEvent(victim, attacker, victim.getTeam(), attacker.getTeam(), this);
	        Bukkit.getPluginManager().callEvent(invokeEvent);
        } else { //if there isn't an attacker, increment suicides
        	victim.getStats().incrementSuicides();
        	NovsWarPlayerDeathEvent invokeEvent = new NovsWarPlayerDeathEvent(victim, victim.getTeam(), true, this);
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
        player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.getBukkitPlayer().setFoodLevel(20);

        for(PotionEffect effect : player.getBukkitPlayer().getActivePotionEffects()) {
        	player.getBukkitPlayer().removePotionEffect(effect.getType());
        }
        player.getBukkitPlayer().getWorld().playEffect(player.getBukkitPlayer().getLocation(), Effect.SMOKE, 30, 2);
        player.getBukkitPlayer().getWorld().playSound(player.getBukkitPlayer().getLocation(), Sound.ENTITY_WITCH_DEATH, 5, 0.5f);

        player.getBukkitPlayer().setWalkSpeed(0f);
        player.getBukkitPlayer().setFlySpeed(0f);

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

    public void joinGame(NovsPlayer player) {
        
        boolean canJoinInProgress = novsWar.getConfigManager().getConfig("core").getBoolean("core.game.join_in_progress");

        if (!canJoinInProgress && (gameState.equals(GameState.DURING_GAME) || gameState.equals(GameState.POST_GAME))) {
            ChatUtil.sendNotice(player, Messages.CANNOT_JOIN_GAME.toString());
            return;
        }

        if (!player.getTeam().equals(novsWar.getTeamManager().getDefaultTeam())) {
            ChatUtil.sendNotice(player, "You're already in the game.");
            return;
        }

        //Invoke event
        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()==false) {
        	novsWar.getTeamManager().assignPlayerTeam(player);
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
        	int minimum = novsWar.getConfigManager().getConfig("core").getInt("core.game.minimum_players");
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
        this.world.closeIntermissionGates();
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
	    	NovsPlayer currentTarget = novsWar.getPlayerManager().getPlayers().get(observer.getBukkitPlayer().getSpectatorTarget());
	    	ArrayList<NovsPlayer> inGamePlayers = novsWar.getPlayerManager().getGamePlayers();
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
    		spectator.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(novsWar.getTeamManager().getDefaultTeam()));
    		spectator.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            ChatUtil.sendBroadcast(spectator.getBukkitPlayer().getName()+" stopped spectating.");
    	} else {
    		ChatUtil.printDebug("WARNING: Attempted to call quitSpectating on a non-spectating player");
    	}
    }

    /**
     * Sends all in-game players to their spawns and balances the teams
     */
    public void balanceTeams() {
        Game game = novswar.getGameHandler().getGame();
        game.pauseGame();
        messageTime = 5;
        messageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(novswar.getPlugin(), new Runnable() {
            public void run() {
                ArrayList<NovsPlayer> autobalancePlayers = new ArrayList<NovsPlayer>();
                for(NovsPlayer player : novswar.getPlayerManager().getGamePlayers()) {
                    SendTitle.sendTitle(player.getBukkitPlayer(), 0, 2000, 0, " ", "Team Auto-Balance in "+messageTime+"...");
                    autobalancePlayers.add(player);
                }
                messageTime--;
                if(messageTime <= 0) {
                    Bukkit.getScheduler().cancelTask(messageTask);
                    //Set every player's team to default
                    for(NovsPlayer player : autobalancePlayers) {
                        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                        player.setTeam(novswar.getTeamManager().getDefaultTeam());
                    }
                    //re-do the team sorting algorithm
                    for(NovsPlayer player : autobalancePlayers) {
                        assignPlayerTeam(player);
                    }
                    game.unpauseGame();
                }
            }
        }, 0, 20);
    }

    /**
     * Assigns all in-game players to the next team index in the NovsTeam array list
     */
    public void rotateTeams() {
        Game game = novswar.getGameHandler().getGame();
        HashMap<NovsTeam, NovsTeam> rotationMap = new HashMap<NovsTeam, NovsTeam>(); //key = source, value = target
        int targetIndex = 0;
        //Generate map for team switching
        for(int sourceIndex = 0; sourceIndex < novswar.getTeamManager().getTeams().size(); sourceIndex++) {
            targetIndex = sourceIndex + 1;
            if(targetIndex >= novswar.getTeamManager().getTeams().size()) {
                targetIndex = 0;
            }
            rotationMap.put(novswar.getTeamManager().getTeams().get(sourceIndex), novswar.getTeamManager().getTeams().get(targetIndex));
        }
        //Switch teams for each player in-game
        for(NovsPlayer player : novswar.getPlayerManager().getGamePlayers()) {
            NovsTeam newTeam = rotationMap.get(player.getTeam());
            player.setTeam(newTeam);
            player.getBukkitPlayer().teleport(game.getWorld().getTeamSpawnLoc(newTeam));
            player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.getBukkitPlayer().setFoodLevel(20);
        }
    }
    
    //Getters

    public ArrayList<NovsPlayer> getInGamePlayers() {
        ArrayList<NovsPlayer> players = new ArrayList<>();

        for (NovsPlayer player : novsWar.getPlayerManager().getNovsPlayers()) {
            if (enabledTeams.contains(player.getPlayerState().getTeam())) {
                players.add(player);
            }
        }

        return players;
    }
    
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

    public HashMap<NovsPlayer, DeathTimer> getDeathTimers() { return deathTimers; }
}
