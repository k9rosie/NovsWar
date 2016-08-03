package com.k9rosie.novswar.model;

import org.bukkit.block.Block;

public class NovsCommandSign {
	
	private Block signBlock;
	private String command;
	
	public NovsCommandSign(Block signBlock, String command) {
		this.signBlock = signBlock;
		this.command = command;
	}
	
	public Block getSignBlock() {
		return signBlock;
	}
	
	public String getCommand() {
		return command;
	}

}
