package com.k9rosie.novswar.game;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.event.NovsWarEndGameEvent;
import com.k9rosie.novswar.event.NovsWarJoinGameEvent;
import com.k9rosie.novswar.event.NovsWarNewGameEvent;
import com.k9rosie.novswar.event.NovsWarTeamVictoryEvent;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.player.DeathTimer;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.team.TeamState;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.player.PlayerState;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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

        // set the initial scores for the teams
        gamemode.setInitialScores();

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
        int gameTime = novsWar.getCoreConfig().getGamePreGameTimer();
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
        for (NovsPlayer player : getInGamePlayers()) {
        	if (player.getPlayerState().isDead()) {
        		player.getPlayerState().respawn();
        	} else {
        		player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(player.getPlayerState().getTeam()));
        	}
        }

        gameTimer.pauseTimer();
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

            }

            // eject all specating players from spectator mode
            for (NovsPlayer player : novsWar.getPlayerManager().getNovsPlayers()) {
                if (player.getPlayerState().isSpectating()) {
                    player.getPlayerState().quitSpectating();
                }
            }

            // Determine winning teams
            ArrayList<NovsTeam> winners = gamemode.getWinningTeams();
            if (winners.size() == 1) { // if there's only one winning team
                NovsTeam winner = winners.get(0);

                // Display victory message for all players, given single victor
                SendTitle.broadcastSubtitle(winner.getColor()+winner.getTeamName()+" §fwins!");

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
                SendTitle.broadcastSubtitle(teamList.toString() + " §ftie!");

            } else { // no winners (all teams scored 0)
                SendTitle.broadcastSubtitle("§fNo winners");
            }

            for(NovsTeam winner : winners) {
            	NovsWarTeamVictoryEvent invokeEvent = new NovsWarTeamVictoryEvent(winner, this);
                Bukkit.getPluginManager().callEvent(invokeEvent);
            }

            //Stats generation
            for (NovsTeam team : winners) {
                for (NovsPlayer player : team.getTeamState().getPlayers()) {
                    player.getStats().incrementWins();
                }
            }

            for (NovsTeam team : enabledTeams) {
                for (NovsPlayer player : team.getTeamState().getPlayers()) {
                    player.getStats().incrementGamesPlayed();
                }
            }

            world.closeIntermissionGates();

            int gameTime = novsWar.getConfigManager().getCoreConfig().getGamePostGameTimer();
            gameTimer.stopTimer();
            gameTimer.setTime(gameTime);
            gameTimer.startTimer();

            // We delay the next couple of actions for better game flow effect
        	Bukkit.getScheduler().scheduleSyncDelayedTask(novsWar.getPlugin(), new Runnable() {
                @Override
                public void run() {
                	//Remove victory message
                	for(NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
                		SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                	}
                	if(rounds <= 1) {
                		//This was the final round. Prompt voting.
                		if(novsWar.getConfigManager().getCoreConfig().getVotingEnabled() == true) {
                            ballotBox.promptVoting();
                        }
                	} else {
                		//Start a new round
                    	rounds--;
                    	gamemode.setInitialScores();
                    	rotateTeams();
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
            nextMap = nextWorld(world);
        }

        gameHandler.newGame(nextMap);
    }

    // This has been moved to the Gamemode class to give gamemodes more power to who wins the game
    /*
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
    }*/


    /**
     * Checks player count on teams
     * @return True if there are the minimum required players in-game, else false
     */
    public boolean checkPlayerCount() {
        int numPlayers = 0;
        int required = novsWar.getCoreConfig().getGameMinimumPlayers();
        for (NovsTeam team : enabledTeams) {
            numPlayers += team.getTeamState().getPlayers().size();
        }
        if (numPlayers >= required) {
            return true;
        } else {
            return false;
        }
    }

    public void joinGame(NovsPlayer player) {
        boolean canJoinInProgress = novsWar.getCoreConfig().getGameJoinInProgress();

        if (!canJoinInProgress && (gameState.equals(GameState.DURING_GAME) || gameState.equals(GameState.POST_GAME))) {
            ChatUtil.sendNotice(player, MessagesConfig.getCannotJoinGame());
            return;
        }

        if (!player.getPlayerState().getTeam().equals(novsWar.getTeamManager().getDefaultTeam())) {
            ChatUtil.sendNotice(player, "You're already in the game.");
            return;
        }

        //Invoke event
        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() == false) {
        	assignTeam(player);
        }
        
        if (checkPlayerCount()) {
            if (gameState == GameState.WAITING_FOR_PLAYERS) {
                preGame();
            }
            
            if (paused) {
                unpauseGame();
            }
        } else {
        	int minimum = novsWar.getCoreConfig().getGameMinimumPlayers();
        	String message = MessagesConfig.getNotEnoughPlayers(Integer.toString(minimum));
            ChatUtil.sendBroadcast(message);
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

    public void assignTeam(NovsPlayer player) {
        // novsloadout has its own way of sorting players, only run this code if it isnt enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("NovsLoadout")) { // TODO: convert this into an event

            //Determine which team has the fewest players and put them on there
            NovsTeam smallestTeam = enabledTeams.get(0);
            int smallest = smallestTeam.getTeamState().getPlayers().size();
            for (NovsTeam team : enabledTeams) {
                if(team.getTeamState().getPlayers().size() <= smallest) {
                    smallest = team.getTeamState().getPlayers().size();
                    smallestTeam = team;
                }
            }
            smallestTeam.getTeamState().addPlayer(player);
        }
    }

    public NovsWorld nextWorld(NovsWorld currentWorld) {
        NovsWorld nextWorld = null;
        String currentWorldName = currentWorld.getBukkitWorld().getName();
        List<String> enabledWorlds = novsWar.getConfigManager().getCoreConfig().getEnabledWorlds();

        for (int i = 0; i < enabledWorlds.size(); i++) {
            if (enabledWorlds.get(i).equals(currentWorldName)) {
                int nextIndex = i+1;
                if(nextIndex == enabledWorlds.size()) {
                    nextIndex = 0;
                }
                nextWorld = novsWar.getWorldManager().getWorld(Bukkit.getWorld(enabledWorlds.get(nextIndex)));
            }
        }

        return nextWorld;
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

    public void nextSpectatorTarget(NovsPlayer observer) {
        NovsPlayer target = null;

        if (observer.getPlayerState().isDead() || observer.getPlayerState().isSpectating()) {
            NovsPlayer currentTarget = novsWar.getPlayerManager().getPlayers().get(observer.getBukkitPlayer().getSpectatorTarget());

            if (currentTarget != null) {
                ArrayList<NovsPlayer> validTargets = getInGamePlayers();
                validTargets.remove(observer);

                if (validTargets.size() > 0) {
                    int index = validTargets.indexOf(currentTarget);
                    int nextIndex = (index + 1) >= validTargets.size() ? 0 : index + 1; // if nextIndex is larger than the size of the validTargets then return 0

                    for (int i = 0; i < validTargets.size(); i++) {
                        NovsPlayer potentialTarget = validTargets.get(nextIndex);

                        // do some conditionals to check if they are eligible to spectate
                        if (potentialTarget.getPlayerState().isDead() == false) {
                            target = potentialTarget;
                        }
                    }
                }
            }

            if(target != null) {
                observer.getPlayerState().setSpectatorTarget(target);
            } else {
                ChatUtil.sendError(observer, "A valid spectator target couldn't be found. Teleporting to spawn...");
                observer.getBukkitPlayer().setSpectatorTarget(null);
                if (observer.getPlayerState().isSpectating()) {
                    observer.getPlayerState().quitSpectating(observer);
                } else {
                    observer.getBukkitPlayer().teleport(world.getTeamSpawnLoc(observer.getPlayerState().getTeam()));
                }
            }
        }
    }

    public void printDeathMessage(String message) {
        for (NovsPlayer player : novsWar.getPlayerManager().getNovsPlayers()) {
            if (player.canSeeDeathMessages()) {
                player.getBukkitPlayer().sendMessage(message);
            }
        }
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

    public NovsWar getNovsWarInstance() {
        gameHandler.getNovsWarInstance();
    }
}
