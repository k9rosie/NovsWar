package com.k9rosie.novswar.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player bukkitPlayer;
    private NovsStats stats;
    private NovsTeam team;
    private HashMap<NovsPlayer, Double> attackerMap;
    private boolean deathMessages;
    private boolean isDead;
    private boolean isSpectating;
    private boolean isSettingRegion;
    private boolean hasVoted;

    private Location cornerOneBuffer;
    private RegionType regionTypeBuffer;
    private String regionNameBuffer;

    public NovsPlayer (Player bukkitPlayer, NovsTeam team) {
        this.bukkitPlayer = bukkitPlayer;
        this.team = team;
        attackerMap = new HashMap<NovsPlayer, Double>();
        stats = new NovsStats(this);
        deathMessages = true;
        isDead = false;
        isSpectating = false;
        isSettingRegion = false;
        hasVoted = false;
    }
    
    public void addAttacker(NovsPlayer player, Double damage) {
    	if(attackerMap.containsKey(player)) {
    		attackerMap.put(player, attackerMap.get(player) + damage);
    	} else {
    		attackerMap.put(player, damage);
    	}
    	//DEBUG
    	System.out.println(player.getBukkitPlayer().getName()+" attacked "+bukkitPlayer.getName()+" with "+attackerMap.get(player)+" cumulitive damage.");
    }
    
    public void clearAttackers() {
    	attackerMap.clear();
    }
    
    /**
     * Determines which NovsPlayer dealt the most assist damage
     * 
     * @param killer - The NovsPlayer that killed this player
     * @return the NovsPlayer that dealt the most assist damage. If there are no
     *   	   assists, returns null.
     */
    public NovsPlayer getAssistAttacker(NovsPlayer killer) {
    	attackerMap.remove(killer);
    	NovsPlayer assistAttacker = null;
    	System.out.println(bukkitPlayer.getName()+" is evaluating assist attackers...");
    	if(attackerMap.size() > 0) {
    		Iterator<Entry<NovsPlayer, Double>> it = attackerMap.entrySet().iterator();
        	double assistAttackerDamage = 0;
        	while (it.hasNext()) {
        		Map.Entry<NovsPlayer, Double> pair = (Map.Entry<NovsPlayer, Double>)it.next();
        		System.out.println(pair.getKey().getBukkitPlayer().getName()+" has dealt "+pair.getValue()+" damage.");
        		if(pair.getValue() > assistAttackerDamage) {
        			assistAttackerDamage = pair.getValue();
        			assistAttacker = pair.getKey();
        		}
        	}
    	}
    	System.out.println("assistAttacker is: "+assistAttacker);
    	return assistAttacker;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public NovsStats getStats() {
        return stats;
    }
    
    public NovsTeam getTeam() {
    	return team;
    }
    
    public void setTeam(NovsTeam team) {
        this.team.getScoreboardTeam().removeEntry(bukkitPlayer.getDisplayName());
        team.getScoreboardTeam().addEntry(bukkitPlayer.getDisplayName());
    	this.team = team;
    }

    public boolean canSeeDeathMessages() {
        return deathMessages;
    }

    public void setDeathMessages(boolean deathMessages) {
        this.deathMessages = deathMessages;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDeath(boolean isDead) {
        this.isDead = isDead;
    }
    
    public boolean hasVoted() {
        return hasVoted;
    }

    public void setVoted(boolean voted) {
        this.hasVoted = voted;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setSpectating(boolean isSpectating) {
        this.isSpectating = isSpectating;
    }

    public void setSettingRegion(boolean isSettingRegion) {
        this.isSettingRegion = isSettingRegion;
    }

    public boolean isSettingRegion() {
        return isSettingRegion;
    }

    public void setCornerOneBuffer(Location cornerOne) {
        cornerOneBuffer = cornerOne;
    }

    public Location getCornerOneBuffer() {
        return cornerOneBuffer;
    }

    public void setRegionTypeBuffer(RegionType regionType) {
        regionTypeBuffer = regionType;
    }

    public RegionType getRegionTypeBuffer() {
        return regionTypeBuffer;
    }

    public void setRegionNameBuffer(String regionName) {
        regionNameBuffer = regionName;
    }

    public String getRegionNameBuffer() {
        return regionNameBuffer;
    }
}