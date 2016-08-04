package com.k9rosie.novswar.model;


import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class NovsInfoSign {

	private Block signBlock;
	
	public NovsInfoSign(Block block) {
		this.signBlock = block;
	}
	
	public Block getBlock() {
		return signBlock;
	}
	
	public void updateMap(String world, String gamemode) {
		if(signBlock.getState() instanceof Sign) {
			Sign sign = (Sign)signBlock.getState();
			sign.setLine(2, world);
			sign.setLine(3, gamemode);
		} else {
			System.out.println("WARNING: NovsSignBlock in "+signBlock.getWorld()+" at "+signBlock.getX()+", "+signBlock.getY()+", "+signBlock.getZ()+" is not a Sign object!");
		}
	}
	
	public void updatePlayers(int players) {
		if(signBlock.getState() instanceof Sign) {
			Sign sign = (Sign)signBlock.getState();
			sign.setLine(3, Integer.toString(players));
		} else {
			System.out.println("WARNING: NovsSignBlock in "+signBlock.getWorld()+" at "+signBlock.getX()+", "+signBlock.getY()+", "+signBlock.getZ()+" is not a Sign object!");
		}
	}
	
}
