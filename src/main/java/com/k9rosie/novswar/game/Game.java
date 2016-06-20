package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.packet.NametagEdit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class Game {
    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private GameState gameState;
    private HashMap<NovsTeam, TeamData> teamData;
    private NovsWar novsWar;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        teamData = new HashMap<NovsTeam, TeamData>();
        gameState = GameState.WAITING_FOR_PLAYERS;
        novsWar = gameHandler.getNovsWarInstance();
    }

    public void initialize() {
        gamemode.setGame(this);

        NovsTeam defaultTeam = gameHandler.getNovsWarInstance().getTeamManager().getDefaultTeam();
        teamData.put(defaultTeam, new TeamData(defaultTeam));

        List<String> enabledTeamNames = novsWar.getConfigurationCache().getConfig("worlds").getStringList("worlds."+world.getBukkitWorld().getName()+".enabled_teams");
        for (String teamName : enabledTeamNames) {
            for (NovsTeam team : novsWar.getTeamManager().getTeams()) {
                if (teamName.equalsIgnoreCase(team.getTeamName())) {
                    teamData.put(team, new TeamData(team));
                }
            }
        }


    }

    public void startGame() {
        gamemode.onNewGame();
        // TODO: check if there are enough players to start
        // TODO: start timer
        // TODO: adjust game score according to gamemode
        // TODO: teleport all players to their team's designated spawn points
        // TODO: start schedulers for the world's regions
        // TODO: start game timer according to gamemode
    }

    public void pauseGame() {
        // TODO: teleport all players to their spawn points
        // TODO: stop timer
        // TODO: stop schedulers for the world's regions
    }

    public void endGame() {
        // TODO: stop timer
        // TODO: teleport players to spawn points
        // TODO: start voting if enabled
        // TODO: pick next world and request a new game from the gameHandler
    }

    public HashMap<NovsTeam, TeamData> getTeamData() {
        return teamData;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public NovsTeam getPlayerTeam(NovsPlayer player) {
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
        TeamData currentTeamData = teamData.get(currentTeam);
        currentTeamData.getPlayers().remove(player);
        teamData.get(team).getPlayers().add(player);
        NametagEdit.setPlayerTagColor(player.getBukkitPlayer(), team.getColor());
    }
}
