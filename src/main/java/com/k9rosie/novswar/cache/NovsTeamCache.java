package com.k9rosie.novswar.cache;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarJoinTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.ColorParser;
import com.k9rosie.novswar.util.Messages;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class NovsTeamCache {

    private NovsWar novswar;
    private ArrayList<NovsTeam> teams;
    private NovsTeam defaultTeam;
    private int messageTime;
    private int messageTask;

    public NovsTeamCache(NovsWar novswar) {
        this.novswar = novswar;
        teams = new ArrayList<NovsTeam>();
        messageTime = 0;
        messageTask = 0;
    }

    public void initialize() {
        teams.add(new NovsTeam("default", ChatColor.GRAY, false, false, false));
        loadTeams(); // load teams' data from config
    }

    public ArrayList<NovsTeam> getTeams() {
        return teams;
    }

    public NovsTeam getDefaultTeam() {
        return teams.get(0);
    }

    public NovsTeam getTeam(String teamName) {
        for (NovsTeam team : teams) {
            if (teamName.equalsIgnoreCase(team.getTeamName())) {
                return team;
            }
        }
        return null;
    }

    public void loadTeams() {
        FileConfiguration teamsConfig = novswar.getNovsConfigCache().getConfig("teams");
        List<Map<?, ?>> teamInfoMaps = teamsConfig.getMapList("teams");


        for(Map<?, ?> teamInfoMap : teamInfoMaps) {
            HashMap<String, Object> teamInfo = (HashMap<String, Object>) teamInfoMap;
            String teamName = (String) teamInfo.get("name");
            ChatColor color = ColorParser.parseString((String) teamInfo.get("color"));
            boolean canBeDamaged = (boolean) teamInfo.get("can_be_damaged");
            boolean canAttack = (boolean) teamInfo.get("can_attack");
            boolean friendlyFire = (boolean) teamInfo.get("friendly_fire");
            NovsTeam team = new NovsTeam(teamName, color, canBeDamaged, canAttack, friendlyFire);
            teams.add(team);
        }
    }
    
    public void assignPlayerTeam(NovsPlayer player) {
    	ArrayList<NovsTeam> enabledTeams = novswar.getGameHandler().getGame().getTeams();
    	// novsloadout has its own way of sorting players, only run this code if it isnt enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("NovsLoadout")) {
        	//Determine which team has fewer players
        	NovsTeam smallestTeam = enabledTeams.get(0);
        	int smallest = smallestTeam.getPlayers().size();
            for (NovsTeam team : enabledTeams) {
            	if(team.getPlayers().size() <= smallest) {
            		smallest = team.getPlayers().size();
            		smallestTeam = team;
            	}
            }
            forcePlayerTeam(player, smallestTeam);
            
        } else {
        	
        	//TODO Call NovsLoadout's sorting algorithm
        	
        }
    }
    
    /**
     * Sets a players team, health, hunger and teleports them to their team's spawn
     * @param player
     * @param team
     */
    public void forcePlayerTeam(NovsPlayer player, NovsTeam team) {
    	Game game = novswar.getGameHandler().getGame();
    	NovsWarJoinTeamEvent event = new NovsWarJoinTeamEvent(game, player, team);
        Bukkit.getServer().getPluginManager().callEvent(event);
    	
        if(event.isCancelled()==false) {
        	String message;
        	if(game.getTeams().contains(team)) {
        		player.setTeam(team);
        		//novsWar.printDebug("Assigning team "+team.getTeamName()+" location "+world.getTeamSpawns().get(team).toString());
                player.getBukkitPlayer().teleport(game.getWorld().getTeamSpawnLoc(team));
                player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
                player.getBukkitPlayer().setFoodLevel(20);
                message = Messages.JOIN_TEAM.toString().replace("%team_color%", team.getColor().toString()).replace("%team%", team.getTeamName());
        	} else {
        		message = Messages.CANNOT_JOIN_TEAM.toString().replace("%team_color%", team.getColor().toString()).replace("%team%", team.getTeamName());
        	}
        	ChatUtil.sendNotice(player,  message);
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
                for(NovsPlayer player : novswar.getNovsPlayerCache().getGamePlayers()) {
                    SendTitle.sendTitle(player.getBukkitPlayer(), 0, 2000, 0, " ", "Team Auto-Balance in "+messageTime+"...");
                    autobalancePlayers.add(player);
                }
                messageTime--;
                if(messageTime <= 0) {
                    Bukkit.getScheduler().cancelTask(messageTask);
                    //Set every player's team to default
                    for(NovsPlayer player : autobalancePlayers) {
                        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 0, 0, " ", "");
                        player.setTeam(novswar.getNovsTeamCache().getDefaultTeam());
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
        for(int sourceIndex = 0; sourceIndex < novswar.getNovsTeamCache().getTeams().size(); sourceIndex++) {
            targetIndex = sourceIndex + 1;
            if(targetIndex >= novswar.getNovsTeamCache().getTeams().size()) {
                targetIndex = 0;
            }
            rotationMap.put(novswar.getNovsTeamCache().getTeams().get(sourceIndex), novswar.getNovsTeamCache().getTeams().get(targetIndex));
        }
        //Switch teams for each player in-game
        for(NovsPlayer player : novswar.getNovsPlayerCache().getGamePlayers()) {
            NovsTeam newTeam = rotationMap.get(player.getTeam());
            player.setTeam(newTeam);
            player.getBukkitPlayer().teleport(game.getWorld().getTeamSpawnLoc(newTeam));
            player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
            player.getBukkitPlayer().setFoodLevel(20);
        }
    }
}
