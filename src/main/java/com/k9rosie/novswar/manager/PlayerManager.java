package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;

public class PlayerManager {

    private NovsWar novswar;

    private HashMap<Player, NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
        players = new HashMap<Player, NovsPlayer>();
    }

    public HashMap<Player, NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer, novswar.getTeamManager().getDefaultTeam());
        players.put(bukkitPlayer, player);
        return player;
    }

    public NovsPlayer getNovsPlayerFromUsername(String displayName) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(displayName)) {
                return players.get(player);
            }
        }
        return null;
    }
    
    public ArrayList<NovsPlayer> getInGamePlayers() {
    	ArrayList<NovsPlayer> inGamePlayers = new ArrayList<NovsPlayer>();
    	for(NovsPlayer aPlayer : players.values()) {
    		if(aPlayer.getTeam().equals(novswar.getTeamManager().getDefaultTeam())==false) {
    			inGamePlayers.add(aPlayer);
    		}
    	}
    	return inGamePlayers;
    }
}
