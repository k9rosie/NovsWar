package com.k9rosie.novswar.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player bukkitPlayer;
    private NovsPlayer spectatorTarget;
    private ArrayList<NovsPlayer> spectatorObservers;
    private NovsStats stats;
    private NovsTeam team;
    private HashMap<NovsPlayer, Double> attackerMap;
    private boolean deathMessages;
    private boolean isDead; //Whether a player has died and is spectating via death cam
    private boolean isSpectating; //Whether a player in the lobby entered spectator mode
    private boolean isSettingRegion;
    private boolean hasVoted;
    private boolean isShiftToggled;
    private boolean isGlobalChat; //True for global, false for team

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
        isShiftToggled = true;
        isGlobalChat = true;
        spectatorTarget = null;
        spectatorObservers = new ArrayList<NovsPlayer>();
    }
    
    public void addAttacker(NovsPlayer player, Double damage) {
    	if(attackerMap.containsKey(player)) {
    		attackerMap.put(player, attackerMap.get(player) + damage);
    	} else {
    		attackerMap.put(player, damage);
    	}
    	ChatUtil.printDebug(player.getBukkitPlayer().getName()+" attacked "+bukkitPlayer.getName()+" with "+attackerMap.get(player)+" cumulitive damage.");
    }
    
    public void clearAttackers() {
    	attackerMap.clear();
    }
    
    /**
     * Determines which NovsPlayer dealt the most assist damage
     * 
     * @param killer - The NovsPlayer that killed this player. If this is null, then the highest attacker
     * 				overall will be selected
     * @return the NovsPlayer that dealt the most assist damage. If there are no
     *   	   assists, returns null.
     */
    public NovsPlayer getAssistAttacker(NovsPlayer killer) {
    	if(killer != null) {
    		attackerMap.remove(killer);
    	}
    	NovsPlayer assistAttacker = null;
    	if(attackerMap.size() > 0) {
    		Iterator<Entry<NovsPlayer, Double>> it = attackerMap.entrySet().iterator();
        	double assistAttackerDamage = 0;
        	while (it.hasNext()) {
        		Map.Entry<NovsPlayer, Double> pair = (Map.Entry<NovsPlayer, Double>)it.next();
        		//System.out.println(pair.getKey().getBukkitPlayer().getName()+" has dealt "+pair.getValue()+" damage.");
        		if(pair.getValue() > assistAttackerDamage) {
        			assistAttackerDamage = pair.getValue();
        			assistAttacker = pair.getKey();
        		}
        	}
        	//System.out.println("assistAttacker is: "+assistAttacker.getBukkitPlayer().getName());
    	}
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
    
    public boolean isGlobalChat() {
        return isGlobalChat;
    }

    public void setGlobalChat(boolean isGlobalChat) {
        this.isGlobalChat = isGlobalChat;
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
    
    public void setSpectatorTarget(NovsPlayer target) {
    	ChatUtil.printDebug("Setting "+this.getBukkitPlayer().getName()+"'s target to "+target.getBukkitPlayer().getName());
    	spectatorTarget = target;
    	bukkitPlayer.teleport(target.getBukkitPlayer().getLocation());
    	bukkitPlayer.setSpectatorTarget(target.getBukkitPlayer());
    	ChatUtil.sendNotice(this, "Spectating "+target.getBukkitPlayer().getName());
    }
    
    public ArrayList<NovsPlayer> getSpectatorObservers() {
    	return spectatorObservers;
    }
    
    public NovsPlayer getSpectatorTarget() {
    	return spectatorTarget;
    }
    
    public boolean isShiftToggled() {
    	return isShiftToggled;
    }
    
    public void setShiftToggled(boolean toggle) {
    	isShiftToggled = toggle;
    }

    /**
     * Iterates through the list of in-game players for the next spectator target. If
     * there is no available target, TPs player to their spawn.
     * @param game
     * @return The spectator target
     */
    public NovsPlayer nextSpectatorTarget(Game game) {
    	NovsPlayer target = null;
    	if(isDead || isSpectating) {
	    	ChatUtil.printDebug(bukkitPlayer.getName()+" is switching spectator targets");
	        ArrayList<NovsPlayer> inGamePlayers = game.getGamePlayers();
	        inGamePlayers.remove(this);	//Remove this player from the options of spectator targets
	        int index = inGamePlayers.indexOf(spectatorTarget);
	        int nextIndex = index + 1; //If spectatorTarget is null, nextIndex will be 0

	        boolean foundValidTarget = false;
	        if(inGamePlayers.size() > 0) {
		        int watchdog = 0;
		        while(foundValidTarget == false) {
		        	if(nextIndex >= inGamePlayers.size()) {
		                nextIndex = 0;
		            }
		        	NovsPlayer potentialTarget = inGamePlayers.get(nextIndex);
		        	if(potentialTarget.isDead()==false) {
		        		target = potentialTarget;
		        		foundValidTarget = true;
		        	}
		        	if(watchdog >= inGamePlayers.size()){
	                    ChatUtil.printDebug("Could not find valid spectator target");
		        		break;
		        	}
		        	watchdog++;
		        	nextIndex++;
		        }
	        }
	        if(foundValidTarget) {
                ChatUtil.printDebug("...New target is "+target.getBukkitPlayer().getName());
	        	this.setSpectatorTarget(target);
	        } else {
	        	if(isSpectating == false) {
	        		bukkitPlayer.teleport(game.getWorld().getTeamSpawnLoc(team));
	        		ChatUtil.printDebug("WARNING: nextSpectatorTarget could not find a valid target for player "+bukkitPlayer.getName());
	        	}
	        }
    	} else {
            ChatUtil.printDebug("WARNING: Attempted to call nextSpectatorTarget on an alive/non-spectating player");
    	}
        return target;
    }
    
}