package com.k9rosie.novswar.game;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.event.*;
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

        // populate enabledTeams list
        ArrayList<String> list = novsWar.getConfigManager().getWorldsConfig().getWorldData().get(world.getBukkitWorld().getName()).getEnabledTeams();
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
        novsWar.getWorldManager().updateSigns(this);
        waitForPlayers();
    }

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
        // set the initial scores for the teams
        setInitialScores();
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

                for (int i = 0; i < winners.size(); i++) {
                    NovsTeam team = winners.get(i);
                    teamList.append(team.getColor()+team.getTeamName());
                    if (i != winners.size()-1) {
                        teamList.append(ChatColor.GRAY+", ");
                    }
                }

                // Display victory message for all players, given multiple victors
                SendTitle.broadcastSubtitle(teamList.toString() + " §ftie!");

            } else { // no winners (array is empty)
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
                	// clear victory message
                	for (NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
                		SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                	}
                	if (rounds <= 1) {
                		// This was the final round. Prompt voting.
                		if (novsWar.getConfigManager().getCoreConfig().getVotingEnabled() == true) {
                            ballotBox.promptVoting();
                        }
                	} else {
                		// Start a new round
                    	rounds--;
                    	setInitialScores();
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

        // if the game has started or just ended
        if (!canJoinInProgress && (gameState.equals(GameState.DURING_GAME) || gameState.equals(GameState.POST_GAME))) {
            ChatUtil.sendNotice(player, MessagesConfig.getCannotJoinGame());
            return;
        }

        // if the player is already in the game
        if (player.getPlayerState().isInGame()) {
            ChatUtil.sendNotice(player, "You're already in the game.");
            return;
        }

        // Invoke event
        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
        	assignTeam(player);
        	player.getPlayerState().setInGame(true);
        }

        // check the player count to see if that was the last player we needed to start the game
        if (gameState == GameState.WAITING_FOR_PLAYERS) {
            if (checkPlayerCount()) {
                if (paused) {
                    unpauseGame();
                } else {
                    preGame();
                }
            } else {
                int minimum = novsWar.getCoreConfig().getGameMinimumPlayers();
                String message = MessagesConfig.getNotEnoughPlayers(Integer.toString(minimum));
                ChatUtil.sendBroadcast(message);
            }
        }
    }

    public void leaveGame(NovsPlayer player) {
        NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
        Location spawn = novsWar.getWorldManager().getLobbyWorld().getTeamSpawnLoc(defaultTeam);

        if (player.getPlayerState().isInGame() == false) {
            ChatUtil.sendError(player, "You're not in the current game");
            return;
        }

        player.getPlayerState().setTeam(defaultTeam);
        player.getBukkitPlayer().teleport(spawn);
        player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.getBukkitPlayer().setFoodLevel(20);
        NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(player, this);
        Bukkit.getPluginManager().callEvent(invokeEvent);
    }
    
    public void nextGame(NovsWorld world) {
    	if(gameTimer.getTaskID() != 0) { //if there is a running timer
			gameTimer.stopTimer();
		}
        this.world.closeIntermissionGates();
		gameHandler.newGame(world);
    }

    public void balanceTeams() {
        pauseGame();
        int messageTime = novsWar.getCoreConfig().getGameRebalanceWarningTimer();
        ChatUtil.sendBroadcast("Team auto-balance will occur in "+ messageTime + " seconds");
        Bukkit.getScheduler().scheduleSyncDelayedTask(novsWar.getPlugin(), () -> {
            for (NovsPlayer player : getInGamePlayers()) {
                player.getPlayerState().setTeam(novsWar.getTeamManager().getDefaultTeam());
                assignTeam(player);
            }

            unpauseGame();
        }, messageTime*20);
    }

    public void assignTeam(NovsPlayer player) {
        NovsWarAssignTeamEvent event = new NovsWarAssignTeamEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
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
        for (NovsPlayer player : getInGamePlayers()) {
            NovsTeam team = player.getPlayerState().getTeam();
            int nextTeam = enabledTeams.indexOf(team) == enabledTeams.size()-1 ? 0 : enabledTeams.indexOf(team)+1;
            NovsTeam newTeam = enabledTeams.get(nextTeam);
            player.getPlayerState().setTeam(newTeam);
            player.getBukkitPlayer().teleport(world.getTeamSpawnLoc(newTeam));
            player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.getBukkitPlayer().setFoodLevel(20);
        }
    }

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
                    int nextIndex = (index + 1) >= validTargets.size()-1 ? 0 : index + 1; // if nextIndex is larger than the size of the validTargets then return 0

                    for (int i = 0; i < validTargets.size(); i++) {
                        NovsPlayer potentialTarget = validTargets.get(nextIndex);

                        // do some conditionals to check if they are eligible to spectate
                        if (potentialTarget.getPlayerState().isDead() == false) {
                            target = potentialTarget;
                        }
                    }
                }
            }

            if (target != null) {
                observer.getPlayerState().setSpectatorTarget(target);
            } else {
                ChatUtil.sendError(observer, "A valid spectator target couldn't be found. Teleporting to spawn...");
                observer.getBukkitPlayer().setSpectatorTarget(null);
                if (observer.getPlayerState().isSpectating()) {
                    observer.getPlayerState().quitSpectating();
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

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public ArrayList<NovsTeam> getEnabledTeams() {
        return enabledTeams;
    }

    public int getRounds() {
        return rounds;
    }

    public NovsWar getNovsWarInstance() {
        return gameHandler.getNovsWarInstance();
    }

    private void setInitialScores() {
        HashMap<NovsTeam, Integer> initialScores = gamemode.getInitialScores();
        for (NovsTeam team : initialScores.keySet()) {
            int score = initialScores.get(team);
            team.getTeamState().getScore().setScore(score);
        }
    }
}
