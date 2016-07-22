package com.k9rosie.novswar.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsWorld;

public class BallotBox {
	
	private static Inventory ballotBox = Bukkit.createInventory(null, 9, "Vote for the next map");
	private NovsWar novswar;
	private int mapWinner[]; //each spot in the array matches to a world in voteWorldList
	private List<NovsWorld> voteWorldList;
	
	public BallotBox(NovsWar novswar) {
		this.novswar = novswar;
		mapWinner = new int[9];
		voteWorldList = new ArrayList<NovsWorld>();
	}
	
	public void castVotes() {
		List<String> enabledWorlds = novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds");
		
		//Choose 9 gamemodes randomly, and get their names and gamemodes
    	HashSet<NovsWorld> worlds = novswar.getWorldManager().getWorlds();
    	voteWorldList.addAll(worlds);
    	Collections.shuffle(voteWorldList);
    	int worldCount = worlds.size();
    	if(worldCount > 9) {
    		worldCount = 9; //cap limit at 9
    	}
    	
    	//Remove disabled worlds
    	for(int k = 0; k < voteWorldList.size(); k++) {
    		if(enabledWorlds.contains(voteWorldList.get(k)) == false) {
    			voteWorldList.remove(k);
    			k--; //decrement k to account for the left-shift of elements
    		}
    	}

    	//Generate the voting options
    	for (int i = 0; i < worldCount; i++) {
    		String name = voteWorldList.get(i).getName();
    		String gamemode = novswar.getConfigurationCache().getConfig("worlds").getString(name+".gamemode");
    		createVoteOption(Material.BEDROCK, ballotBox, i, name, gamemode);
    	}
    	
    	//Open the voting screen for each player
    	for(NovsPlayer player : novswar.getPlayerManager().getPlayers()) {
    		player.getBukkitPlayer().sendMessage("Cast your Vote");
    		player.getBukkitPlayer().openInventory(ballotBox);
    	}
	}
	
	public void recordResult(int result) {
		mapWinner[result]++;
	}
	
	public NovsWorld tallyResults() {
		NovsWorld winnerWinnerChickenDinner = voteWorldList.get(0); //initialize to first map by default
		int mostVotes = 0;
		for(int i = 0; i < voteWorldList.size(); i++) {
			if(mapWinner[i] >= mostVotes) {
				mostVotes = mapWinner[i];
				winnerWinnerChickenDinner = voteWorldList.get(i);
			}
		}
		return winnerWinnerChickenDinner;
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

}
