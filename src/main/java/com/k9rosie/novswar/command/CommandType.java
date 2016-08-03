package com.k9rosie.novswar.command;

public enum CommandType {
	//THIS IS NOT BEING USED ANYWHERE YET
	HELP		("novswar.command.help"),
	JOIN		("novswar.command.join"),
	LEAVE		("novswar.command.leave"),
	MAP			("novswar.command.map"),
	PLAYER		("novswar.command.player"),
	STATS		("novswar.command.player"),
	SPECTATE	("novswar.command.spectate"),
	TEAM		("novswar.command.team"),
	VOTE		("novswar.command.vote"),
	ADMIN		("novswar.command.admin");
	
	private final String permission;
	CommandType(String permission) {
		this.permission = permission;
	}
	
	public String permission() {
		return permission;
	}
	
	public boolean contains(String command) {
		return (CommandType.valueOf(command) instanceof CommandType);
	}
	
}
