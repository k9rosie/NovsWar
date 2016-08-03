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
    
    public void nextSpectatorTarget(NovsPlayer observer) {
    	ArrayList<NovsPlayer> inGamePlayers = getInGamePlayers();
		int index = 0;
		int nextIndex = 0;
		int watchdog = 0;
		//Get current index of spectator target in player list
		while(observer.getSpectatorTarget().equals(inGamePlayers.get(index))==false){
			index++;
			if(index >= inGamePlayers.size()){
				index = 0;
				watchdog++;
			}
			if(watchdog >= 2) {
				System.out.println("WARNING: nextSpectatorTarget could not find the next spectator target");
				break;
			}
		}
		//Modify player index
		nextIndex = index + 1;
		if(nextIndex >= inGamePlayers.size()) {
			nextIndex = 0;
		}
		NovsPlayer target = inGamePlayers.get(nextIndex);
		observer.setSpectatorTargetObserver(target);
		observer.getBukkitPlayer().setSpectatorTarget(target.getBukkitPlayer());
		observer.getBukkitPlayer().sendMessage("Spectating "+target.getBukkitPlayer().getName());
    }
}
