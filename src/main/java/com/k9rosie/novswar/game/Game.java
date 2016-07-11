package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Game {
    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private GameState gameState;
    private TeamData neutralTeamData;
    private HashMap<NovsTeam, TeamData> teamData;
    private NovsWar novsWar;
    private Timer deathTimer;
    private GameTimer gameTimer;
    private Scoreboard scoreboard;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        teamData = new HashMap<NovsTeam, TeamData>();
        gameState = GameState.WAITING_FOR_PLAYERS;
        novsWar = gameHandler.getNovsWarInstance();
        deathTimer = new Timer();
        gameTimer = new GameTimer(this);
        scoreboard = gameHandler.getScoreboardManager().getNewScoreboard();
    }

    public void initialize() {
        gamemode.setGame(this);

        NovsTeam defaultTeam = gameHandler.getNovsWarInstance().getTeamManager().getDefaultTeam();
        Team defaultScoreboardTeam = createScoreboardTeam(defaultTeam);
        neutralTeamData = new TeamData(defaultTeam, defaultScoreboardTeam);

        // create team data for all enabled teams for the world
        List<String> enabledTeamNames = novsWar.getConfigurationCache().getConfig("worlds").getStringList("worlds."+world.getBukkitWorld().getName()+".enabled_teams");
        for (String teamName : enabledTeamNames) {
            for (NovsTeam team : novsWar.getTeamManager().getTeams()) {
                if (teamName.equalsIgnoreCase(team.getTeamName())) {
                    teamData.put(team, new TeamData(team, createScoreboardTeam(team)));
                }
            }
        }

        setupScoreboard();

        waitForPlayers();
    }

    public void endTimer() {
        gameTimer.getTask().cancel();

        if (gameState.equals(GameState.PRE_GAME)) {
            startGame();
            return;
        } else if (gameState.equals(GameState.DURING_GAME)) {
            Bukkit.broadcastMessage("Time's up!");
            endGame();
        }

    }

    public void waitForPlayers() {
        gameState = GameState.WAITING_FOR_PLAYERS;
        Bukkit.broadcastMessage("Waiting for players");

    }

    public void preGame() {
        gameState = GameState.PRE_GAME;
        int gameTime = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.pre_game_timer");
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
    }

    public void startGame() {
        gameState = GameState.DURING_GAME;
        gamemode.onNewGame();

        int gameTime = gamemode.getGameTime();
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();

        // TODO: start timer
        // TODO: adjust game score according to gamemode
        // TODO: teleport all players to their team's designated spawn points
        // TODO: start schedulers for the world's regions
        // TODO: start game timer according to gamemode
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
        // TODO: teleport all players to their spawn points
        // TODO: stop timer
        // TODO: stop schedulers for the world's regions
    }

    public void unpauseGame() {
        if (!gameState.equals(GameState.PAUSED)) {
            return;
        }


    }

    public void endGame() {
        gameState = GameState.POST_GAME;
        gamemode.onEndGame();

        int gameTime = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.post_game_timer");
        gameTimer.setTime(gameTime);
        gameTimer.startTimer();
        // TODO: stop timer
        // TODO: teleport players to spawn points
        // TODO: start voting if enabled
        // TODO: pick next world and request a new game from the gameHandler
    }

    public void startVoting() {

    }

    public boolean checkPlayerCount() {
        int total = 0;
        for (TeamData data : teamData.values()) {
            total += data.getPlayers().size();
        }

        int required = novsWar.getConfigurationCache().getConfig("core").getInt("core.game.minimum_players");
        if (total >= required) {
            return true;
        } else {
            return false;
        }
    }

    public TeamData getNeutralTeamData() {
        return neutralTeamData;
    }

    public HashMap<NovsTeam, TeamData> getTeamData() {
        return teamData;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public NovsTeam getPlayerTeam(NovsPlayer player) {

        // first check neutral team data
        for (NovsPlayer p : neutralTeamData.getPlayers()) {
            if (player.equals(p)) {
                return neutralTeamData.getTeam();
            }
        }

        for (TeamData data : teamData.values()) {
            for (NovsPlayer p : data.getPlayers()) {
                if (player.equals(p)) {
                    return data.getTeam();
                }
            }
        }

        return null;
    }

    public void setPlayerTeam(NovsPlayer player, NovsTeam team) {
        NovsTeam currentTeam = getPlayerTeam(player);
        TeamData currentTeamData;

        // if the player belongs to the neutral team
        if (neutralTeamData.getPlayers().contains(player)) {
            currentTeamData = neutralTeamData;
        } else {
            currentTeamData = teamData.get(currentTeam);
        }
        currentTeamData.getPlayers().remove(player);
        currentTeamData.getScoreboardTeam().removeEntry(player.getBukkitPlayer().getDisplayName());
        teamData.get(team).getPlayers().add(player);
        teamData.get(team).getScoreboardTeam().addEntry(player.getBukkitPlayer().getDisplayName());
    }

    public void scheduleDeath(NovsPlayer player, int seconds) {
        player.setDeath(true);
        player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
        int rand = new Random().nextInt();
        NovsPlayer spec = (NovsPlayer) teamData.get(getPlayerTeam(player)).getPlayers().toArray()[rand];
        player.getBukkitPlayer().setSpectatorTarget(spec.getBukkitPlayer());

        deathTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setDeath(false);

            }
        }, seconds*1000);
    }

    public void joinGame(NovsPlayer player) {
        boolean canJoinInProgress = novsWar.getConfigurationCache().getConfig("core").getBoolean("core.game.join_in_progress");

        if (!canJoinInProgress && gameState.equals(GameState.DURING_GAME)) {
            player.getBukkitPlayer().sendMessage(Messages.CANNOT_JOIN_GAME.toString());
            return;
        }

        // novsloadout has its own way of sorting players, only run this code if it isnt enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("NovsLoadout")) {
            int pCount = 0;
            NovsTeam team = null;
            for (TeamData data : teamData.values()) {
                if (data.getPlayers().size() <= pCount) {
                    pCount = data.getPlayers().size();
                    team = data.getTeam();
                }
            }

            setPlayerTeam(player, team);
            Location teamSpawn = world.getTeamSpawns().get(team);
            player.getBukkitPlayer().teleport(teamSpawn);

            String message = Messages.JOIN_TEAM.toString().replace("%team_color%", team.getColor().toString()).replace("%team%", team.getTeamName());
            player.getBukkitPlayer().sendMessage(message);

            if (gameState.equals(GameState.WAITING_FOR_PLAYERS)) {
                if (checkPlayerCount()) {
                    preGame();
                } else {
                    Bukkit.broadcastMessage("Need more players");
                }
            }
        }

    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Team createScoreboardTeam(NovsTeam team) {
        Team scoreboardTeam = scoreboard.registerNewTeam(team.getColor()+team.getTeamName());
        scoreboardTeam.setPrefix(team.getColor().toString());
        scoreboardTeam.setDisplayName(team.getColor()+team.getTeamName());
        scoreboardTeam.setAllowFriendlyFire(team.getFriendlyFire());
        scoreboardTeam.setCanSeeFriendlyInvisibles(false);
        return scoreboardTeam;
    }

    public void setupScoreboard() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }
}
