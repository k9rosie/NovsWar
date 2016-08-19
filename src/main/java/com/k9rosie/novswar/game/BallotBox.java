package com.k9rosie.novswar.game;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;

public class BallotBox {
	
	private static Inventory ballotBox = Bukkit.createInventory(null, 9, "Vote for the next map");
	private static Material voteItem = Material.MAP;
	private NovsWar novswar;
	private int[] mapWinner; //each spot in the array matches to a world in voteWorldList
	private List<NovsWorld> ballotList;
	
	public BallotBox(NovsWar novswar) {
		this.novswar = novswar;
		mapWinner = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};//new int[9];
		ballotList = new ArrayList<NovsWorld>();
	}
	
	public void castVotes() {
			//List of enabled world names
		List<String> enabledWorlds = novswar.getNovsConfigCache().getConfig("core").getStringList("core.world.enabled_worlds");

		//Choose 9 gamemodes randomly, and get their names and gamemodes
    	Collection<NovsWorld> worlds = novswar.getNovsWorldCache().getWorlds().values();
    	ballotList.addAll(worlds);
    	Collections.shuffle(ballotList);
    	int worldCount = worlds.size();
    	if(worldCount > 9) {
    		worldCount = 9; //cap limit at 9
    	}

    	//Remove disabled worlds
    	for(int k = 0; k < ballotList.size(); k++) {
    		String currentBallotName = ballotList.get(k).getBukkitWorld().getName();
    		boolean isWorldEnabled = false;
    		for(String worldName : enabledWorlds) {
    			if(currentBallotName.equals(worldName)) {
    				isWorldEnabled = true;
    				break;
    			}
    		}
    		if(isWorldEnabled == false) {
    			ballotList.remove(k);
    			k--; //decrement k to account for the left-shift of elements
    		}
    	}

    	//Generate the voting options
    	ballotBox.clear(); //ensure the ballotbox is empty
    	for (int i = 0; i < worldCount && i < ballotList.size(); i++) {
    		String name = ballotList.get(i).getName();
    		String bukkitWorldName = ballotList.get(i).getBukkitWorld().getName();
    		String gamemode = novswar.getNovsConfigCache().getConfig("worlds").getString("worlds."+bukkitWorldName+".gamemode");
    		createVoteOption(voteItem, ballotBox, i, name, gamemode);
    	}
    	
    	//Open the voting screen for each player
    	for(NovsPlayer player : novswar.getNovsPlayerCache().getPlayers().values()) {
    		//player.getBukkitPlayer().sendMessage("Cast your Vote");
    		player.getBukkitPlayer().openInventory(ballotBox);
    		player.setVoted(false);
    	}
    	ChatUtil.sendBroadcast("Type '/nw vote' to vote for the next map");
	}
	
	public void recordResult(int result) {
		mapWinner[result]++;
	}
	
	public NovsWorld tallyResults() {
		NovsWorld winnerWinnerChickenDinner;
		if(ballotList.size() > 0) {
			winnerWinnerChickenDinner = ballotList.get(0); //initialize to first map by default
			int mostVotes = 0;
			for(int i = 0; i < ballotList.size(); i++) {
				if(mapWinner[i] >= mostVotes) {
					mostVotes = mapWinner[i];
					winnerWinnerChickenDinner = ballotList.get(i);
				}
			}
			Bukkit.broadcastMessage(winnerWinnerChickenDinner.getName()+" has won the vote!");
		}
		else {
			winnerWinnerChickenDinner = null;
		}
		
		return winnerWinnerChickenDinner;
	}
	
	public NovsWorld nextWorld(NovsWorld currentWorld) {
		NovsWorld nextWorld = null;
		String currentWorldName = currentWorld.getBukkitWorld().getName();
		List<String> enabledWorlds = novswar.getNovsConfigCache().getConfig("core").getStringList("core.world.enabled_worlds");
		for(int i = 0; i < enabledWorlds.size(); i++) {
			if(enabledWorlds.get(i).equals(currentWorldName)) {
				int nextIndex = i+1;
				if(nextIndex == enabledWorlds.size()) {
					nextIndex = 0;
				}
				for(NovsWorld nworld : novswar.getNovsWorldCache().getWorlds().values()) {
					if(nworld.getBukkitWorld().getName().equals(enabledWorlds.get(nextIndex))) {
						nextWorld = nworld;
					}
				}
			}
		}
		
		return nextWorld;
	}
	
	public static void createVoteOption(Material material, Inventory inv, int slot, String name, String lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		List<String> loreList = new ArrayList<String>();
		loreList.add(lore);
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inv.setItem(slot, item);
	}
	
	public Inventory getBallots() {
		return ballotBox;
	}
	
	public Material getVoteItem() {
		return voteItem;
	}

}
