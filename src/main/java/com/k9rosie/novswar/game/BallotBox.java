package com.k9rosie.novswar.game;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;

public class BallotBox {

	private NovsWar novswar;
	private static Inventory inventory;
	private ArrayList<NovsWorld> selectedMaps;
	private int[] totalVotes;
	
	public BallotBox(NovsWar novswar) {
		this.novswar = novswar;
		selectedMaps = new ArrayList<>(9);
		inventory = Bukkit.createInventory(null, 9, "Vote for the next map");
	}
	
	public void promptVoting() {
		ArrayList<NovsWorld> worlds = new ArrayList<>(novswar.getWorldManager().getWorlds().values());
		worlds.remove(0); // remove the lobby world
		// populate selectedMaps with random worlds from the master world list
		Random random = new Random(); // generate a random wi
		int max = worlds.size() < 9 ? worlds.size() : 9;
		while (selectedMaps.size() < max) {
			int index = random.nextInt(worlds.size());
			selectedMaps.add(worlds.get(index));
		}
		totalVotes = new int[max];

    	// Generate the voting options
    	inventory.clear(); // ensure the ballotbox is empty
		for (int i = 0; i < selectedMaps.size(); i++) {
			NovsWorld world = selectedMaps.get(i);
			String name = world.getName();
			String bukkitWorldName = world.getBukkitWorld().getName();
			String gamemode = novswar.getConfigManager().getWorldsConfig().getWorldData().get(bukkitWorldName).getGamemode();

			if (gamemode == null) {
				gamemode = novswar.getGameHandler().getDefaultGamemode().getGamemodeName();
			}

			createVoteOption(Material.MAP, inventory, i, name, "A " + gamemode + " map");
		}
    	
    	// Open the voting screen for each player
    	for(NovsPlayer player : novswar.getPlayerManager().getPlayers().values()) {
    		player.getBukkitPlayer().openInventory(inventory);
    	}

    	ChatUtil.sendBroadcast("Type '/nw vote' to vote for the next map");
	}
	
	public void castVote(int position) {
		totalVotes[position]++;
	}

	public NovsWorld tallyResults() {
	    NovsWorld winner = null;
	    for (int i = 0; i < selectedMaps.size(); i++) {
	        int mostVotes = 0;
	        if (totalVotes[i] >= mostVotes) {
	            mostVotes = totalVotes[i];
	            winner = selectedMaps.get(i);
            }
        }
        return winner;
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
	
	public Inventory getInventory() {
		return inventory;
	}

}
