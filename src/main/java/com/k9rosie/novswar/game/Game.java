package com.k9rosie.novswar.game;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarEndGameEvent;
import com.k9rosie.novswar.event.NovsWarJoinGameEvent;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private GameState gameState;
    private ArrayList<NovsTeam> enabledTeams;
    private HashMap<NovsPlayer, DeathTimer> deathTimers;
    private NovsWar novsWar;
    private GameTimer gameTimer;
    private GameScoreboard scoreboard;
    private static BallotBox ballotBox;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        enabledTeams = new ArrayList<NovsTeam>();
        deathTimers = new HashMap<NovsPlayer, DeathTimer>();
        gameState = GameState.WAITING_FOR_PLAYERS;
        novsWar = gameHandler.getNovsWarInstance();
        gameTimer = new GameTimer(this);
        scoreboard = new GameScoreboard(this);
        ballotBox = new BallotBox(novsWar);
    }

    public void initialize() {
    	//Create default team
        NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
        Team defaultScoreboardTeam = scoreboard.createScoreboardTeam(defaultTeam);
        defaultTeam.setScoreboardTeam(defaultScoreboardTeam);

        //Populate the list 'enabledTeams' with valid NovsTeam objects
        List<String> enabledTeamNames = novsWar.getConfigurationCache().getConfig("worlds").getStringList("worlds."+world.getBukkitWorld().getName()+".enabled_teams");
        for (String validTeam : enabledTeamNames) {
            for (NovsTeam team : novsWar.getTeamManager().getTeams()) {
                if (validTeam.equalsIgnoreCase(team.getTeamName())) {
                	team.setScoreboardTeam(scoreboard.createScoreboardTeam(team));
                	enabledTeams.add(team);
                }
            }
        }

        for (NovsPlayer player : novsWar.getPlayerManager().getPlayers()) {
        	player.setTeam(defaultTeam); // NovsPlayer now has private NovsTeam var
        	defaultTeam.incrementMember();
            player.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
        }

        scoreboard.initialize();

        waitForPlayers();
    }

    /**
     * endTimer()
     * Controls the next state of the game when the timer ends
     */
    public void endTimer() {
    	switch(gameState) {
    	case PRE_GAME :
    		startGame();
    		break;
    	case DURING_GAME :
    		endGame();
    		break;
    	case POST_GAME :
    		gameHandler.newGame(ballotBox.tallyResults());
    		break;
    	default :
    		break;
    	}
    }

    public void waitForPlayers() {
        gameState = GameState.WAITING_FOR_PLAYERS;
        scoreboard.setSidebarTitle("Waiting for players");

    }

    public void preGame() {
        gameState = GameState.PRE_GAME;
        world.respawnBattlefields();

        int gameTime = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.pre_game_timer");
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
    }

    public void startGame() {
        gameState = GameState.DURING_GAME;

        world.openIntermissionGates();

        int gameTime = gamemode.getGameTime();
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
        Bukkit.broadcastMessage("Starting Round");
        for (NovsPlayer player : novsWar.getPlayerManager().getPlayers()) {
            player.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(novsWar.getTeamManager().getDefaultTeam()));
        }

        // TODO: start timer
        // TODO: adjust game score according to gamemode
        // TODO: teleport all players to their team's designated spawn points
        // TODO: start schedulers for the world's regions
        // TODO: start game timer according to gamemode
        
        //gamemode.hook(this); ???
        
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;

        world.closeIntermissionGates();
        // TODO: teleport all players to their spawn points
        // TODO: stop timer
        // TODO: stop schedulers for the world's regions
    }

    public void unpauseGame() {
        if (!gameState.equals(GameState.PAUSED)) {
            return;
        }


    }

    /**
     * endGame()
     * Controls what happens during the post-game
     */
    public void endGame() {
        NovsWarEndGameEvent event = new NovsWarEndGameEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            gameState = GameState.POST_GAME;
            world.closeIntermissionGates();
            world.respawnBattlefields();
            int gameTime = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.post_game_timer");
            gameTimer.setTime(gameTime);
            gameTimer.startTimer();
            
            //Teleport each player to their team's spawn
            for (NovsPlayer player : novsWar.getPlayerManager().getPlayers()) {
            	NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
            	player.setTeam(defaultTeam);
            	defaultTeam.incrementMember();
                player.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(player.getTeam()));
            }
            
            //Check if voting is enabled
            if(novsWar.getConfigurationCache().getConfig("core").getBoolean("core.voting.enabled") == true) {
            	ballotBox.castVotes();
            }
        }
    }

    public void clockTick() {
        String secondsString = Integer.toString(gameTimer.getSeconds());
        String minutesString = Integer.toString(gameTimer.getMinutes());
        String gameStateString = "";

        if (gameState == GameState.PRE_GAME) {
            gameStateString = ChatColor.GRAY + "Setting up: ";
        } else if (gameState == GameState.DURING_GAME) {
            gameStateString = "";
        } else if (gameState == GameState.POST_GAME) {
            gameStateString = ChatColor.GRAY + "Post game: ";
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

    public boolean checkPlayerCount() {
        int numPlayers = novsWar.getPlayerManager().getPlayers().size();
        int required = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.minimum_players");
        if (numPlayers >= required) {
            return true;
        } else {
            return false;
        }
    }
    
    public ArrayList<NovsTeam> getTeams() {
    	return enabledTeams;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void scheduleDeath(NovsPlayer player, int seconds) {
        player.setDeath(true);
        player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
        player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());

        // code to set spectator target
        /*int rand = new Random().nextInt();
        NovsPlayer spec = (NovsPlayer) teamData.get(getPlayerTeam(player)).getPlayers().toArray()[rand];
        player.getBukkitPlayer().setSpectatorTarget(spec.getBukkitPlayer());*/

        DeathTimer timer = new DeathTimer(this, seconds, player);
        timer.startTimer();
        deathTimers.put(player, timer);
    }

    public void deathTick(NovsPlayer player) {
        DeathTimer timer = deathTimers.get(player);

        player.getBukkitPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName("Respawn in " + timer.getSeconds() + "...");
    }

    public void respawn(NovsPlayer player) {
        DeathTimer timer = deathTimers.get(player);
        timer.stopTimer();
        deathTimers.remove(player);
        player.getBukkitPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(scoreboard.getSidebarTitle());

        if (player.isDead()) {
            NovsTeam team = player.getTeam();

            player.setDeath(false);
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            player.getBukkitPlayer().teleport(world.getTeamSpawns().get(team));
        }
    }

    public void joinGame(NovsPlayer player) {
        NovsWarJoinGameEvent event = new NovsWarJoinGameEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            boolean canJoinInProgress = novsWar.getConfigurationCache().getConfig("core").getBoolean("core.game.join_in_progress");

            if (!canJoinInProgress && gameState.equals(GameState.DURING_GAME)) {
                player.getBukkitPlayer().sendMessage(Messages.CANNOT_JOIN_GAME.toString());
                return;
            }

            // novsloadout has its own way of sorting players, only run this code if it isnt enabled
            if (!Bukkit.getPluginManager().isPluginEnabled("NovsLoadout")) {
            	//Determine which team has fewer players
            	int smallest = 0;
            	NovsTeam smallestTeam = null;
                for (NovsTeam team : enabledTeams) {
                	if(team.getMemberCount() <= smallest) {
                		smallest = team.getMemberCount();
                		smallestTeam = team;
                	}
                }
                player.setTeam(smallestTeam);
                smallestTeam.incrementMember();
                
                Location teamSpawn = world.getTeamSpawns().get(smallestTeam);
                player.getBukkitPlayer().teleport(teamSpawn);

                String message = Messages.JOIN_TEAM.toString().replace("%team_color%", smallestTeam.getColor().toString()).replace("%team%", smallestTeam.getTeamName());
                player.getBukkitPlayer().sendMessage(message);

                if (gameState.equals(GameState.WAITING_FOR_PLAYERS)) {
                    if (checkPlayerCount()) {
                        preGame();
                    }
                }
            }
        }
    }

    public GameScoreboard getScoreboard() {
        return scoreboard;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }
    
    public static BallotBox getBallotBox() {
    	return ballotBox;
    }
}
