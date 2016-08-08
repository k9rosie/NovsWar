package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;

public class PlayerDataCache {

    private NovsWar novswar;

    private HashMap<Player, NovsPlayer> players;

    public PlayerDataCache(NovsWar novswar) {
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
    
    /**
     * Selects the next in-game player to spectate
     * THIS METHOD DOES NOT CALL getSpectatorObservers()
     * @param observer
     * @return NovsPlayer - new spectate target
     */
    public NovsPlayer nextSpectatorTarget(NovsPlayer observer) {
    	System.out.println(observer.getBukkitPlayer().getName()+" is switching spectator targets");
    	ArrayList<NovsPlayer> inGamePlayers = getInGamePlayers();
		int index = inGamePlayers.indexOf(observer.getSpectatorTarget());
		int nextIndex = index + 1;
		if(nextIndex == 0) {
			System.out.println("WARNING: Could not find target in nextSpectatorTarget");
		}
		System.out.println("...Old target was "+observer.getSpectatorTarget().getBukkitPlayer().getName());
		//Modify player index
		if(nextIndex >= inGamePlayers.size()) {
			nextIndex = 0;
		}
		NovsPlayer target = inGamePlayers.get(nextIndex);
		if(target != null) {
			System.out.println("...New target is "+target.getBukkitPlayer().getName());
			observer.setSpectatorTarget(target);
			//target.getSpectatorObservers().add(observer);
			observer.getBukkitPlayer().setSpectatorTarget(target.getBukkitPlayer());
			observer.getBukkitPlayer().sendMessage("Spectating "+target.getBukkitPlayer().getName());
		} else {
			System.out.println("WARNING: nextSpectatorTarget used a null target");
		}
		
		return target;
    }
}
